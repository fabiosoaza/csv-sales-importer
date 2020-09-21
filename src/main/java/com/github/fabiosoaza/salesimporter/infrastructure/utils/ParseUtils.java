package com.github.fabiosoaza.salesimporter.infrastructure.utils;

import java.util.Optional;

public final class ParseUtils {
	
	private static final String RECORD_CODE_SELLER = "001";
	private static final String RECORD_CODE_CLIENT = "002";
	private static final String RECORD_CODE_SALE = "003";
	public static final String RECORD_SEPARATOR = "ç";
	public static final String SALE_ITEM_RECORD_SEPARATOR = "-";
	public static final String SALES_ITEMS_SEPARATOR = ",";

	private ParseUtils() {}
	
	public static boolean isSellerRecord(String content) {
		return isContentType(content, RECORD_CODE_SELLER);
	}
	
	public static boolean isClientRecord(String content) {
		return isContentType(content, RECORD_CODE_CLIENT);
	}
	
	public static boolean isSaleRecord(String content) {
		return isContentType(content, RECORD_CODE_SALE);
	}
	
	private static boolean isContentType(String content, String tipoRegistroCabecalho) {
		return Optional.ofNullable(content)
				.orElse("")
				.startsWith(tipoRegistroCabecalho+RECORD_SEPARATOR);
	}
	
}
