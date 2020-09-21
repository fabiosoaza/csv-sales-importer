package com.github.fabiosoaza.salesimporter.infrastructure.predicate;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.springframework.stereotype.Component;

import com.github.fabiosoaza.salesimporter.infrastructure.utils.ParseErrorsUtils;

@Component
public class ExchangeContainErrorsPredicate implements Predicate{

	@Override
	public boolean matches(Exchange exchange) {
		return ParseErrorsUtils.hasExchangeErrors(exchange);
	}
	
	
}
