package com.github.fabiosoaza.salesimporter.infrastructure.predicate;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

import com.github.fabiosoaza.salesimporter.infrastructure.util.ParseUtils;

public class ParsePredicates {
	
	public static Predicate isSeller() {
		return new Predicate() {
			@Override
			public boolean matches(Exchange exchange) {
				return ParseUtils.isSellerRecord(extractBodyContent(exchange));
			}
		};
	}
	
	public static Predicate isClient() {
		return new Predicate() {
			@Override
			public boolean matches(Exchange exchange) {
				return ParseUtils.isClientRecord(extractBodyContent(exchange));
			}
		};
	}
	
	public static Predicate isSale() {
		return new Predicate() {
			@Override
			public boolean matches(Exchange exchange) {
				return ParseUtils.isSaleRecord(extractBodyContent(exchange));
			}
		};
	}
	
	private static String extractBodyContent(Exchange exchange) {
		return exchange.getIn().getBody(String.class);
	}
	
}
