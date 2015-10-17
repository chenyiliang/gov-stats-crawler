package com.github.cyl.crawler.residence;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ResidentialPriceAnalysisCrawler {
	private static final Pattern PUB_DATE_PAT = Pattern.compile("发布时间：(\\d{4}-\\d{2}-\\d{2}.{0,2}\\d{2}:\\d{2})");

	private static final Pattern UNVALID_CHAR = Pattern
			.compile("[^\u4e00-\u9fa5|\\w+|%|,|，|.|。|;|；|:|：|、|(|（|)|）|+|-]");

	private String priceUrl;

	public ResidentialPriceAnalysisCrawler(String priceUrl) {
		this.priceUrl = priceUrl;
	}

	public Map<String, Object> parseRPCData() {
		try {
			Document document = Jsoup.connect(priceUrl)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
					.get();
			return parseRPCDoc(document);
		} catch (IOException e) {
			throw new RuntimeException("解析网址失败", e);
		}
	}

	private Map<String, Object> parseRPCDoc(Document doc) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		// 解析标题
		String title = doc.title();
		map.put("newsTitle", title);
		// 解析发布时间
		String pubDateStr = doc.getElementsByTag("font").text();
		Date pubDate = parseDateStr(UNVALID_CHAR.matcher(pubDateStr).replaceAll(""));
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
		Element element = doc.getElementsByClass("TRS_Editor").first();
		element.getElementsByTag("table").remove();
		Elements paragraphs = element.getElementsByTag("p");
		for (int i = 0; i < paragraphs.size(); i++) {
			String pStr = paragraphs.get(i).text().trim();
			if (pStr.length() > 3) {
				contentList.add(UNVALID_CHAR.matcher(pStr).replaceAll(""));
			}
		}
		map.put("contents", contentList);
		return map;
	}

	private Date parseDateStr(String dateStr) {
		System.out.println(dateStr);
		Matcher matcher = PUB_DATE_PAT.matcher(dateStr);
		if (matcher.find()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm");
			try {
				return dateFormat.parse(matcher.group(1));
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("Not Find Pub Date");
		}
	}
}
