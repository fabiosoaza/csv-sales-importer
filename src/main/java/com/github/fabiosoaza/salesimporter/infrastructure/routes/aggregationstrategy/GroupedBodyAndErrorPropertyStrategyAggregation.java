package com.github.fabiosoaza.salesimporter.infrastructure.routes.aggregationstrategy;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;

import com.github.fabiosoaza.salesimporter.infrastructure.utils.ParseErrorsUtils;

public class GroupedBodyAndErrorPropertyStrategyAggregation extends GroupedBodyAggregationStrategy{

	@Override
	public Exchange aggregate(Exchange exchangeAcumulator, Exchange newExchange) {
		Exchange aggregated = super.aggregate(exchangeAcumulator, newExchange);
		List<String> errors = ParseErrorsUtils.extractExchangeErrors(newExchange);
		ParseErrorsUtils.addErrorsToExchange(aggregated, errors);
		return aggregated;
	}
	
}
