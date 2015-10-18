package com.github.cyl.service.cxpmi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.cyl.crawler.cxpmi.ServicePMICrawler;
import com.github.cyl.dao.cxpmi.ServicePMIDao;

public class ServicePMIService {
	private ServicePMIDao svpmiDao = new ServicePMIDao();

	public void fetchSvPMIDataToMongo(String urlsTxtPath, String charsetName) {
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
			ServicePMICrawler svpmiCrawler = new ServicePMICrawler(urlList.get(i));
			Map<String, Object> map = svpmiCrawler.parseServicePMIData();
			svpmiDao.insertOneServicePMIDoc(map);
		}

	}
}
