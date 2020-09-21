package com.github.fabiosoaza.salesimporter.infrastructure.predicate;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.fabiosoaza.salesimporter.infrastructure.record.ImportRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.validator.ImportRecordValidator;

@Component
public class IsValidRecordPredicate implements Predicate{
	
	private ImportRecordValidator validator;
	
	@Autowired
	public IsValidRecordPredicate(ImportRecordValidator validator) {
		super();
		this.validator = validator;
	}

	@Override
	public boolean matches(Exchange exchange) {
		ImportRecord record = exchange.getMessage().getBody(ImportRecord.class);
		return validator.validate(record);
	}

}
