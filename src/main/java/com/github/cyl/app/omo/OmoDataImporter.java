package com.github.cyl.app.omo;

import com.github.cyl.service.omo.OmoService;

public class OmoDataImporter {

	public static void main(String[] args) {
		new OmoService().fetchCPIDataToMongo("D:/data/omoUrl.txt", "utf-8");
	}

}
