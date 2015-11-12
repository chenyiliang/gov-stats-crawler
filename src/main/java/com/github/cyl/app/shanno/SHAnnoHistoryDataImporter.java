package com.github.cyl.app.shanno;

import com.github.cyl.service.shanno.SHAnnoHistoryService;

public class SHAnnoHistoryDataImporter {

	public static void main(String[] args) {
		new SHAnnoHistoryService().fetchSHAnnoHistoryDataToMongo("2015-01-01", "2015-11-11");
	}

}
