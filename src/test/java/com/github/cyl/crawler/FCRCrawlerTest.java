package com.github.cyl.crawler;

import java.util.Map;

import org.junit.Test;

import com.github.cyl.crawler.fcr.FCRCrawler;

public class FCRCrawlerTest {
	@Test
	public void test() {
		FCRCrawler crawler = new FCRCrawler(
				"http://www.pbc.gov.cn/diaochatongjisi/resource/cms/2015/10/2015100710200130457.htm");
		Map<String, Object> map = crawler.parseFCRData();
		System.out.println(map);
	}
}
