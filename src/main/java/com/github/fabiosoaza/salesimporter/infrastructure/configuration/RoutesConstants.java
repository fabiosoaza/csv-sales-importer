package com.github.fabiosoaza.salesimporter.infrastructure.configuration;

public interface RoutesConstants {

	static final String FILESYSTEM_READING_ROUTE_ID = "filesystemReadingId";
	static final String SALES_IMPORT_ROUTE_NAME = "direct:salesImport";
	static final String SALES_IMPORT_ROUTE_ID = "salesImportId";
	static final String SALES_PARSE_ROUTE_NAME = "direct:parseImport";
	static final String SALES_PARSE_ROUTE_ID = "parseImportId";
	
	static final String UNMARSHAL_SELLER_ROUTE_NAME = "direct:unmarshalSeller";
	static final String UNMARSHAL_SELLER_ROUTE_ID = "unmarshalSellerId";
	static final String UNMARSHAL_CLIENT_ROUTE_NAME = "direct:unmarshalClient";
	static final String UNMARSHAL_CLIENT_ROUTE_ID = "unmarshalClientId";
	static final String UNMARSHAL_SALE_ROUTE_NAME = "direct:unmarshalSale";
	static final String UNMARSHAL_SALE_ROUTE_ID = "unmarshalSaleId";

	static final String TRANSFORM_ROUTE_NAME = "direct:transform";
	static final String TRANSFORM_ROUTE_ID = "transformId";
	
	static final String EXPORT_REPORT_ROUTE_NAME ="direct:exportReport";
	static final String EXPORT_REPORT_ROUTE_ID ="exportReportId";
	
	static final String EXPORT_ERRORS_ROUTE_NAME ="direct:exportErrors";
	static final String EXPORT_ERRORS_ROUTE_ID ="exportErrorsId";
	
	static final String FILESYSTEM_WRITING_ROUTE_NAME = "direct:filesystemWriting";
	static final String FILESYSTEM_WRITING_ROUTE_ID = "filesystemWritingId";
	
	static final String FILESYSTEM_WRITING_ERRORS_ROUTE_NAME = "direct:filesystemWritingErrors";
	static final String FILESYSTEM_WRITING_ERRORS_ROUTE_ID = "filesystemWritingErrorsId";
	
}
