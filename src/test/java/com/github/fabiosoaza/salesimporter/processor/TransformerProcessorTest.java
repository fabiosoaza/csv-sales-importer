package com.github.fabiosoaza.salesimporter.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.fabiosoaza.salesimporter.infrastructure.component.SalesSummarizer;
import com.github.fabiosoaza.salesimporter.infrastructure.processor.TransformerProcessor;
import com.github.fabiosoaza.salesimporter.infrastructure.record.ImportRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.validator.ImportRecordValidator;

import test.CamelTestUtils;
import test.TestFixtures;


@ExtendWith(MockitoExtension.class)
public class TransformerProcessorTest {

	@InjectMocks
	private TransformerProcessor transformerProcessor;
	
	@Mock
	private SalesSummarizer salesSumarizer;
	
	@Mock
	private ImportRecordValidator importRecordValidator;
	
	@Captor
	private ArgumentCaptor<List<ImportRecord>> captor; 
	
	@Test
	public void shouldWriteToExchangeIfRecordsAreEmpty() throws Exception {
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		exchange.getMessage().setBody(Collections.emptyList());
		transformerProcessor.process(exchange);
		verify(salesSumarizer).sum(captor.capture());
		assertThat(captor.getValue()).isEmpty();
	}
	
	@Test
	public void shouldWriteToExchangeIfRecordsAreInvalid() throws Exception {
		when(importRecordValidator.validate(any())).thenReturn(false);
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		exchange.getMessage().setBody(TestFixtures.defaultValidRecords());
		transformerProcessor.process(exchange);
		verify(salesSumarizer).sum(captor.capture());
		assertThat(captor.getValue()).isEmpty();
	}
	
	@Test
	public void shouldWriteToExchangeIfRecordsAreValid() throws Exception {
		when(importRecordValidator.validate(any())).thenReturn(true);
		DefaultExchange exchange = CamelTestUtils.createDefaultExchange();
		List<ImportRecord> defaultValidRecords = TestFixtures.defaultValidRecords();
		exchange.getMessage().setBody(defaultValidRecords);
		transformerProcessor.process(exchange);
		verify(salesSumarizer).sum(captor.capture());
		assertThat(captor.getValue()).hasSize(defaultValidRecords.size());
	}
	
	
}
