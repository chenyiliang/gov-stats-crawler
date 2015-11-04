package com.github.cyl.crawler.fcr;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FCRCrawler {
	private String fcrUrl;

	public FCRCrawler(String fcrUrl) {
		this.fcrUrl = fcrUrl;
	}

	public Map<String, Object> parseFCRData() {
		try {
			Document document = Jsoup.connect(fcrUrl)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
					.get();
			return parseFCRDoc(document);
		} catch (IOException e) {
			throw new RuntimeException("解析网址失败", e);
		}
	}

	private Map<String, Object> parseFCRDoc(Document doc) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		// 解析标题(这里标题是死的)
		map.put("title", "官方储备资产");
		String[] split = fcrUrl.split("/");
		// 解析发布时间(从url中解析)
		String pubDateStr = split[split.length - 1].substring(0, 8);
		map.put("pubDate", parseDateStr(pubDateStr));
		// 解析数据日期(从url中解析)
		int year = Integer.parseInt(split[split.length - 3]);
		int month = Integer.parseInt(split[split.length - 2]);
		if (month != 1) {
			month--;
		} else {
			year--;
			month = 12;
		}
		map.put("year", year);
		map.put("month", month);
		// 整个文档保存
		map.put("doc", doc.toString());

		return map;
	}

	private Date parseDateStr(String dateStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
