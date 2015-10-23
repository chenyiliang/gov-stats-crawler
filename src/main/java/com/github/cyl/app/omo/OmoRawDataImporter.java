package com.github.cyl.app.omo;

import com.github.cyl.service.omo.OmoRawService;

public class OmoRawDataImporter {

	public static void main(String[] args) {
		new OmoRawService().fetchCPIDataToMongo("D:/data/omoUrl.txt", "utf-8");
	}

}
