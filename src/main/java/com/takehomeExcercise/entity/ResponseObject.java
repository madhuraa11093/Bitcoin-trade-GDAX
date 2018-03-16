package com.takehomeExcercise.entity;

import java.math.BigDecimal;
import java.util.List;

public class ResponseObject {
	private BigDecimal sequence;
	private List<List<BigDecimal>> bids;
	private List<List<BigDecimal>> asks;
	public BigDecimal getSequence() {
		return sequence;
	}
	public void setSequence(BigDecimal sequence) {
		this.sequence = sequence;
	}
	public List<List<BigDecimal>> getBids() {
		return bids;
	}
	public void setBids(List<List<BigDecimal>> bids) {
		this.bids = bids;
	}
	public List<List<BigDecimal>> getAsks() {
		return asks;
	}
	public void setAsks(List<List<BigDecimal>> asks) {
		this.asks = asks;
	}
	
	}
