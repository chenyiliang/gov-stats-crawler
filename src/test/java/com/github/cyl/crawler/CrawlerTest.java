package com.github.cyl.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class CrawlerTest {

	@Test
	public void testCrawler() throws Exception {
		Document doc = Jsoup.connect("http://www.stats.gov.cn/tjsj/zxfb/201508/t20150809_1227697.html").get();
		String title = doc.title();
		System.out.println(title);
		Elements tbodys = doc.getElementsByTag("tbody");
		if (tbodys.size() == 0) {
			return;
		}

		Element tbody = tbodys.first();
		Elements trs = tbody.getElementsByTag("tr");
		for (int i = 3; i < trs.size(); i++) {
			Element tr = trs.get(i);
			Elements tds = tr.getElementsByTag("td");
			for (int j = 0; j < tds.size(); j++) {
				Element td = tds.get(j);
				String text = td.getElementsByTag("span").first().text().trim();
				System.out.print(text + "\t");
			}
			System.out.println();
		}
	}
}
