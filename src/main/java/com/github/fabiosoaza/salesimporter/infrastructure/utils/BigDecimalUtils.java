package com.github.fabiosoaza.salesimporter.infrastructure.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class BigDecimalUtils {

	private static final Integer DEFAULT_PRECISION = 2;
	
	public static BigDecimal ofString(String value) {
        return ofString(
            value,
            DEFAULT_PRECISION
        );
    }
	
	public static BigDecimal ofString(String value, Integer precision) {
        return ofBigDecimal(
            new BigDecimal(value),
            precision
        );
    }
	
	public static BigDecimal ofBigDecimal(BigDecimal value, Integer precision) {
        return value.setScale(precision, RoundingMode.DOWN);
	}
	
}
