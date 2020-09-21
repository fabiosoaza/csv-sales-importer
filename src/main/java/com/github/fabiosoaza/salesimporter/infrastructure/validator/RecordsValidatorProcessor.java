package com.github.fabiosoaza.salesimporter.infrastructure.validator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.fabiosoaza.salesimporter.infrastructure.exception.ParseRecordException;
import com.github.fabiosoaza.salesimporter.infrastructure.parser.SaleItemsParser;
import com.github.fabiosoaza.salesimporter.infrastructure.record.ImportRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SaleRecord;

@Component
public class RecordsValidatorProcessor implements Processor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RecordsValidatorProcessor.class);

	private SaleItemsParser parser;
	
	@Autowired
	public RecordsValidatorProcessor(SaleItemsParser parser) {
		super();
		this.parser = parser;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		List<Object> list = Optional.ofNullable(exchange.getMessage().getBody(List.class)).orElse(Collections.emptyList());
		List<ImportRecord> records = list
		.stream()
		.filter(record -> isValidImportRecord(record, exchange))	
		.map(item->(ImportRecord)item)
		.collect(Collectors.toList());		
		exchange.getMessage().setBody(records);
	}
	
	private boolean isValidImportRecord(Object object, Exchange exchange) {
		return validate(object);
	}

	private boolean validate(Object object) {
		if( !(object instanceof ImportRecord)) {
			return false;
		}
		if( object instanceof SaleRecord) {
			SaleRecord sale = (SaleRecord) object;
			try {
				parser.parse(sale.getItems());
				return true;
			}
			catch(ParseRecordException ex) {
				LOGGER.error("Error parsing record "+sale.getItems(), ex);
				return false;
			}
			
		}
		
		return true;
	}

}
