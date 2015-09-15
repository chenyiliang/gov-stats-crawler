package com.github.cyl.crawler.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AnalysisCrawler {
	private String analysisUrl;

	private static final Pattern YEAR_MONTH_EX = Pattern.compile("(\\d{4})年(\\d{1,2})月");
	private static final Pattern CN_CHAR = Pattern.compile("[\u4e00-\u9fa5]");
	private static final Pattern UNVALID_CHAR = Pattern
			.compile("[^\u4e00-\u9fa5|\\w+|%|,|，|.|。|;|；|:|：|、|(|（|)|）|+|-]");

	public AnalysisCrawler(String analysisUrl) {
		this.analysisUrl = analysisUrl;
	}

	public List<Object> parseAnalysisData() {
		try {
			Document document = Jsoup.connect(analysisUrl)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
					.get();
			return parseAnalysisDoc(document);
		} catch (IOException e) {
			throw new RuntimeException("解析分析报告网址失败", e);
		}
	}

	private List<Object> parseAnalysisDoc(Document doc) {
		List<Object> list = new ArrayList<Object>();
		String title = doc.title();
		int[] yearMonth = parseYearMonthFromTitle(title);
		list.add(yearMonth[0]);
		list.add(yearMonth[1]);
		list.add(title);
		Element center = doc.select(".center").first();
		StringTokenizer tokenizer = new StringTokenizer(center.text());
		boolean begin = false;
		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();
			if (!begin && line.contains("。")) {
				begin = true;
			}
			if (begin && !line.isEmpty() && line.length() > 4 && (CN_CHAR.matcher(line).find())) {
				list.add(UNVALID_CHAR.matcher(line).replaceAll(""));
			}
		}
		return list;
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
