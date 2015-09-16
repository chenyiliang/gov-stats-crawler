package com.github.cyl.crawler;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import com.github.cyl.crawler.ppi.PPICrawler;

public class PPICrawlerTest {
	@Test
	public void testPPICrawler() {
		PPICrawler ppiCrawler = new PPICrawler("http://www.stats.gov.cn/tjsj/zxfb/201509/t20150910_1242528.html");
		Map<String, Object> PPIMap = ppiCrawler.parsePPIData();
		Set<Entry<String, Object>> entrySet = PPIMap.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}
}
