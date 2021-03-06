package com.github.cyl.crawler.pmi;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PMIWuliuCrawler {
	private String pmiUrl;

	private static final Pattern YEAR_MONTH_PAT = Pattern.compile("(\\d{4})年(\\d{1,2})月");
	private static final Pattern PUB_DATE_PAT = Pattern.compile("\\d{4}年\\d{2}月\\d{2}日\\d{2}:\\d{2}");

	public PMIWuliuCrawler(String pmiUrl) {
		this.pmiUrl = pmiUrl;
	}

	public Map<String, Object> parsePMIWuliuData() {
		try {
			Document document = Jsoup.connect(pmiUrl)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
					.get();
			return parsePMIWuliuDoc(document);
		} catch (IOException e) {
			throw new RuntimeException("Connet Url Exception", e);
		}
	}

	private Map<String, Object> parsePMIWuliuDoc(Document doc) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		// 解析标题
		String title = doc.title();
		map.put("title", title);
		// 解析发布时间
		Date pubDate = parsePubDate(doc.getElementsByTag("dd").first().text());
		map.put("pubDate", pubDate);
		// 解析数据日期
		int[] yearMonth = parseYearMonth(doc.getElementById("mainContent").text());
		map.put("year", yearMonth[0]);
		map.put("month", yearMonth[1]);
		// 整个文档保存
		map.put("doc", doc.toString());
		return map;
	}

	private int[] parseYearMonth(String str) {
		Matcher matcher = YEAR_MONTH_PAT.matcher(str);
		if (matcher.find()) {
			String year = matcher.group(1);
			String month = matcher.group(2);
			return new int[] { Integer.parseInt(year), Integer.parseInt(month) };
		} else {
			throw new RuntimeException("Title Regex Pattern Exception");
		}
	}

	private Date parsePubDate(String str) {
		Matcher matcher = PUB_DATE_PAT.matcher(str);
		if (matcher.find()) {
			String dateStr = matcher.group();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
			try {
				Date date = dateFormat.parse(dateStr);
				return date;
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}
}
