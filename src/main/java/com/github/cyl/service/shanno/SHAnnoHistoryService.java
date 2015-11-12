package com.github.cyl.service.shanno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;

import com.github.cyl.crawler.shanno.SHAnnoHistoryCrawler;
import com.github.cyl.dao.shanno.SHAnnoDao;

public class SHAnnoHistoryService {
	private Pattern CODE_PAT = Pattern.compile("\\d{6}");
	private SHAnnoDao shAnnoDao = new SHAnnoDao();

	public void fetchSHAnnoHistoryDataToMongo(String startDateStr, String endDateStr) {
		List<Integer> codeList = readCodeList();
		for (int i = 0; i < codeList.size(); i++) {
			int code = codeList.get(i);
			System.out.println("***********************爬取：" + code);
			try {
				SHAnnoHistoryCrawler crawler = new SHAnnoHistoryCrawler(code, startDateStr, endDateStr);
				List<Map<String, Object>> dataList = crawler.parseAnnoUrls();
				for (int j = 0; j < dataList.size(); j++) {
					shAnnoDao.insertOneSHAnnoDoc(new Document(dataList.get(j)));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private List<Integer> readCodeList() {
		List<Integer> codeList = new ArrayList<Integer>();
		InputStream inputStream = SHAnnoHistoryService.class.getClassLoader().getResourceAsStream("codes.txt");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))) {
			String codeStr;
			while ((codeStr = br.readLine()) != null) {
				Matcher codeMat = CODE_PAT.matcher(codeStr);
				if (codeMat.find()) {
					codeList.add(Integer.parseInt(codeMat.group()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return codeList;
	}
}
