package com.github.cyl.crawler.omo;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;

public class OMORawCrawler {
	private String omoUrl;
	private WebDriver webDriver;

	private static final long WAIT_LOAD_BASE_TIME = 1000;
	private static final int WAIT_LOAD_RANDOM_TIME = 3000;
	private static final Pattern DATE_PAT = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");

	public OMORawCrawler(String omoUrl, WebDriver webDriver) {
		this.omoUrl = omoUrl;
		this.webDriver = webDriver;
	}

	public Map<String, Object> parseOMOData() {
		try {
			Random random = new Random(System.currentTimeMillis());
			// 要抓取的网页
			webDriver.get(omoUrl);
			// 等待页面动态加载完毕
			Thread.sleep(WAIT_LOAD_BASE_TIME + random.nextInt(WAIT_LOAD_RANDOM_TIME));

			return parseOMODoc(Jsoup.parse(webDriver.getPageSource()));
		} catch (Exception e) {
			throw new RuntimeException("解析CPI网址失败", e);
		}
	}

	private Map<String, Object> parseOMODoc(Document doc) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		// 解析标题
		map.put("title", doc.title());

		// 解析年、月、日
		int[] dateArray = parseDateFromStr(doc.getElementById("shijian").text());
		map.put("year", dateArray[0]);
		map.put("month", dateArray[1]);
		map.put("day", dateArray[2]);
		map.put("pubDate", new Date(dateArray[0] - 1900, dateArray[1] - 1, dateArray[2]));
		map.put("doc", doc.toString());

		return map;
	}

	private int[] parseDateFromStr(String str) {
		Matcher matcher = DATE_PAT.matcher(str);
		if (matcher.find()) {
			String year = matcher.group(1);
			String month = matcher.group(2);
			String day = matcher.group(3);
			return new int[] { Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day) };
		} else {
			throw new RuntimeException("Parse Date Exception");
		}
	}
}
