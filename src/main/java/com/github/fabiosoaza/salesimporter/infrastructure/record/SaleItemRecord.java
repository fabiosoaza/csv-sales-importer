package com.github.fabiosoaza.salesimporter.infrastructure.record;

import java.math.BigDecimal;
import java.util.Objects;

public class SaleItemRecord {

	private String itemId;
	
	private Integer quantity;
	
	private BigDecimal price;
	
	public SaleItemRecord() {
	}
	
	public SaleItemRecord(String itemId, Integer quantity, BigDecimal price) {
		super();
		this.itemId = itemId;
		this.quantity = quantity;
		this.price = price;
	}

	public String getItemId() {
		return itemId;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public BigDecimal total() {
		return price.multiply(BigDecimal.valueOf(quantity.longValue()));
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof SaleItemRecord)) {
			return false;
		}
		SaleItemRecord record = (SaleItemRecord)obj;
		return Objects.equals(itemId, record.getItemId()) 
				&&  Objects.equals(quantity, record.getQuantity())
				&& Objects.equals(price, record.getPrice());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(itemId, quantity, price);
	}
	
	
}
