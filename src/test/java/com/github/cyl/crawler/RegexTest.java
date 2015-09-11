package com.github.cyl.crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class RegexTest {

	@Test
	public void testYearMonthExtract() {
		Pattern pattern = Pattern.compile("(\\d{4})年(\\d{1,2})月");
		Matcher matcher = pattern.matcher("2015年8月份居民消费价格同比上涨2.0%");
		while (matcher.find()) {
			String year = matcher.group(1);
			String month = matcher.group(2);
			System.out.println(year);
			System.out.println(month);
		}
	}

	@Test
	public void testCharRegex() {
		String target = "　　烟　　草";
		Pattern pattern = Pattern.compile("[^\u4e00-\u9fa5]");
		Matcher matcher = pattern.matcher(target);
		String replaceAll = matcher.replaceAll("");
		System.out.println(replaceAll);
	}
}
