package com.github.cyl.crawler.fs;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FinancialStatisticCrawler {
	private String fsUrl;

	public FinancialStatisticCrawler(String fsUrl) {
		this.fsUrl = fsUrl;
	}

	public Map<String, Object> parseFSData() {
		try {
			Document document = Jsoup.connect(fsUrl)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
					.get();
			return parseFSDoc(document);
		} catch (IOException e) {
			throw new RuntimeException("解析CPI网址失败", e);
		}
	}

	private Map<String, Object> parseFSDoc(Document doc) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		// 解析标题
		String title = doc.title();
		map.put("newsTitle", title);
		// 解析发布时间
		String pubDateStr = doc.getElementById("shijian").text();
		Date pubDate = parseDateStr(pubDateStr);
		map.put("pubDate", pubDate);
		// 解析数据日期
		int year = pubDate.getYear() + 1900;
		int month = pubDate.getMonth() + 1;
		if (month < 12) {
			month--;
		} else {
			year--;
			month = 1;
		}
		map.put("year", year);
		map.put("month", month);
		// 解析内容列表
		List<String> contentList = new ArrayList<String>();
		Element zoom = doc.getElementById("zoom");
		Elements paragraphs = zoom.getElementsByTag("p");
		for (int i = 0; i < paragraphs.size(); i++) {
			Element p = paragraphs.get(i);
			contentList.add(p.text());
		}
		map.put("contents", contentList);
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
