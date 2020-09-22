package com.github.fabiosoaza.salesimporter.predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.fabiosoaza.salesimporter.infrastructure.predicate.IsValidRecordPredicate;
import com.github.fabiosoaza.salesimporter.infrastructure.record.ClientRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.validator.ImportRecordValidator;

import test.CamelTestUtils;

@ExtendWith(MockitoExtension.class)
public class IsValidRecordPredicateTest {

	@InjectMocks
	private IsValidRecordPredicate predicate;
	
	@Mock
	private ImportRecordValidator validator;
	
	@Test
	public void shouldMatchIfRecordIsValid() {
		when(validator.validate(any())).thenReturn(true);
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		exchange.getMessage().setBody(new ClientRecord());
		assertThat(predicate.matches(exchange)).isTrue();
	}
	
	@Test
	public void shouldNotMatchIfIsInValid() {
		when(validator.validate(any())).thenReturn(false);
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		assertThat(predicate.matches(exchange)).isFalse();
	}
	
	
}
