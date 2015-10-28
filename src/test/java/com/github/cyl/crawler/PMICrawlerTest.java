package com.github.cyl.crawler;

import java.util.Map;

import org.junit.Test;

import com.github.cyl.crawler.pmi.PMIAnalysisCrawler;

public class PMICrawlerTest {

	@Test
	public void test1() {
		PMIAnalysisCrawler crawler = new PMIAnalysisCrawler(
				"http://www.stats.gov.cn/tjsj/sjjd/201410/t20141001_617720.html");
		Map<String, Object> data = crawler.parsePMIGovData();
		System.out.println(data);
	}

}
