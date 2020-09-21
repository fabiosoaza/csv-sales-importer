package com.github.fabiosoaza.salesimporter.infrastructure.parser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import com.github.fabiosoaza.salesimporter.infrastructure.exception.ParseRecordException;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SaleItemRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.utils.BigDecimalUtils;
import com.github.fabiosoaza.salesimporter.infrastructure.utils.ParseUtils;

@Component
public class SaleItemsParser {

	public List<SaleItemRecord> parse(String string) throws ParseRecordException{
		String contentWithoutBrackets = extractContentInsideBrackets(string);
		Stream<String> stream = extractSalesItems(contentWithoutBrackets);
		List<SaleItemRecord> items = stream
		.map(this::parseRecord)
		.filter(Objects::nonNull)
		.collect(Collectors.toList());
		return items;
	}

	private SaleItemRecord parseRecord(String saleItem) {
		String[] pieces = StringUtils.split(saleItem,  ParseUtils.SALE_ITEM_RECORD_SEPARATOR);
		if (!checkRecord(pieces)) {
			throw new ParseRecordException("Invalid Record: "+saleItem);
		}
		try {
			String id = pieces[0];
			Integer quantity = Integer.valueOf(pieces[1]);
			BigDecimal price = BigDecimalUtils.ofString(pieces[2]);
			return new SaleItemRecord(id, quantity, price);
		}
		catch(Exception ex) {
			throw new ParseRecordException("Invalid Record: "+saleItem, ex);
		}
		
	}

	private boolean checkRecord(String[] pieces) {
		return pieces.length == 3 && StringUtils.isNotEmpty(pieces[0]) && NumberUtils.isParsable(pieces[1]) && NumberUtils.isParsable(pieces[2]);
	}
	
	private Stream<String> extractSalesItems(String contentWithoutBrackets) {
		String[] salesItems = StringUtils.split(contentWithoutBrackets, ParseUtils.SALES_ITEMS_SEPARATOR);
		Stream<String> stream = Stream.of(salesItems);
		return stream;
	}

	private String extractContentInsideBrackets(String string) {
		String content = Optional.ofNullable(string).orElse("");
		String contentWithoutBrackets = StringUtils.substringBetween(content, "[", "]");
		return Optional.ofNullable(contentWithoutBrackets).orElse("");
	}
	
}
