package com.github.fabiosoaza.salesimporter.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileSystemConfigs {
	
	private String importUri;
	private String exportUri;
	private String exportErrorsUri;
		
	public FileSystemConfigs(@Value("${filesProcessor.importUri}") String importUri, @Value("${filesProcessor.exportUri}") String exportUri, @Value("${filesProcessor.exportErrorsUri}") String exportErrorsUri) {
		super();
		this.importUri = importUri;
		this.exportUri = exportUri;
		this.exportErrorsUri = exportErrorsUri;
	}

	public String getImportUri() {
		return importUri;
	}
	
	public String getExportUri() {
		return exportUri;
	}
	
	public String getExportErrorsUri() {
		return exportErrorsUri;
	}
	
}
