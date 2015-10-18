package com.github.cyl.crawler;

import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.github.cyl.crawler.statecouncil.ExecutiveMeetingCrawler;

public class StateCouncilExecutiveMeetingCrawlerTest {
	@Test
	public void test() throws Exception {
		// Document document =
		// Jsoup.connect("http://www.gov.cn/guowuyuan/2015-09/23/content_2937440.htm")
		// .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0)
		// Gecko/20100101 Firefox/29.0").get();
		// String dateStr =
		// document.getElementsByClass("pages-date").first().text();
		// Element content =
		// document.getElementsByClass("pages_content").first();
		// Elements paragraphs = content.getElementsByTag("p");
		// for (Element p : paragraphs) {
		// String pRaw = p.text();
		// StringTokenizer tokenizer = new StringTokenizer(pRaw);
		// while (tokenizer.hasMoreElements()) {
		// System.out.println(tokenizer.nextElement());
		// }
		// }
		ExecutiveMeetingCrawler crawler = new ExecutiveMeetingCrawler(
				"http://www.gov.cn/guowuyuan/2015-10/14/content_2946877.htm");
		Map<String, Object> map = crawler.parseExeMeetData();
		System.out.println(map);
	}
}
