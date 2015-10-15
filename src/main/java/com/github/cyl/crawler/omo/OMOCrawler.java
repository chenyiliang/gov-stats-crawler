package com.github.cyl.crawler.omo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

public class OMOCrawler {
	private String omoUrl;
	private WebDriver webDriver;

	private static final long WAIT_LOAD_BASE_TIME = 1000;
	private static final int WAIT_LOAD_RANDOM_TIME = 3000;
	private static final Pattern DATE_PAT = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
	private static final Pattern METHOD_PAT = Pattern.compile("以(.+)方式");
	private static final Pattern INT_TEXT_PAT = Pattern.compile("(\\d+)([\u4e00-\u9fa5]+)");
	private static final Pattern DOUBLE_PAT = Pattern.compile("\\d+(\\.\\d+)?");

	public OMOCrawler(String omoUrl, WebDriver webDriver) {
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
		map.put("newsTitle", doc.title());

		// 解析年、月、日
		int[] dateArray = parseDateFromStr(doc.getElementById("shijian").text());
		map.put("year", dateArray[0]);
		map.put("month", dateArray[1]);
		map.put("day", dateArray[2]);

		// 解析方法
		Element content = doc.getElementsByClass("content").first();
		String text = content.text();
		Matcher methodMatcher = METHOD_PAT.matcher(text);
		if (methodMatcher.find()) {
			map.put("method", methodMatcher.group(1));
		}

		Element table = content.getElementsByTag("table").first();
		Element tr = table.getElementsByTag("tr").get(1);
		Elements tds = tr.getElementsByTag("td");

		// 解析期限
		Matcher timeLimitMatcher = INT_TEXT_PAT.matcher(tds.get(0).text());
		if (timeLimitMatcher.find()) {
			map.put("timeLimitQuantity", Integer.valueOf(timeLimitMatcher.group(1)));
			map.put("timeLimitUnit", timeLimitMatcher.group(2));
		}
		// 解析交易量
		Matcher tradingVolumeMatcher = INT_TEXT_PAT.matcher(tds.get(1).text());
		if (tradingVolumeMatcher.find()) {
			map.put("tradingVolumeQuantity", Integer.valueOf(tradingVolumeMatcher.group(1)));
			map.put("tradingVolumeUnit", tradingVolumeMatcher.group(2));
		}
		// 解析中标利率
		Matcher bidRateMatcher = DOUBLE_PAT.matcher(tds.get(2).text());
		if (bidRateMatcher.find()) {
			map.put("bidRate", Double.valueOf(bidRateMatcher.group(0)));
		}

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
