package com.github.fabiosoaza.salesimporter.infrastructure.route;

import static com.github.fabiosoaza.salesimporter.infrastructure.configuration.RoutesConstants.*;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.fabiosoaza.salesimporter.infrastructure.configuration.FileSystemConfigs;

@Component
public class FileSystemIORoutes extends RouteBuilder{

	private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemIORoutes.class);
	
	private FileSystemConfigs applicationConfigs;

	@Autowired
	public FileSystemIORoutes(FileSystemConfigs applicationConfigs) {
		super();
		this.applicationConfigs = applicationConfigs;
	}

	@Override
	public void configure() throws Exception {
		from(applicationConfigs.getImportUri())
			.routeId(FILESYSTEM_READING_ROUTE_ID)
			.log(LoggingLevel.INFO, LOGGER, "Importando o arquivo ${headers." + Exchange.FILE_NAME + "}")
			.to(SALES_IMPORT_ROUTE_NAME)
		.end();
		
		from(FILESYSTEM_WRITING_ROUTE_NAME)
			.routeId(FILESYSTEM_WRITING_ROUTE_ID)
			.log(LoggingLevel.INFO, LOGGER, "Exportando o arquivo ${headers." + Exchange.FILE_NAME + "}")
			.to(applicationConfigs.getExportUri())
		.end();
		
		from(FILESYSTEM_WRITING_ERRORS_ROUTE_NAME)
			.routeId(FILESYSTEM_WRITING_ERRORS_ROUTE_ID)
			.log(LoggingLevel.INFO, LOGGER, "Exportando o arquivo com os erros da importação ${headers." + Exchange.FILE_NAME + "}")
			.to(applicationConfigs.getExportErrorsUri())
		.end();
	}
	
}
