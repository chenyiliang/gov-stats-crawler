package com.github.cyl.crawler;

import java.text.SimpleDateFormat;
import java.util.Map;

import org.junit.Test;

import com.github.cyl.crawler.residence.ResidentialPriceChangesCrawler;

public class ResidentialPriceAnalysisCrawlerTest {

	@Test
	public void test() {
		Map<String, Object> map = new ResidentialPriceChangesCrawler(
				"http://www.stats.gov.cn/tjsj/sjjd/201509/t20150918_1246626.html").parseRPCData();
		System.out.println(map);
	}

}
