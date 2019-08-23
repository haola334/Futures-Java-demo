package com.huobi.common.demo.bean;

import java.math.BigDecimal;

/**
 * TODO completion javadoc.
 *
 * @author xiao.liang
 * @since 23 August 2019
 */
public class RecentTrade {

	private int amount;

	private String direction;

	private long id;

	private BigDecimal price;

	private long ts;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}
}
