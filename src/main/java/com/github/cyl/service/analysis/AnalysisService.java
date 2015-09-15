package com.github.cyl.service.analysis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.cyl.crawler.analysis.AnalysisCrawler;
import com.github.cyl.dao.analysis.AnalysisDao;

public class AnalysisService {
	private AnalysisDao analysisDao = new AnalysisDao();

	public void fetchAnalysisDataToMongo(String urlsTxtPath, String charsetName) {
		List<String> urlList = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(urlsTxtPath), charsetName))) {
			String url;
			while ((url = br.readLine()) != null) {
				urlList.add(url.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < urlList.size(); i++) {
			AnalysisCrawler analysisCrawler = new AnalysisCrawler(urlList.get(i));
			List<Object> rawAnalysisList = analysisCrawler.parseAnalysisData();
			Map<String, Object> analysisMap = assembleAnalysisDoc(rawAnalysisList);
			analysisDao.insertOneAnalysisDoc(analysisMap);
			System.out.println(analysisMap);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private Map<String, Object> assembleAnalysisDoc(List<Object> list) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("year", list.get(0));
		map.put("month", list.get(1));
		map.put("title", list.get(2));
		map.put("contents", list.subList(2, list.size()));
		return map;
	}
}
