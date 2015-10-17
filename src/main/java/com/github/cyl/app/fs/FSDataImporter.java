package com.github.cyl.app.fs;

import com.github.cyl.service.fs.FinancialStatisticService;

public class FSDataImporter {

	public static void main(String[] args) {
		new FinancialStatisticService().fetchFSDataToMongo("D:/data/fsUrl.txt", "utf-8");
	}

}
