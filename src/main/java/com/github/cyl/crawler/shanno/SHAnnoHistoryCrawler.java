package com.github.cyl.crawler.shanno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

@SuppressWarnings("unused")
public class SHAnnoHistoryCrawler {
	private static final String BASE_URL = "http://www.sse.com.cn/assortment/stock/list/stockdetails/announcement/index.shtml?COMPANY_CODE=%d&productId=%d&startDate=%s&endDate=%s&reportType=ALL&moreConditions=true";
	private static final Pattern DATE_PAT = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
	private static final Pattern COUNT_PAT = Pattern.compile("\\d+");

	private static final long waitLoadBaseTime = 2000;
	private static final int waitLoadRandomTime = 1000;

	private int code;
	private String startDateStr;
	private String endDateStr;
	private String indexUrl;

	public SHAnnoHistoryCrawler(int code, String startDateStr, String endDateStr) {
		if (code >= 600000 && code <= 600400) {
			this.code = code;
		} else {
			throw new RuntimeException("The code " + code + " is not in correct range !");
		}
		if (DATE_PAT.matcher(startDateStr).matches()) {
			this.startDateStr = startDateStr;
		} else {
			throw new RuntimeException("The startDateStr format is not correct ");
		}
		if (DATE_PAT.matcher(endDateStr).matches()) {
			this.endDateStr = endDateStr;
		} else {
			throw new RuntimeException("The startDateStr format is not correct ");
		}
		if (Integer.parseInt(startDateStr.replaceAll("-", "")) > Integer.parseInt(endDateStr.replaceAll("-", ""))) {
			throw new RuntimeException("The startDateStr should be not bigger than endDateStr !");
		}
		this.indexUrl = String.format(BASE_URL, code, code, startDateStr, endDateStr);
	}

	public List<Map<String, Object>> parseAnnoUrls() {
		// 存储数据
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Random random = new Random(System.currentTimeMillis());
		WebDriver driver = new FirefoxDriver();
		driver.get(indexUrl);
		try {
			Thread.sleep(waitLoadBaseTime + random.nextInt(waitLoadRandomTime));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		do {
			Document doc = Jsoup.parse(driver.getPageSource());
			List<Map<String, Object>> curList = extractFromCurDoc(doc);
			list.addAll(curList);
		} while (toNextPage(driver));

		driver.close();

		return list;
	}

	private List<Map<String, Object>> extractFromCurDoc(Document doc) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Element table = doc.getElementById("announcementDiv_container");
		if (table != null) {
			Elements dataTrs = table.getElementsByClass("data");
			for (int i = 0; i < dataTrs.size(); i++) {
				Element dataTr = dataTrs.get(i);
				Elements tds = dataTr.getElementsByTag("td");
				HashMap<String, Object> map = new HashMap<String, Object>();
				Element a = tds.get(0).getElementsByTag("a").first();
				map.put("title", a.html());
				map.put("originUrl", a.attr("href"));
				map.put("code", Integer.parseInt(tds.get(1).text()));
				map.put("type", tds.get(2).text());
				String pubDateStr = tds.get(4).text();
				Matcher pubDatePat = DATE_PAT.matcher(pubDateStr);
				if (pubDatePat.find()) {
					map.put("year", Integer.parseInt(pubDatePat.group(1)));
					map.put("month", Integer.parseInt(pubDatePat.group(2)));
					map.put("day", Integer.parseInt(pubDatePat.group(3)));
				}
				list.add(map);
			}
		}
		return list;
	}

	private boolean toNextPage(WebDriver driver) {
		List<Integer> list = new ArrayList<Integer>();
		String rowCountStr = driver.findElement(By.id("deadling")).getText();
		Matcher countMat = COUNT_PAT.matcher(rowCountStr);
		while (countMat.find()) {
			list.add(Integer.parseInt(countMat.group()));
		}
		if (list != null && list.size() > 0 && list.get(0) > 10 && list.get(0) > list.get(list.size() - 1)) {
			driver.findElement(By.className("paging_next")).click();
			try {
				Thread.sleep(waitLoadBaseTime + new Random(System.currentTimeMillis()).nextInt(waitLoadRandomTime));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		List<Map<String, Object>> list = new SHAnnoHistoryCrawler(600219, "2015-11-10", "2015-11-10").parseAnnoUrls();
		System.out.println(list.size());
	}
}
