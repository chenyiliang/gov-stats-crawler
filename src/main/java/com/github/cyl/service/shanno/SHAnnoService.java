package com.github.cyl.service.shanno;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.jsoup.Jsoup;

import com.github.cyl.crawler.shanno.SHAnnoCrawler;
import com.github.cyl.dao.shanno.SHAnnoDao;

public class SHAnnoService {
	private SHAnnoDao shAnnoDao = new SHAnnoDao();
	private SHAnnoCrawler shAnnoCrawler = new SHAnnoCrawler();

	public void fetchRPCDataToMongo() {
		List<Map<String, Object>> list = shAnnoCrawler.parseAnnoUrls();
		for (Map<String, Object> map : list) {
			shAnnoDao.insertOneSHAnnoDoc(map);
		}
	}

	public void appendDocToMongo() {
		int len = 1;
		while (len != 0) {
			List<Document> list = shAnnoDao.getUnDocByNum(10);
			for (Document doc : list) {
				String url = doc.getString("originUrl");
				try {
					String docStr = shAnnoCrawler.pdf2HtmlStr(url);
					org.jsoup.nodes.Document htmldoc = Jsoup.parse(docStr);
					htmldoc.getElementsByTag("img").remove();
					shAnnoDao.appendDoc(doc.getObjectId("_id"), htmldoc.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			len = list.size();
		}
	}
}
