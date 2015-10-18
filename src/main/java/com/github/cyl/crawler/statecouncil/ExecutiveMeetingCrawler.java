package com.github.cyl.crawler.statecouncil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExecutiveMeetingCrawler {
	private static final Pattern PUB_DATE_PAT = Pattern.compile("\\d{4}-\\d{2}-\\d{2}.{0,2}\\d{2}:\\d{2}");

	private static final Pattern UNVALID_CHAR = Pattern
			.compile("[^\u4e00-\u9fa5|\\w+|%|,|，|.|。|;|；|:|：|、|(|（|)|）|+|-]");

	private String meetingUrl;

	public ExecutiveMeetingCrawler(String meetingUrl) {
		this.meetingUrl = meetingUrl;
	}

	public Map<String, Object> parseExeMeetData() {
		try {
			Document document = Jsoup.connect(meetingUrl)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
					.get();
			return parseExeMeetDoc(document);
		} catch (IOException e) {
			throw new RuntimeException("解析网址失败", e);
		}
	}

	private Map<String, Object> parseExeMeetDoc(Document doc) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		// 解析发布时间
		String pubDateStr = doc.getElementsByClass("pages-date").first().text();
		Date pubDate = parseDateStr(UNVALID_CHAR.matcher(pubDateStr).replaceAll(""));
		map.put("pubDate", pubDate);
		// 解析数据日期
		int year = pubDate.getYear() + 1900;
		int month = pubDate.getMonth() + 1;
		int day = pubDate.getDate();
		map.put("year", year);
		map.put("month", month);
		map.put("day", day);
		// 解析内容列表
		List<String> contentList = new ArrayList<String>();
		Element content = doc.getElementsByClass("pages_content").first();
		Elements paragraphs = content.getElementsByTag("p");
		for (Element p : paragraphs) {
			String pRaw = p.text();
			StringTokenizer tokenizer = new StringTokenizer(pRaw);
			while (tokenizer.hasMoreElements()) {
				contentList.add(UNVALID_CHAR.matcher(tokenizer.nextElement().toString()).replaceAll(""));
			}
		}
		map.put("contents", contentList);
		// 解析标题
		String title = contentList.remove(0);
		map.put("newsTitle", title);
		return map;
	}

	private Date parseDateStr(String dateStr) {
		System.out.println(dateStr);
		Matcher matcher = PUB_DATE_PAT.matcher(dateStr);
		if (matcher.find()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm");
			try {
				return dateFormat.parse(matcher.group(0));
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("Not Find Pub Date");
		}
	}
}
