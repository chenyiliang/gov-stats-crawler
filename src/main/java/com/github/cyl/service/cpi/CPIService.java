package com.github.cyl.service.cpi;

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

import com.github.cyl.crawler.cpi.CPICrawler;
import com.github.cyl.dao.cpi.CPIDao;

public class CPIService {
	private static final Properties cpi_cn_en_props;

	static {
		InputStream inputStream = CPIService.class.getClassLoader().getResourceAsStream("cpi_cn_en.properties");
		cpi_cn_en_props = new Properties();
		try {
			cpi_cn_en_props.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("mongodb.properties read failly!");
		}
	}

	private CPIDao cpiDao = new CPIDao();

	public void fetchCPIDataToMongo(String urlsTxtPath, String charsetName) {
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
			CPICrawler cpiCrawler = new CPICrawler(urlList.get(i));
			Map<String, Object> rawCPIMap = cpiCrawler.parseCPIData();
			Map<String, Object> cpiMap = assembleCPIDoc(rawCPIMap);
			cpiDao.insertOneCpiDoc(cpiMap);
			System.out.println(cpiMap);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private Map<String, Object> assembleCPIDoc(Map<String, Object> rawCPIMap) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Set<Entry<String, Object>> entrySet = rawCPIMap.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			map.put(cpi_cn_en_props.getProperty(entry.getKey()), entry.getValue());
		}
		return map;
	}
}
