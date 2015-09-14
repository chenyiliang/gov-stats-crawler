package com.github.cyl.crawler;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import com.github.cyl.crawler.cpi.CPICrawler;

public class CPICrawlerTest {
	@Test
	public void test() throws IOException {
		CPICrawler crawler = new CPICrawler("http://www.stats.gov.cn/tjsj/zxfb/201509/t20150910_1242519.html");
		Map<String, Object> map = crawler.parseCPIData();
		Set<Entry<String, Object>> set = map.entrySet();
		for (Entry<String, Object> entry : set) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}
}
