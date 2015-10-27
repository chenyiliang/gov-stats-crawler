package com.github.cyl.app.fs;

import com.github.cyl.service.fs.FinancialStatisticRawService;

public class FSRawDataImporter {

	public static void main(String[] args) {
		new FinancialStatisticRawService().fetchFSDataToMongo("D:/data/fsUrl.txt", "utf-8");
	}

}
