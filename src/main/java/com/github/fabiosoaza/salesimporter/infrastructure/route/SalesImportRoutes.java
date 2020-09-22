package com.github.fabiosoaza.salesimporter.infrastructure.route;

import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.EXPORT_ERRORS_ROUTE_NAME;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.EXPORT_REPORT_ROUTE_NAME;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.SALES_IMPORT_ROUTE_ID;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.SALES_IMPORT_ROUTE_NAME;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.SALES_PARSE_ROUTE_ID;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.SALES_PARSE_ROUTE_NAME;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.TRANSFORM_ROUTE_NAME;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.UNMARSHAL_CLIENT_ROUTE_ID;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.UNMARSHAL_CLIENT_ROUTE_NAME;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.UNMARSHAL_SALE_ROUTE_ID;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.UNMARSHAL_SALE_ROUTE_NAME;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.UNMARSHAL_SELLER_ROUTE_ID;
import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.UNMARSHAL_SELLER_ROUTE_NAME;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.fabiosoaza.salesimporter.infrastructure.predicate.IsValidRecordPredicate;
import com.github.fabiosoaza.salesimporter.infrastructure.predicate.ParsePredicates;
import com.github.fabiosoaza.salesimporter.infrastructure.processor.TransformerProcessor;
import com.github.fabiosoaza.salesimporter.infrastructure.record.ClientRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SaleRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SellerRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.route.aggregationstrategy.GroupedBodyAndErrorPropertyStrategyAggregation;
import com.github.fabiosoaza.salesimporter.infrastructure.util.ParseErrorsUtils;
import com.github.fabiosoaza.salesimporter.infrastructure.validator.ImportRecordValidator;

@Component
public class SalesImportRoutes extends RouteBuilder{

	private static final Logger LOGGER = LoggerFactory.getLogger(SalesImportRoutes.class);
	public static final String WRAP_LINE_REGEX = "\\r\\n|\\n|\\r";	

	private TransformerProcessor transformerProcessor; 
	private IsValidRecordPredicate isValidRecordPredicate;
		
	@Autowired
	public SalesImportRoutes(TransformerProcessor transformerProcessor, ImportRecordValidator importRecordValidator, IsValidRecordPredicate isValidRecordPredicate) {
		super();
		this.transformerProcessor=transformerProcessor;
		this.isValidRecordPredicate = isValidRecordPredicate;
	}
		
	@Override
	public void configure() throws Exception {
		configureImportRoute();		
		configureParseRoute();		
		configureUnmarshalRoute(UNMARSHAL_SELLER_ROUTE_NAME, UNMARSHAL_SELLER_ROUTE_ID, SellerRecord.class);
		configureUnmarshalRoute(UNMARSHAL_CLIENT_ROUTE_NAME, UNMARSHAL_CLIENT_ROUTE_ID, ClientRecord.class);
		configureUnmarshalRoute(UNMARSHAL_SALE_ROUTE_NAME, UNMARSHAL_SALE_ROUTE_ID, SaleRecord.class);
		configureTransformRoute();
	}

	private void configureTransformRoute() {
		from(TRANSFORM_ROUTE_NAME)
		.routeId(TRANSFORM_ROUTE_NAME)
		.process(transformerProcessor)
		.log(LoggingLevel.DEBUG, LOGGER, "Resultado da Importação. ${body}")
		.to(EXPORT_REPORT_ROUTE_NAME)
		.to(EXPORT_ERRORS_ROUTE_NAME)
		;
	}
	
	
	private void configureParseRoute() {
		from(SALES_PARSE_ROUTE_NAME)
		.routeId(SALES_PARSE_ROUTE_ID)
		.choice()
			.when(ParsePredicates.isSeller())
				.log(LoggingLevel.DEBUG, LOGGER, "Dados Vendedor ${body}")
				.to(UNMARSHAL_SELLER_ROUTE_NAME)
			.when(ParsePredicates.isClient())
				.log(LoggingLevel.DEBUG, LOGGER, "Dados Cliente ${body}")
				.to(UNMARSHAL_CLIENT_ROUTE_NAME)
			.when(ParsePredicates.isSale())
				.log(LoggingLevel.DEBUG, LOGGER, "Dados Venda ${body}")
				.to(UNMARSHAL_SALE_ROUTE_NAME)
			.otherwise()
				.log(LoggingLevel.DEBUG, LOGGER, "Ignorando Registro ${body}")
		 .endChoice()
		 
		.end();
	}

	private void configureImportRoute() {
		from(SALES_IMPORT_ROUTE_NAME)
			.routeId(SALES_IMPORT_ROUTE_ID)
			.log(LoggingLevel.INFO, LOGGER, "Inicio SPLIT arquivo. ${headers." + Exchange.FILE_NAME + "}")
			.split(body().regexTokenize(WRAP_LINE_REGEX), new GroupedBodyAndErrorPropertyStrategyAggregation())
				.to(SALES_PARSE_ROUTE_NAME)
			.end()
			.to(TRANSFORM_ROUTE_NAME);
	}
	
	@SuppressWarnings("rawtypes")
	private void configureUnmarshalRoute(String routeEndpoint, String routeId, Class clazz) {
		from(routeEndpoint)
			.routeId(routeId)
			.doTry()
				.unmarshal().bindy(BindyType.Csv, clazz)
				.choice()
					.when(PredicateBuilder.not(isValidRecordPredicate) )
						.process(exchangeErrorsProcessor())	
					.endChoice()	
				.end()				
			.endDoTry()
			.doCatch(Exception.class)
				.log(LoggingLevel.ERROR, LOGGER, " Erro ao fazer parse da linha. Arquivo ${headers." + Exchange.FILE_NAME + "}, Exception: ${exception} - ${exception.stacktrace}")
				.process(exchangeErrorsProcessor())	
			.end();
	}

	private Processor exchangeErrorsProcessor() {
		return new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				Integer index = exchange.getProperty(Exchange.SPLIT_INDEX, Integer.class);
				String errorMessage = "Error parsing line "+(index+1);
				ParseErrorsUtils.addErrorMessageToExchange(exchange, errorMessage);						
			}
		};
	}
		
}
