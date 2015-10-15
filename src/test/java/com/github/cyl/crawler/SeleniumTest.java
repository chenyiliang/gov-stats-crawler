package com.github.cyl.crawler;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumTest {
	private static final Pattern DATE_PAT = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
	private static final Pattern METHOD_PAT = Pattern.compile("以(.+)方式");
	private static final Pattern INT_TEXT_PAT = Pattern.compile("(\\d+)([\u4e00-\u9fa5]+)");
	private static final Pattern DOUBLE_PAT = Pattern.compile("\\d+(\\.\\d+)?");

	@Test
	public void testDoublePattern() {
		Matcher matcher = DOUBLE_PAT.matcher("2.35%");
		System.out.println(matcher.matches());
	}

	@Test
	public void test() throws InterruptedException {
		// 等待数据加载的时间
		// 为了防止服务器封锁，这里的时间要模拟人的行为，随机且不能太短
		long waitLoadBaseTime = 1000;
		int waitLoadRandomTime = 3000;
		Random random = new Random(System.currentTimeMillis());
		// 火狐浏览器
		WebDriver driver = new FirefoxDriver();
		// 要抓取的网页
		driver.get("http://www.pbc.gov.cn/zhengcehuobisi/125207/125213/125431/125475/2963301/index.html");
		// 等待页面动态加载完毕
		Thread.sleep(waitLoadBaseTime + random.nextInt(waitLoadRandomTime));
		Document doc = Jsoup.parse(driver.getPageSource());
		// 解析标题
		String newsTitle = doc.title();
		System.out.println("newsTitle:" + newsTitle);
		// 解析日期
		String dateStr = doc.getElementById("shijian").text();
		Matcher matcher = DATE_PAT.matcher(dateStr);
		if (matcher.find()) {
			System.out.println("year:" + matcher.group(1));
			System.out.println("month:" + matcher.group(2));
			System.out.println("day:" + matcher.group(3));
		}
		Element content = doc.getElementsByClass("content").first();
		String text = content.text();
		Matcher matcher2 = METHOD_PAT.matcher(text);
		if (matcher2.find()) {
			System.out.println("method:" + matcher2.group(1));
		}
		Element table = content.getElementsByTag("table").first();
		Element tr = table.getElementsByTag("tr").get(1);
		Elements tds = tr.getElementsByTag("td");
		System.out.println(tds.get(0).text());
		System.out.println(tds.get(1).text());
		System.out.println(tds.get(2).text());
		driver.close();
	}
}
