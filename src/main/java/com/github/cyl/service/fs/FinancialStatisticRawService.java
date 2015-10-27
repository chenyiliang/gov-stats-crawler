package com.github.cyl.service.fs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.cyl.crawler.fs.FinancialStatisticRawCrawler;
import com.github.cyl.dao.fs.FinancialStatisticDao;

public class FinancialStatisticRawService {
	private FinancialStatisticDao fsDao = new FinancialStatisticDao();

	public void fetchFSDataToMongo(String urlsTxtPath, String charsetName) {
		List<String> urlList = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(urlsTxtPath), charsetName))) {
			String url;
			while ((url = br.readLine()) != null) {
				if (!url.trim().isEmpty()) {
					urlList.add(url.trim());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < urlList.size(); i++) {
			FinancialStatisticRawCrawler fsCrawler = new FinancialStatisticRawCrawler(urlList.get(i));
			Map<String, Object> map = fsCrawler.parseFSData();
			fsDao.insertOneCpiDoc(map);
		}

	}
}
