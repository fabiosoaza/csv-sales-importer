package com.github.fabiosoaza.salesimporter.predicate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;

import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;

import com.github.fabiosoaza.salesimporter.infrastructure.predicate.ExchangeContainErrorsPredicate;
import com.github.fabiosoaza.salesimporter.infrastructure.util.ParseErrorsUtils;

import test.CamelTestUtils;

public class ExchangeContainErrorsPredicateTest {

	private ExchangeContainErrorsPredicate predicate = new ExchangeContainErrorsPredicate();
	
	@Test
	public void shouldMatchIfExchangeContainErrors() {
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		exchange.setProperty(ParseErrorsUtils.ERRORS_PROPERTY, Arrays.asList("Parsing Error"));
		assertThat(predicate.matches(exchange)).isTrue();
	}

	@Test
	public void shouldMatchIfPropertyWasNotCreated() {
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		assertThat(predicate.matches(exchange)).isFalse();
	}
	
	@Test
	public void shouldMatchIfPropertyIsNull() {
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		exchange.setProperty(ParseErrorsUtils.ERRORS_PROPERTY, null);
		assertThat(predicate.matches(exchange)).isFalse();
	}
	
	@Test
	public void shouldMatchIfPropertyIsEmpty() {
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		exchange.setProperty(ParseErrorsUtils.ERRORS_PROPERTY, Collections.emptyList());
		assertThat(predicate.matches(exchange)).isFalse();
	}
	

	
}
