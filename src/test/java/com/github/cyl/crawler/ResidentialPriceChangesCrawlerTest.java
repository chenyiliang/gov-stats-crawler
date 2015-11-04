package com.github.cyl.crawler;

import java.util.Map;

import org.junit.Test;

import com.github.cyl.crawler.residence.ResidentialPriceChangesCrawler;

public class ResidentialPriceChangesCrawlerTest {

	@Test
	public void test() {
		Map<String, Object> map = new ResidentialPriceChangesCrawler(
				"http://www.stats.gov.cn/tjsj/zxfb/201509/t20150918_1246629.html").parseRPCData();
		System.out.println(map);
	}

}
