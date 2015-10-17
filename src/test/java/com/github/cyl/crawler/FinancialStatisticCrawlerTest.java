package com.github.cyl.crawler;

import java.io.IOException;
import java.util.Map;

import com.github.cyl.crawler.fs.FinancialStatisticCrawler;

public class FinancialStatisticCrawlerTest {

	public static void main(String[] args) throws IOException {
		FinancialStatisticCrawler crawler = new FinancialStatisticCrawler(
				"http://www.pbc.gov.cn/diaochatongjisi/116219/116225/2949941/index.html");
		Map<String, Object> map = crawler.parseFSData();
		System.out.println(map);
	}

}
