package com.github.cyl.crawler;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.github.cyl.crawler.omo.OMOCrawler;

public class OmoCrawlerTest {
	@Test
	public void test() throws IOException {
		WebDriver driver = new FirefoxDriver();
		OMOCrawler omoCrawler = new OMOCrawler(
				"http://www.pbc.gov.cn/zhengcehuobisi/125207/125213/125431/125475/2964194/index.html", driver);
		Map<String, Object> map = omoCrawler.parseOMOData();
		System.out.println(map);
	}
}
