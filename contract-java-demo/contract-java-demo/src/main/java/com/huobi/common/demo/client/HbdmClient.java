package com.huobi.common.demo.client;

import java.io.IOException;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.huobi.common.api.HbdmRestApiV1;
import com.huobi.common.api.IHbdmRestApi;
import com.huobi.common.demo.bean.RecentTrade;
import com.huobi.common.demo.bean.RecentTradeResponse;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO completion javadoc.
 *
 * @author xiao.liang
 * @since 23 August 2019
 */
public class HbdmClient {

	private final Logger logger = LoggerFactory.getLogger(HbdmClient.class);

	private final String API_KEY = "323f8ad0-31d45677-4cbc018c-mjlpdje3ld"; // huobi申请的apiKey,API调试过程中有问题或者有疑问可反馈微信号shaoxiaofeng1118
	private final String SECRET_KEY = "062335a6-169fd6a2-7f68e972-e0b6f"; // huobi申请的secretKey
	private final String URL_PREX = "https://api.hbdm.com";//火币api接口地址https://api.hbdm.com
	private IHbdmRestApi hbdmRestApi = new HbdmRestApiV1(URL_PREX, API_KEY, SECRET_KEY);

	public RecentTrade getRecentTrade() {
		try {
			String response = hbdmRestApi.futureMarketDetailTrade("EOS_CQ", "1");

			RecentTradeResponse recentTradeResponse = JSON.parseObject(response, RecentTradeResponse.class);

			if (!"ok".equals(recentTradeResponse.getStatus())) {
				return null;
			}

			return recentTradeResponse.getData().get(0).getData().get(0);

		}
		catch (Exception e) {
			logger.error("get recent trace error!", e);
		}

		return null;
	}

}
