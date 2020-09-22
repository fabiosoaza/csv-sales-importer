package com.github.fabiosoaza.salesimporter.predicate;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.camel.Predicate;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;

import com.github.fabiosoaza.salesimporter.infrastructure.predicate.ParsePredicates;

import test.CamelTestUtils;

public class ParsePredicatesTest {

	@Test
	public void shouldMatchIfRecordIsSeller() {
		Predicate predicate = ParsePredicates.isSeller();
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		exchange.getIn().setBody("001ç1234567891234çPedroç50000");
		assertThat(predicate.matches(exchange)).isTrue();
	}
	
	@Test
	public void shouldMatchIfRecordIsClient() {
		Predicate predicate = ParsePredicates.isClient();
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		exchange.getIn().setBody("002ç2345675434544345çJose da SilvaçRural");
		assertThat(predicate.matches(exchange)).isTrue();
	}
	
	@Test
	public void shouldMatchIfRecordIsSale() {
		Predicate predicate = ParsePredicates.isSale();
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		exchange.getIn().setBody("003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro");
		assertThat(predicate.matches(exchange)).isTrue();
	}
	
}
