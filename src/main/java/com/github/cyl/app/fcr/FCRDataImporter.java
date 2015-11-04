package com.github.cyl.app.fcr;

import com.github.cyl.service.fcr.FCRService;

public class FCRDataImporter {
	public static void main(String[] args) {
		new FCRService().fetchPMIAnalysisDataToMongo("D:/data/fcrUrl.txt", "utf-8");
	}
}
