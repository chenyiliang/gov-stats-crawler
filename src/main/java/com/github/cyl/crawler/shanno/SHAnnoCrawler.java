package com.github.cyl.crawler.shanno;

import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SHAnnoCrawler {
	private static final Pattern PUB_PAT = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
	private static final Pattern CODE_PAT = Pattern.compile("^(\\d{6})");

	private String indexUrl = "http://www.sse.com.cn/disclosure/listedinfo/announcement/";

	public SHAnnoCrawler() {
		// TODO Auto-generated constructor stub
	}

	public SHAnnoCrawler(String indexUrl) {
		this.indexUrl = indexUrl;
	}

	public List<Map<String, Object>> parseAnnoUrls() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		long waitLoadBaseTime = 2000;
		int waitLoadRandomTime = 1000;
		Random random = new Random(System.currentTimeMillis());
		WebDriver driver = new FirefoxDriver();
		driver.get(indexUrl);
		try {
			Thread.sleep(waitLoadBaseTime + random.nextInt(waitLoadRandomTime));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Document document = Jsoup.parse(driver.getPageSource());
		driver.close();
		Element ul = document.getElementsByClass("list_ul").first();
		Elements lis = ul.getElementsByTag("li");
		for (Element li : lis) {
			Element a = li.getElementsByTag("a").first();
			String href = a.attr("href");
			String title = a.html();
			String list_date = li.getElementsByClass("list_date").first().html();
			Matcher pubMat = PUB_PAT.matcher(list_date);
			int year = 0, month = 0, day = 0, code = 0;
			if (pubMat.find()) {
				year = Integer.parseInt(pubMat.group(1));
				month = Integer.parseInt(pubMat.group(2));
				day = Integer.parseInt(pubMat.group(3));
			}
			Matcher codeMat = CODE_PAT.matcher(title);
			if (codeMat.find()) {
				code = Integer.parseInt(codeMat.group(1));
			}
			String doc = "error";
			try {
				doc = pdf2HtmlStr(href);
			} catch (Exception e) {
				e.printStackTrace();
				doc = "error";
			}

			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("title", title);
			map.put("code", code);
			map.put("year", year);
			map.put("month", month);
			map.put("day", day);
			map.put("origin_url", href);
			map.put("doc", doc);
			list.add(map);
		}

		return list;
	}

	public String pdf2HtmlStr(String href) throws Exception {
		PDDocument pddoc = null;
		try {
			URL url = new URL(href);
			pddoc = PDDocument.load(url.openStream());
			if (pddoc.isEncrypted()) {
				pddoc.decrypt("");
			}
			PDFDomTree parser = new PDFDomTree();

			StringWriter writer = new StringWriter();
			parser.writeText(pddoc, writer);
			return writer.toString();
		} finally {
			if (pddoc != null) {
				pddoc.close();
			}
		}

	}

	public static void main(String[] args) {
		new SHAnnoCrawler().parseAnnoUrls();
	}
}
