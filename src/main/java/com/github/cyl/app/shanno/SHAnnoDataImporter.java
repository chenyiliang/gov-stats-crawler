package com.github.cyl.app.shanno;

import com.github.cyl.service.shanno.SHAnnoService;

public class SHAnnoDataImporter {

	public static void main(String[] args) {
		SHAnnoService shAnnoService = new SHAnnoService();
		shAnnoService.fetchRPCDataToMongo();
		shAnnoService.appendDocToMongo();
	}

}
