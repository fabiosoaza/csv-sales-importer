package com.github.fabiosoaza.salesimporter.infrastructure.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.fabiosoaza.salesimporter.infrastructure.exception.ParseRecordException;
import com.github.fabiosoaza.salesimporter.infrastructure.parser.SaleItemsParser;
import com.github.fabiosoaza.salesimporter.infrastructure.record.ImportRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SaleRecord;

@Component
public class ImportRecordValidator {
		
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportRecordValidator.class);

	private SaleItemsParser parser;
		
	@Autowired
	public ImportRecordValidator(SaleItemsParser parser) {
		super();
		this.parser = parser;
	}

	public boolean validate(ImportRecord record) {
		if (record == null) {
			return false;
		}		
		if( record instanceof SaleRecord) {
			SaleRecord sale = (SaleRecord) record;
			try {
				parser.parse(sale.getItems());
				return true;
			}
			catch(ParseRecordException ex) {
				LOGGER.error("Error parsing record", ex);
				return false;
			}
			
		}
		return true;
	}

	
}
