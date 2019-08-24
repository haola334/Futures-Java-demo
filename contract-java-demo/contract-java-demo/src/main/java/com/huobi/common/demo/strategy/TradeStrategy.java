package com.huobi.common.demo.strategy;

import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.huobi.common.demo.bean.RecentTrade;
import com.huobi.common.demo.client.HbdmClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO completion javadoc.
 *
 * @author xiao.liang
 * @since 23 August 2019
 */
public class TradeStrategy {

	private final Logger logger = LoggerFactory.getLogger(TradeStrategy.class);

	private final HbdmClient hbdmClient = new HbdmClient();


	private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	private volatile BigDecimal totalMoney = new BigDecimal("100000");

	private BigDecimal tradeMoney = new BigDecimal("1000");

	private AtomicBoolean trading = new AtomicBoolean(false);

	private volatile BigDecimal buyPrice = null;

	private volatile TradeType tradeType;

	private volatile boolean lastProfit = false; //上次是否盈利

	private volatile TradeType lastTradeType = TradeType.kong;

	private AtomicLong id = new AtomicLong(0);


	public void start() {
		ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					trade();
				}
				catch (Throwable throwable) {
					logger.error("trade error!", throwable);
				}
			}
		}, 0, 1000, TimeUnit.MILLISECONDS);

		while (!future.isDone()) {

		}
	}

	private void trade() {
		if (!trading.get()) {
			if (trading.compareAndSet(false, true)) {
				buy();
				return;

			}
		}

		BigDecimal sellPrice = getSellPrice();

		if (sellPrice != null) {
			sell(sellPrice);
			trading.set(false);
		}

	}

	private BigDecimal getSellPrice() {
		RecentTrade recentTrade = hbdmClient.getRecentTrade();

		while (recentTrade == null) {
			recentTrade = hbdmClient.getRecentTrade();
		}

		BigDecimal currentPrice = recentTrade.getPrice();

		BigDecimal forceSellPercent = new BigDecimal("0.001");
		BigDecimal profitPercent = new BigDecimal("0.002");
		if (tradeType == TradeType.duo) {
			if (currentPrice.compareTo(buyPrice) < 0) {
				BigDecimal delta = buyPrice.subtract(currentPrice);
				//止损
				if (delta.divide(buyPrice,4, BigDecimal.ROUND_HALF_EVEN).compareTo(forceSellPercent) >= 0) {
					logger.info("交易id：{}, 当前价格为： {}, 交易类型为: {}, 触发止损！", id.get(), currentPrice, tradeType.getText());
					return new BigDecimal("-10");
				}
			}
			else if (currentPrice.compareTo(buyPrice) > 0) {
				BigDecimal delta = currentPrice.subtract(buyPrice);
				//止盈
				if (delta.divide(buyPrice,4, BigDecimal.ROUND_HALF_EVEN).compareTo(profitPercent) >= 0) {
					logger.info("交易id：{}, 当前价格为： {}, 交易类型为: {}, 触发止盈！", id.get(), currentPrice, tradeType.getText());
					return new BigDecimal("20");
				}
			}


		} else {
			if (currentPrice.compareTo(buyPrice) > 0) {
				BigDecimal delta = currentPrice.subtract(buyPrice);
				//止损
				if (delta.divide(buyPrice,4, BigDecimal.ROUND_HALF_EVEN).compareTo(forceSellPercent) >= 0) {
					logger.info("交易id：{}, 当前价格为： {}, 交易类型为: {}, 触发止损！", id.get(), currentPrice, tradeType.getText());
					return new BigDecimal("-10");
				}
			}
			else if (currentPrice.compareTo(buyPrice) < 0) {
				BigDecimal delta = buyPrice.subtract(currentPrice);
				//止盈
				if (delta.divide(buyPrice,4, BigDecimal.ROUND_HALF_EVEN).compareTo(profitPercent) >= 0) {
					logger.info("交易id：{}, 当前价格为： {}, 交易类型为: {}, 触发止盈！", id.get(), currentPrice, tradeType.getText());
					return new BigDecimal("20");
				}
			}
		}

		logger.info("交易id：{}, 当前价格为： {}, 交易类型为: {}, 没达到触发卖出阈值，继续轮询..", id.get(), currentPrice, tradeType.getText());

		return null;
	}



	private BigDecimal buy() {
		RecentTrade recentTrade = hbdmClient.getRecentTrade();

		while (recentTrade == null) {
			recentTrade = hbdmClient.getRecentTrade();
		}

		long currentId = id.incrementAndGet();

		int code = (int)(System.currentTimeMillis() % 2);

		if (!lastProfit) {
			tradeType = (lastTradeType == TradeType.duo ? TradeType.kong : TradeType.duo);
			logger.info("交易id：{}, 上次是否盈利：{}, 本次交易类型为：{}", id.get(), lastProfit, tradeType);

			lastTradeType = tradeType;
		} else {
			logger.info("交易id：{}, 上次是否盈利：{}, 本次交易类型为：{}", id.get(), lastProfit, tradeType);
		}

		buyPrice = recentTrade.getPrice();


		logger.info("开始买入，交易id: {}, 交易类型为： {},  成交价格： {}", currentId, tradeType.getText(), recentTrade.getPrice());

		return recentTrade.getPrice();
	}


	private void sell(BigDecimal delta) {

		lastProfit = delta.compareTo(new BigDecimal(0)) > 0;

		totalMoney = totalMoney.add(delta);
		logger.info("开始卖出，交易id：{}, 收益：{}, 总本金剩余： {}", id.get(), delta, totalMoney);
	}


	public static void main(String[] args) {
		BigDecimal a = new BigDecimal("0.004");

		System.out.println(0.004/3.734);

		System.out.println(a.divide(new BigDecimal("3.734"),4, BigDecimal.ROUND_HALF_EVEN));
	}

	private static enum TradeType{
		kong(0, "做空"),
		duo(1, "做多")
		;

		private final int code;
		private final String text;


		TradeType(int code, String text) {
			this.code = code;
			this.text = text;
		}

		public static TradeType fromCode(int code) {
			for (TradeType tradeType : values()) {
				if (code == tradeType.code) {
					return tradeType;
				}
			}
			throw new RuntimeException("invalid code: " + code);
		}

		public int getCode() {
			return code;
		}

		public String getText() {
			return text;
		}
	}

}
