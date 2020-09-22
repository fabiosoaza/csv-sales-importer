package com.github.fabiosoaza.salesimporter.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.github.fabiosoaza.salesimporter.infrastructure.util.ParseUtils;

public class ParseUtilTest {
	
	@Test
	public void isSellerRecordShouldReturnTrue() {
		assertThat(ParseUtils.isSellerRecord("001ç")).isTrue();
	}
	
	@Test
	public void isSellerRecordShouldReturnFalse() {
		assertThat(ParseUtils.isSellerRecord("invalid")).isFalse();
	}
	
	@Test
	public void isClientRecordShouldReturnTrue() {
		assertThat(ParseUtils.isClientRecord("002ç")).isTrue();	
	}
	
	@Test
	public void isClientRecordShouldReturnFalse() {
		assertThat(ParseUtils.isClientRecord("invalid")).isFalse();	
	}
	
	@Test
	public void isSaleRecordShouldReturnTrue() {
		assertThat(ParseUtils.isSaleRecord("003ç")).isTrue();	
	}
	
	@Test
	public void isSaleRecordShouldReturnFalse() {
		assertThat(ParseUtils.isSaleRecord("invalid")).isFalse();	
	}

}
