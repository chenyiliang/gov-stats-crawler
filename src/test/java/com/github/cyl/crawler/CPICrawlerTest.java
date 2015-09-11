package com.github.cyl.crawler;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class CPICrawlerTest {
	@Test
	public void test() throws IOException {
		CPICrawler crawler = new CPICrawler("D:/yicai_workspace/2014CPI.txt", "utf-8");
		Document document = Jsoup.connect("http://www.stats.gov.cn/tjsj/zxfb/201509/t20150910_1242519.html")
				.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0").get();
		Map<String, Object> map = crawler.parseCPIDoc(document);
		Set<Entry<String, Object>> set = map.entrySet();
		for (Entry<String, Object> entry : set) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}
}
