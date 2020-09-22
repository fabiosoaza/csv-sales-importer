package com.github.fabiosoaza.salesimporter.infrastructure.component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.fabiosoaza.salesimporter.infrastructure.parser.SaleItemsParser;
import com.github.fabiosoaza.salesimporter.infrastructure.record.ClientRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.ImportRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SaleItemRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SaleRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SellerRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SummaryRecord;

@Component
public class SalesSummarizer {

	private SaleItemsParser parser;

	@Autowired
	public SalesSummarizer(SaleItemsParser parser) {
		super();
		this.parser = parser;
	}

	public SummaryRecord sum(List<ImportRecord> records) {
		Integer totalSellers = countByType(records, SellerRecord.class);
		Integer totalClients = countByType(records, ClientRecord.class);
		String mostExpensiveSaleId = findMostExpensiveSaleId(records); 
		String worstPerformanceSeller = findWorstPerformanceSeller(records); 
		return new SummaryRecord(totalClients.intValue(), totalSellers.intValue(), mostExpensiveSaleId, worstPerformanceSeller);
	}

	private String findMostExpensiveSaleId(List<ImportRecord> records) {
		List<SaleRecord> sales = filterByType(records, SaleRecord.class)
				.map((item)->((SaleRecord)item) )
				.collect(Collectors.toList());
		Optional<SaleRecord> max = sales.stream()
				.max(Comparator.comparing(this::saleValue));
		return max.isPresent() ? max.get().getId() : "";
	}
	
	private Integer countByType(List<ImportRecord> records, Class<? extends ImportRecord> clazz) {
		Long total = filterByType(records, clazz).count();
		return total.intValue();
	}

	private Stream<ImportRecord> filterByType(List<ImportRecord> records, Class<? extends ImportRecord> clazz) {
		return Optional.ofNullable(records).orElse(Collections.emptyList())
		.stream()
		.filter(record -> record.getClass().equals(clazz));
	}
	
	private String findWorstPerformanceSeller(List<ImportRecord> records) {
		Collector<SaleRecord, ?, Map<String, Double>> aggregationBySellerNameAndSaleValue = Collectors.groupingBy(
				SaleRecord::getSalesmanName,
				Collectors.summingDouble(val ->saleValue(val).doubleValue() )						
		);
		
		Map<String, Double> aggregatedSales = filterByType(records, SaleRecord.class)
				.map((item)->((SaleRecord)item) )
				.collect(aggregationBySellerNameAndSaleValue);	
		
		Optional<Entry<String, Double>> min = aggregatedSales.entrySet().stream()
				.min(Comparator.comparing(Map.Entry::getValue));
		return min.isPresent() ? min.get().getKey() : "";
		
	}
	
	private BigDecimal saleValue(SaleRecord sale) {
		return parseSaleItems(sale).stream()
				.map(item->item.total())
				.reduce(BigDecimal.ZERO, BigDecimal::add);		
	}
	
	private List<SaleItemRecord> parseSaleItems(SaleRecord sale){
		return parser.parse(sale.getItems());
	}
	
	
	
}
