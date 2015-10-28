package com.github.cyl.service.pmi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.cyl.crawler.pmi.PMIWuliuCrawler;
import com.github.cyl.dao.pmi.PMIWuliuDao;

public class PMIWuliuService {
	private PMIWuliuDao pmiWuliuDao = new PMIWuliuDao();

	public void fetchPMIWuliuDataToMongo(String urlsTxtPath, String charsetName) {
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
			PMIWuliuCrawler crawler = new PMIWuliuCrawler(urlList.get(i));
			Map<String, Object> map = crawler.parsePMIWuliuData();
			pmiWuliuDao.insertOnePMIWuliuDoc(map);
		}

	}
}
