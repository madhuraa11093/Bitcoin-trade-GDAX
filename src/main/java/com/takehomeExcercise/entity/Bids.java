package com.takehomeExcercise.entity;

import java.math.BigDecimal;

public class Bids {
	
	private BigDecimal price;
	private BigDecimal size;
	private BigDecimal numOrders;
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getSize() {
		return size;
	}
	public void setSize(BigDecimal size) {
		this.size = size;
	}
	public BigDecimal getNumOrders() {
		return numOrders;
	}
	public void setNumOrders(BigDecimal numOrders) {
		this.numOrders = numOrders;
	}
	

}
