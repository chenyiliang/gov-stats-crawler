package com.github.cyl.service.statecouncil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.cyl.crawler.statecouncil.ExecutiveMeetingCrawler;
import com.github.cyl.dao.statecouncil.ExecutiveMeetingDao;

public class ExecutiveMeetingService {
	private ExecutiveMeetingDao exemeetDao = new ExecutiveMeetingDao();

	public void fetchExeMeetDataToMongo(String urlsTxtPath, String charsetName) {
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
			ExecutiveMeetingCrawler exemeetCrawler = new ExecutiveMeetingCrawler(urlList.get(i));
			Map<String, Object> map = exemeetCrawler.parseExeMeetData();
			exemeetDao.insertOneExeMeetDoc(map);
		}

	}
}
