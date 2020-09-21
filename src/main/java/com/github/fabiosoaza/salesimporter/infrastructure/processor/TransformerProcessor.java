package com.github.fabiosoaza.salesimporter.infrastructure.processor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.fabiosoaza.salesimporter.infrastructure.components.SalesSumarizer;
import com.github.fabiosoaza.salesimporter.infrastructure.record.ImportRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SummaryRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.validator.ImportRecordValidator;

@Component
public class TransformerProcessor implements Processor {

	private SalesSumarizer salesSumarizer;
	private ImportRecordValidator importRecordValidator;
	
	@Autowired
	public TransformerProcessor(SalesSumarizer salesSumarizer, ImportRecordValidator importRecordValidator) {
		super();
		this.salesSumarizer = salesSumarizer;
		this.importRecordValidator = importRecordValidator;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		Stream stream = Optional.ofNullable(exchange.getMessage().getBody(List.class))
				.orElse(Collections.emptyList())
				.stream();
		List<ImportRecord> list = (List<ImportRecord>) stream
				.filter(item->item instanceof ImportRecord )
				.map(record->(ImportRecord)record )
				.filter(record->importRecordValidator.validate((ImportRecord)record))
				.collect(Collectors.toList());		
		SummaryRecord summary = salesSumarizer.sum(list);
		exchange.getMessage().setBody(summary);
	}

}
