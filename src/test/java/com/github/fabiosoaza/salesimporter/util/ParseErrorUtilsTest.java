package com.github.fabiosoaza.salesimporter.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;

import com.github.fabiosoaza.salesimporter.infrastructure.util.ParseErrorsUtils;

import test.CamelTestUtils;

public class ParseErrorUtilsTest {

	@Test
	public void hasExchangeErrorsShouldReturnTrueIfErrorsPropertyContainsError() {
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		exchange.setProperty(ParseErrorsUtils.ERRORS_PROPERTY, Arrays.asList("Parsing Error"));
		assertThat(ParseErrorsUtils.hasExchangeErrors(exchange)).isTrue();
	}
	
	@Test
	public void hasExchangeErrorsShouldReturnFalseIfErrorsPropertyIsNull() {
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		exchange.setProperty(ParseErrorsUtils.ERRORS_PROPERTY, null);
		assertThat(ParseErrorsUtils.hasExchangeErrors(exchange)).isFalse();
	}
	
	@Test
	public void hasExchangeErrorsShouldReturnFalseIfErrorsPropertyWasNotCreated() {
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		assertThat(ParseErrorsUtils.hasExchangeErrors(exchange)).isFalse();
	}
	
	@Test
	public void hasExchangeErrorsShouldReturnFalseIfErrorsPropertyIsEmpty() {
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		exchange.setProperty(ParseErrorsUtils.ERRORS_PROPERTY, Collections.emptyList());
		assertThat(ParseErrorsUtils.hasExchangeErrors(exchange)).isFalse();
	}
	
	@Test
	public void addErrorMessageToExchangeShouldNotAddMessageIfAlreadyExists() {
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		List<String> list = new ArrayList<String>();
		list.add("Error");
		exchange.setProperty(ParseErrorsUtils.ERRORS_PROPERTY, list);
		ParseErrorsUtils.addErrorMessageToExchange(exchange, "Error");
		assertThat((List)exchange.getProperty(ParseErrorsUtils.ERRORS_PROPERTY)).hasSize(1);
	}
	
	@Test
	public void addErrorMessageToExchangeShouldAddMessageIfDoesNotExist() {
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		List<String> list = new ArrayList<String>();
		list.add("Error validating");
		exchange.setProperty(ParseErrorsUtils.ERRORS_PROPERTY, list);
		ParseErrorsUtils.addErrorMessageToExchange(exchange, "Error parsing");
		assertThat((List)exchange.getProperty(ParseErrorsUtils.ERRORS_PROPERTY)).hasSize(2);
	}
	
	@Test
	public void addErrorsToExchangeShouldNotAddMessageIfAlreadyExists() {
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		List<String> list = new ArrayList<String>();
		list.add("Error");
		exchange.setProperty(ParseErrorsUtils.ERRORS_PROPERTY, list);
		ParseErrorsUtils.addErrorsToExchange(exchange, Arrays.asList("Error"));
		assertThat((List)exchange.getProperty(ParseErrorsUtils.ERRORS_PROPERTY)).hasSize(1);
	}
	
	@Test
	public void addErrorsToExchangeShouldAddMessageIfDoesNotExist() {
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		List<String> list = new ArrayList<String>();
		list.add("Error validating");
		exchange.setProperty(ParseErrorsUtils.ERRORS_PROPERTY, list);
		ParseErrorsUtils.addErrorsToExchange(exchange, Arrays.asList("Error parsing"));
		assertThat((List)exchange.getProperty(ParseErrorsUtils.ERRORS_PROPERTY)).hasSize(2);
	}
	
}
