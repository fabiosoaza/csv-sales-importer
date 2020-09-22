package com.github.fabiosoaza.salesimporter.infrastructure.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.camel.Exchange;

public class ParseErrorsUtils {
	
	public static String ERRORS_PROPERTY = "parseErrors";
	
	public static void addErrorMessageToExchange(Exchange exchange, String errorMessage) {
		addErrorsToExchange(exchange, Arrays.asList(errorMessage));
	}
	
	public static void addErrorsToExchange(Exchange exchange, List<String> errors) {
		Set<String> parseErrors = new LinkedHashSet<String>(extractExchangeErrors(exchange));
		List<String> newErrors = Optional.ofNullable(errors).orElse(Collections.emptyList());
		if(!newErrors.isEmpty()) {
			parseErrors.addAll(new LinkedHashSet<String>(newErrors));
			exchange.setProperty(ERRORS_PROPERTY, new ArrayList<String>(parseErrors));
		}
	}

	@SuppressWarnings("unchecked")
	public static List<String> extractExchangeErrors(Exchange exchange) {
		return Optional.ofNullable(exchange.getProperty(ERRORS_PROPERTY, List.class))
				.orElse(new ArrayList<String>());
	}
	
	public static boolean hasExchangeErrors(Exchange exchange) {
		return !extractExchangeErrors(exchange).isEmpty();
	}

}
