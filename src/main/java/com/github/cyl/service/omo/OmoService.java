package com.github.cyl.service.omo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.github.cyl.crawler.omo.OMOCrawler;
import com.github.cyl.dao.omo.OmoDao;

public class OmoService {
	private WebDriver webDriver = new FirefoxDriver();
	private OmoDao omoDao = new OmoDao();

	public void fetchCPIDataToMongo(String urlsTxtPath, String charsetName) {
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

		try {
			for (int i = 0; i < urlList.size(); i++) {
				OMOCrawler omoCrawler = new OMOCrawler(urlList.get(i), webDriver);
				Map<String, Object> omoMap = omoCrawler.parseOMOData();
				omoDao.insertOneOmoDoc(omoMap);
			}
		} finally {
			webDriver.close();
		}

	}
}
