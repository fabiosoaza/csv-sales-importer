package com.github.fabiosoaza.salesimporter.component;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.github.fabiosoaza.salesimporter.infrastructure.component.SalesSummarizer;
import com.github.fabiosoaza.salesimporter.infrastructure.parser.SaleItemsParser;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SummaryRecord;

import test.TestFixtures;

public class SalesSummarizerTest {

	private SaleItemsParser parser = new SaleItemsParser();
	
	private SalesSummarizer summarizer = new SalesSummarizer(parser);

	@Test
	public void shouldReturnEmptyValuesIfCollectionIsNull() {
		SummaryRecord sum = summarizer.sum(null);
		assertThat(sum.getTotalClients()).isEqualTo(0);
		assertThat(sum.getTotalSellers()).isEqualTo(0);
		assertThat(sum.getMostExpensiveSaleId()).isEmpty();
		assertThat(sum.getWorstPerformanceSellerName()).isEmpty();
	}
	
	@Test
	public void shouldReturnEmptyValuesIfCollectionIsEmpty() {
		SummaryRecord sum = summarizer.sum(Collections.emptyList());
		assertThat(sum.getTotalClients()).isEqualTo(0);
		assertThat(sum.getTotalSellers()).isEqualTo(0);
		assertThat(sum.getMostExpensiveSaleId()).isEmpty();
		assertThat(sum.getWorstPerformanceSellerName()).isEmpty();
	}
	
	@Test
	public void shouldCountTotalSellers() {
		SummaryRecord sum = summarizer.sum(TestFixtures.defaultValidRecords());
		assertThat(sum.getTotalSellers()).isEqualTo(2);
	}
	
	@Test
	public void shouldCountClients() {
		SummaryRecord sum = summarizer.sum(TestFixtures.defaultValidRecords());
		assertThat(sum.getTotalClients()).isEqualTo(2);
	}
	
	@Test
	public void shouldFindMostExpensiveSaleId() {
		SummaryRecord sum = summarizer.sum(TestFixtures.defaultValidRecords());
		assertThat(sum.getMostExpensiveSaleId()).isEqualTo("04");
	}
	
	@Test
	public void shouldFindWorstPerformanceSellerName() {
		SummaryRecord sum = summarizer.sum(TestFixtures.defaultValidRecords());
		assertThat(sum.getWorstPerformanceSellerName()).isEqualTo("Pedro");
	}
	

		
}
