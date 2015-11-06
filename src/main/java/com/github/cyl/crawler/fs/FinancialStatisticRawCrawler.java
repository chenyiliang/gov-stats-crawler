package com.github.cyl.crawler.fs;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FinancialStatisticRawCrawler {
	private String fsUrl;

	public FinancialStatisticRawCrawler(String fsUrl) {
		this.fsUrl = fsUrl;
	}

	public Map<String, Object> parseFSData() {
		try {
			Document document = Jsoup.connect(fsUrl).header("User-Agent", "spider").get();
			return parseFSDoc(document);
		} catch (IOException e) {
			throw new RuntimeException("解析CPI网址失败", e);
		}
	}

	private Map<String, Object> parseFSDoc(Document doc) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		// 解析标题
		String title = doc.title();
		map.put("title", title);
		// 解析发布时间
		String pubDateStr = doc.getElementById("shijian").text();
		Date pubDate = parseDateStr(pubDateStr);
		map.put("pubDate", pubDate);
		// 解析数据日期
		int year = pubDate.getYear() + 1900;
		int month = pubDate.getMonth() + 1;
		if (month != 1) {
			month--;
		} else {
			year--;
			month = 12;
		}
		map.put("year", year);
		map.put("month", month);
		map.put("originUrl", fsUrl);
		// 整个网页
		map.put("doc", doc.toString());
		return map;
	}

	private Date parseDateStr(String dateStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
