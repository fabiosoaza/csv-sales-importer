package com.github.fabiosoaza.salesimporter.infrastructure.record;

import java.math.BigDecimal;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import com.github.fabiosoaza.salesimporter.infrastructure.utils.ParseUtils;

@CsvRecord(separator = ParseUtils.RECORD_SEPARATOR)
public class SellerRecord implements ImportRecord{
	
	@DataField(pos = 1, required = true)
	private String recordType;
	
	@DataField(pos = 2, required = true)
	private String cpf;
	
	@DataField(pos = 3, required = true)
	private String name;
	
	@DataField(pos = 4, required = true, precision = 2)
	private BigDecimal salary;

	public SellerRecord() {
	}
	
	public SellerRecord(String recordType, String cpf, String name, BigDecimal salary) {
		super();
		this.recordType = recordType;
		this.cpf = cpf;
		this.name = name;
		this.salary = salary;
	}

	public String getRecordType() {
		return recordType;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public String getName() {
		return name;
	}
	
	public BigDecimal getSalary() {
		return salary;
	}
	
}
