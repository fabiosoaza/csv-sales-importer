package com.github.fabiosoaza.salesimporter.infrastructure.record;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import com.github.fabiosoaza.salesimporter.infrastructure.utils.ParseUtils;

@CsvRecord(separator = ParseUtils.RECORD_SEPARATOR)
public class SaleRecord implements ImportRecord{

	@DataField(pos = 1, required = true)
	private String recordType;
	
	@DataField(pos = 2, required = true)
	private String id;
	
	@DataField(pos = 3, required = true)
	private String items;
	
	@DataField(pos = 4, required = true)
	private String salesmanName;
	
	public SaleRecord() {
	}
	
	public SaleRecord(String recordType, String id, String items, String salesmanName) {
		super();
		this.recordType = recordType;
		this.id = id;
		this.items = items;
		this.salesmanName = salesmanName;
	}

	public String getRecordType() {
		return recordType;
	}
	
	public String getId() {
		return id;
	}
	
	public String getItems() {
		return items;
	}
	
	public String getSalesmanName() {
		return salesmanName;
	}
}
