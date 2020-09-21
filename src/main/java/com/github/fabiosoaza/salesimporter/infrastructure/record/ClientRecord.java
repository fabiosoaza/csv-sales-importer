package com.github.fabiosoaza.salesimporter.infrastructure.record;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import com.github.fabiosoaza.salesimporter.infrastructure.utils.ParseUtils;

@CsvRecord(separator = ParseUtils.RECORD_SEPARATOR)
public class ClientRecord implements ImportRecord{
	
	@DataField(pos = 1, required = true)
	private String recordType;
	
	@DataField(pos = 2, required = true)
	private String cnpj;
	
	@DataField(pos = 3, required = true)
	private String name;
	
	@DataField(pos = 4, required = true)
	private String businessArea;
	
	public ClientRecord() {
	}
	
	public ClientRecord(String recordType, String cnpj, String name, String businessArea) {
		super();
		this.recordType = recordType;
		this.cnpj = cnpj;
		this.name = name;
		this.businessArea = businessArea;
	}

	public String getRecordType() {
		return recordType;
	}
	
	public String getCnpj() {
		return cnpj;
	}
	
	public String getName() {
		return name;
	}
	
	public String getBusinessArea() {
		return businessArea;
	}

}
