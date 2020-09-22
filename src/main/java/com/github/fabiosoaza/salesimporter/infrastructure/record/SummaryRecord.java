package com.github.fabiosoaza.salesimporter.infrastructure.record;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import com.github.fabiosoaza.salesimporter.infrastructure.util.ParseUtils;

@CsvRecord(separator = ParseUtils.RECORD_SEPARATOR, generateHeaderColumns=true)
public class SummaryRecord {

	@DataField(pos = 1, required = true, columnName = "Total Clients")
	private Integer totalClients;
	
	@DataField(pos = 2, required = true, columnName = "Total Sellers")
	private Integer totalSellers;
	
	@DataField(pos = 3, required = true, columnName = "Most Expensive Sale Id")
	private String mostExpensiveSaleId;
	
	@DataField(pos = 4, required = true, columnName = "Worst Performance Seller")
	private String worstPerformanceSellerName;

	public SummaryRecord(Integer totalClients, Integer totalSellers, String mostExpensiveSaleId,
			String worstPerformanceSellerName) {
		super();
		this.totalClients = totalClients;
		this.totalSellers = totalSellers;
		this.mostExpensiveSaleId = mostExpensiveSaleId;
		this.worstPerformanceSellerName = worstPerformanceSellerName;
	}

	public Integer getTotalClients() {
		return totalClients;
	}

	public Integer getTotalSellers() {
		return totalSellers;
	}

	public String getMostExpensiveSaleId() {
		return mostExpensiveSaleId;
	}
	
	public String getWorstPerformanceSellerName() {
		return worstPerformanceSellerName;
	}

	@Override
	public String toString() {
		return "Summary [totalClients=" + totalClients + ", totalSellers=" + totalSellers + ", mostExpensiveSaleId="
				+ mostExpensiveSaleId + ", worstPerformanceSellerName=" + worstPerformanceSellerName + "]";
	}
	

}
