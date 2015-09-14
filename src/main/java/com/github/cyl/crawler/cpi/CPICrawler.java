package com.github.cyl.crawler.cpi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CPICrawler {
	private String cpiUrl;

	private static final Pattern YEAR_MONTH_EX = Pattern.compile("(\\d{4})年(\\d{1,2})月");
	private static final Pattern UN_CN_CHAR = Pattern.compile("[^\u4e00-\u9fa5]");
	private static final String START_MARK_WORD = "居民消费价格";
	private static final List<String> STOP_WORDS = Arrays.asList("分类别");
	private static final List<String> BEGIN_REMOVE_WORDS = Arrays.asList("其中", "一", "二", "三", "四", "五", "六", "七", "八",
			"九", "十");

	public CPICrawler(String cpiUrl) {
		this.cpiUrl = cpiUrl;
	}

	public Map<String, Object> parseCPIData() {
		try {
			Document document = Jsoup.connect(cpiUrl)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
					.get();
			return parseCPIDoc(document);
		} catch (IOException e) {
			throw new RuntimeException("解析CPI网址失败", e);
		}
	}

	private Map<String, Object> parseCPIDoc(Document doc) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		// 从标题中解析年和月
		int[] yearMonth = parseYearMonthFromTitle(doc.title());
		map.put("year", yearMonth[0]);
		map.put("month", yearMonth[1]);

		// 解析数据
		Elements tbodys = doc.getElementsByTag("tbody");
		Elements theads = doc.getElementsByTag("thead");
		if (tbodys.size() == 0 && theads.size() == 0) {
			throw new RuntimeException("该文章数据表格不符合CPI报告数据表格");
		}
		Element tbody = tbodys.first();
		Element thead = theads.first();
		Elements trs = tbody.getElementsByTag("tr");
		if (thead != null) {
			Elements trs2 = thead.getElementsByTag("tr");
			if (trs2.size() > trs.size()) {
				trs = trs2;
			}
		}

		outter: for (int i = 2; i < trs.size(); i++) {
			Element tr = trs.get(i);
			Elements tds = tr.getElementsByTag("td");

			String key = null;
			List<Double> indicator = new ArrayList<Double>();

			for (int j = 0; j < tds.size(); j++) {
				Element td = tds.get(j);
				String cell = "";
				Elements spans = td.getElementsByTag("span");
				if (j == 0) {
					Set<String> set = new LinkedHashSet<String>();
					for (int k = 0; k < spans.size(); k++) {
						set.add(spans.get(k).text().trim());
					}
					for (String str : set) {
						cell += str;
					}
				} else {
					cell = spans.first().text().trim();
				}
				if (j == 0) {
					key = UN_CN_CHAR.matcher(cell).replaceAll("");
					// System.out.println(key);
					if (STOP_WORDS.contains(key) || (i == 2 && !key.equals(START_MARK_WORD))) {
						continue outter;
					}
				} else {
					// System.out.println(i + ":" + j + ":" + cell);
					indicator.add(Double.valueOf(cell));
				}
			}
			map.put(processKey(key), indicator);
		}
		return map;
	}

	private String processKey(String key) {
		for (int i = 0; i < BEGIN_REMOVE_WORDS.size(); i++) {
			String beginRemoveWord = BEGIN_REMOVE_WORDS.get(i);
			if (key.startsWith(beginRemoveWord)) {
				return key.replaceFirst(beginRemoveWord, "");
			}
		}
		return key;
	}

	private int[] parseYearMonthFromTitle(String title) {
		Matcher matcher = YEAR_MONTH_EX.matcher(title);
		if (matcher.find()) {
			String year = matcher.group(1);
			String month = matcher.group(2);
			return new int[] { Integer.parseInt(year), Integer.parseInt(month) };
		} else {
			throw new RuntimeException("该文章标题不符合CPI报告标题");
		}
	}
}
