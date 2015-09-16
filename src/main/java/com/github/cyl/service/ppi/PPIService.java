package com.github.cyl.service.ppi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;

import com.github.cyl.crawler.ppi.PPICrawler;
import com.github.cyl.dao.ppi.PPIDao;

public class PPIService {
	private static final Properties ppi_cn_en_props;

	static {
		InputStream inputStream = PPIService.class.getClassLoader().getResourceAsStream("ppi_cn_en.properties");
		ppi_cn_en_props = new Properties();
		try {
			ppi_cn_en_props.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("mongodb.properties read failly!");
		}
	}

	private PPIDao ppiDao = new PPIDao();

	public void fetchPPIDataToMongo(String urlsTxtPath, String charsetName) {
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
			PPICrawler ppiCrawler = new PPICrawler(urlList.get(i));
			Map<String, Object> rawPPIMap = ppiCrawler.parsePPIData();
			Map<String, Object> ppiMap = assemblePPIDoc(rawPPIMap);
			ppiDao.insertOnePpiDoc(ppiMap);
			System.out.println(ppiMap);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private Map<String, Object> assemblePPIDoc(Map<String, Object> rawPPIMap) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Set<Entry<String, Object>> entrySet = rawPPIMap.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			map.put(ppi_cn_en_props.getProperty(entry.getKey()), entry.getValue());
		}
		return map;
	}
}
