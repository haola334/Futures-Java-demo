package com.huobi.common.demo;

import java.math.BigDecimal;

/**
 * TODO completion javadoc.
 *
 * @author xiao.liang
 * @since 26 August 2019
 */
public class CalculateTradeMain {

	public static void main(String[] args) {

		BigDecimal totalMoney = new BigDecimal("1000");

		BigDecimal tradeMoney = new BigDecimal("300");
		BigDecimal beishu = new BigDecimal("20"); //杠杆倍数
		BigDecimal buyPrice = new BigDecimal("3.603"); //开仓价

		boolean duo = false; //做多还是做空

		BigDecimal forceSellPercent = new BigDecimal("0.003"); //止损百分比
		BigDecimal profitPercent = new BigDecimal("0.006"); //止盈百分比


		BigDecimal buyNum = tradeMoney.divide(buyPrice, 0, BigDecimal.ROUND_HALF_EVEN);


		BigDecimal forceSellPrice = buyPrice.multiply(new BigDecimal("1").subtract(forceSellPercent));

		if (!duo) {
			forceSellPrice = buyPrice.multiply(new BigDecimal("1").add(forceSellPercent));
		}

		BigDecimal profitSellPrice = buyPrice.multiply(new BigDecimal("1").add(profitPercent));

		if (!duo) {
			profitSellPrice = buyPrice.multiply(new BigDecimal("1").subtract(profitPercent));
		}


		BigDecimal lostMoney = beishu.multiply(forceSellPercent).multiply(tradeMoney); //止损耗费
		BigDecimal profitMoney = beishu.multiply(profitPercent).multiply(tradeMoney); //止盈收入

		BigDecimal bigestLostTimes = totalMoney.subtract(tradeMoney).divide(lostMoney, 0, BigDecimal.ROUND_HALF_EVEN);

		System.out.println("金钱总数:" + totalMoney );
		System.out.println("投入本金：" + tradeMoney);
		System.out.println("杠杆倍数：" + beishu);
		System.out.printf("是否做多：" + duo);

		System.out.println("开仓价格：" + buyPrice);
		System.out.println("开仓张数：" + buyNum);

		System.out.println("止损价" + forceSellPrice);
		System.out.println("止盈价" + profitSellPrice);

		System.out.println("预计止损耗费：" + lostMoney);
		System.out.println("预计止盈收入：" + profitMoney);


		System.out.println("最大可亏次数：" + bigestLostTimes);


	}

}
