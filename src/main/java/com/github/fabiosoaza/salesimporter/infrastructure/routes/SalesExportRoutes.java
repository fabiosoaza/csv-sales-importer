package com.github.fabiosoaza.salesimporter.infrastructure.routes;

import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.EXPORT_ERRORS_ROUTE_ID;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.EXPORT_ERRORS_ROUTE_NAME;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.EXPORT_REPORT_ROUTE_ID;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.EXPORT_REPORT_ROUTE_NAME;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.FILESYSTEM_WRITING_ERRORS_ROUTE_NAME;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.FILESYSTEM_WRITING_ROUTE_NAME;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.fabiosoaza.salesimporter.infrastructure.predicate.ExchangeContainErrorsPredicate;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SummaryRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.utils.ParseErrorsUtils;

@Component
public class SalesExportRoutes extends RouteBuilder{

	private static final Logger LOGGER = LoggerFactory.getLogger(SalesExportRoutes.class);
	
	private ExchangeContainErrorsPredicate containErrors;
	
	@Autowired
	public SalesExportRoutes(ExchangeContainErrorsPredicate containErrors) {
		super();
		this.containErrors = containErrors;
	}

	@Override
	public void configure() throws Exception {
		from(EXPORT_REPORT_ROUTE_NAME)
		.routeId(EXPORT_REPORT_ROUTE_ID)
		.process(filenameProcessor("out.csv"))
		.marshal()
		.bindy(BindyType.Csv, SummaryRecord.class)
		.log(LoggingLevel.DEBUG, LOGGER, " Resultado ${body}")
		.to(FILESYSTEM_WRITING_ROUTE_NAME);
		
		from(EXPORT_ERRORS_ROUTE_NAME)
		.routeId(EXPORT_ERRORS_ROUTE_ID)
		.choice()
			.when(containErrors)
				.process(filenameProcessor("errors.csv"))
				.log(LoggingLevel.INFO, LOGGER, "Importação contém erros gerando o arquivo com os detalhes.")
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						String content = ParseErrorsUtils.extractExchangeErrors(exchange)
						.stream()
						.collect(Collectors.joining(System.lineSeparator()));
						exchange.getMessage().setBody(content);				
					}
				})
				.log(LoggingLevel.DEBUG, LOGGER, " Erros ${body}")
				.to(FILESYSTEM_WRITING_ERRORS_ROUTE_NAME)
			 .otherwise()
			 	.log(LoggingLevel.INFO, LOGGER, "Importação não contém erros.")
		.endChoice()
		.end();
		
		
	}

	private Processor filenameProcessor(final String newExtension) {
		return new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getIn().setHeader(Exchange.FILE_NAME, generateFilename(exchange));
			}
			private String generateFilename(Exchange exchange) {
				String importedFilename = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);
				return replaceExtension(importedFilename,  newExtension );
			}
			private String replaceExtension(String importedFilename, String newExtension) {
				return importedFilename.replaceFirst("[.][^.]+$", "") + "." + newExtension;
			}
			
		};
	}
	
	

}
