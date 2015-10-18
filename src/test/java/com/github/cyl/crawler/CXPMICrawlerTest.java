package com.github.cyl.crawler;

import java.util.Map;

import org.junit.Test;

import com.github.cyl.crawler.cxpmi.ServicePMICrawler;

public class CXPMICrawlerTest {
	@Test
	public void test() throws Exception {
		ServicePMICrawler servicePMICrawler = new ServicePMICrawler("http://pmi.caixin.com/2015-09-01/100845493.html");
		Map<String, Object> map = servicePMICrawler.parseServicePMIData();
		System.out.println(map);
	}
}
