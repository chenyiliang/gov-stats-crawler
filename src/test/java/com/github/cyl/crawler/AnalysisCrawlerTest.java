package com.github.cyl.crawler;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.github.cyl.crawler.analysis.AnalysisCrawler;

public class AnalysisCrawlerTest {
	@Test
	public void test() throws IOException {
		AnalysisCrawler analysisCrawler = new AnalysisCrawler(
				"http://www.stats.gov.cn/tjsj/sjjd/201507/t20150709_1211572.html");
		List<Object> list = analysisCrawler.parseAnalysisData();
		for (Object obj : list) {
			System.out.println(obj + "**");
		}
	}
}
