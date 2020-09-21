package com.github.fabiosoaza.salesimporter.record;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.fabiosoaza.salesimporter.infrastructure.exception.ParseRecordException;
import com.github.fabiosoaza.salesimporter.infrastructure.parser.SaleItemsParser;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SaleItemRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.utils.BigDecimalUtils;

public class SalesItemsParseTest {

	private SaleItemsParser parser = new SaleItemsParser();
	
	private static String VALID_MULTIPLE_CONTENT = "[1-10-100,2-30-2.50,3-40-3.10]";
	private static String EMPTY_CONTENT = "[]";
	private static String MULTIPLE_CONTENT_WITHOUT_BRACKETS = "1-10-100,2-30-2.50,3-40-3.10";
	private static String MULTIPLE_CONTENT_WITH_ONLY_LEFT_BRACKET = "[1-10-100,2-30-2.50,3-40-3.10";
	private static String MULTIPLE_CONTENT_WITH_ONLY_RIGHT_BRACKET = "1-10-100,2-30-2.50,3-40-3.10]";
	
	private static String MULTIPLE_CONTENT_ONE_RECORD_WITHOUT_ALL_DATA = "[2-30-2.50,3-40]";
	private static String MULTIPLE_CONTENT_ONE_RECORD_THROW_EXCEPTION = "[1-21474836489-100,2-30-2.50,3-40-3.10]";
	
	
	@Test
	public void shouldParseMultipleContent() {
		List<SaleItemRecord> expected = Arrays.asList(
				new SaleItemRecord("3", 40, BigDecimalUtils.ofString("3.10")),
				new SaleItemRecord("1", 10, BigDecimalUtils.ofString("100")),
				new SaleItemRecord("2", 30, BigDecimalUtils.ofString("2.50"))
				);
		
		List<SaleItemRecord> parsed = parser.parse(VALID_MULTIPLE_CONTENT);
		assertThat(parsed).containsAll(expected);		
	}

	@Test
	public void shouldNotParseIfContentIsNotInsideBrackets() {				
		List<SaleItemRecord> parsed = parser.parse(MULTIPLE_CONTENT_WITHOUT_BRACKETS);
		assertThat(parsed).isEmpty();		
	}

	@Test
	public void shouldNotParseIfContentIsEmpty() {				
		List<SaleItemRecord> parsed = parser.parse(EMPTY_CONTENT);
		assertThat(parsed).isEmpty();		
	}
	
	@Test
	public void shouldNotParseIfContentHasOnlyLeftBracket() {				
		List<SaleItemRecord> parsed = parser.parse(MULTIPLE_CONTENT_WITH_ONLY_LEFT_BRACKET);
		assertThat(parsed).isEmpty();		
	}	

	@Test
	public void shouldNotParseIfContentHasOnlyRightBracket() {				
		List<SaleItemRecord> parsed = parser.parse(MULTIPLE_CONTENT_WITH_ONLY_RIGHT_BRACKET);
		assertThat(parsed).isEmpty();		
	}
	
	@Test
	public void shouldThrowExceptionIfOneRecordDoesNotContainAllData() {				
		assertThatExceptionOfType(ParseRecordException.class).isThrownBy(()-> parser.parse(MULTIPLE_CONTENT_ONE_RECORD_WITHOUT_ALL_DATA));		
	}
	
	@Test
	public void shouldThrowExceptionIfOneRecordParsingThrowAnxception() {				
		assertThatExceptionOfType(ParseRecordException.class).isThrownBy(()-> parser.parse(MULTIPLE_CONTENT_ONE_RECORD_THROW_EXCEPTION));		
	}
	
	
	
}
