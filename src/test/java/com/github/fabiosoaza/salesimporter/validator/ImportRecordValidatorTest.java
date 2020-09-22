package com.github.fabiosoaza.salesimporter.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import com.github.fabiosoaza.salesimporter.infrastructure.exception.ParseRecordException;
import com.github.fabiosoaza.salesimporter.infrastructure.parser.SaleItemsParser;
import com.github.fabiosoaza.salesimporter.infrastructure.record.ClientRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SaleRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SellerRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.validator.ImportRecordValidator;

@ExtendWith(MockitoExtension.class)
public class ImportRecordValidatorTest {

	@InjectMocks
	private ImportRecordValidator importRecordValidator;
	
	@Mock
	private SaleItemsParser saleItemsParser;
	
	@Test
	public void shouReturnFalseIfNull() {
		assertThat(importRecordValidator.validate(null)).isFalse();
	}
	
	@Test
	public void shouReturnTrueIfClientRecord() {
		assertThat(importRecordValidator.validate(new ClientRecord())).isTrue();
	}
	
	@Test
	public void shouReturnTrueIfSellerRecord() {
		assertThat(importRecordValidator.validate(new SellerRecord())).isTrue();
	}
	
	@Test
	public void shouReturnFalseIfExceptionIsThrown() {
		when(saleItemsParser.parse(anyString())).thenThrow(new ParseRecordException("Exception"));
		assertThat(importRecordValidator.validate(new SaleRecord("","","",""))).isFalse();
	}
	
	@Test
	public void shouReturnTrueIfSaleRecordParseDoesNotThrowAnException() {
		assertThat(importRecordValidator.validate(new SaleRecord("","","",""))).isTrue();
	}
	
}
