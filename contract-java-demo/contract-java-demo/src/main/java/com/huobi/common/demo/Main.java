package com.huobi.common.demo;

import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.huobi.common.demo.bean.RecentTrade;
import com.huobi.common.demo.client.HbdmClient;
import com.huobi.common.demo.strategy.TradeStrategy;

/**
 * TODO completion javadoc.
 *
 * @author xiao.liang
 * @since 23 August 2019
 */
public class Main {

	public static void main(String[] args) throws InterruptedException {


		new TradeStrategy().start();





	}




}
