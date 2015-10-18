package com.github.cyl.service.residence;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.cyl.crawler.residence.ResidentialPriceChangesCrawler;
import com.github.cyl.dao.residence.ResidentialPriceChangesDao;

public class ResidentialPriceChangesService {
	private ResidentialPriceChangesDao rpcDao = new ResidentialPriceChangesDao();

	public void fetchRPCDataToMongo(String urlsTxtPath, String charsetName) {
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
			ResidentialPriceChangesCrawler rpcCrawler = new ResidentialPriceChangesCrawler(urlList.get(i));
			Map<String, Object> map = rpcCrawler.parseRPCData();
			rpcDao.insertOneRPCDoc(map);
		}

	}
}
