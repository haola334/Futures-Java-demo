package com.huobi.common.demo.bean;

import java.util.List;

/**
 * TODO completion javadoc.
 *
 * @author xiao.liang
 * @since 23 August 2019
 */
public class RecentTradeResponse {

	private String ch;

	private List<Data> data;

	private String status;
	private long ts;

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public static class Data {
		private long id;
		private long ts;
		private List<RecentTrade> data;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public long getTs() {
			return ts;
		}

		public void setTs(long ts) {
			this.ts = ts;
		}

		public List<RecentTrade> getData() {
			return data;
		}

		public void setData(List<RecentTrade> data) {
			this.data = data;
		}
	}

}
