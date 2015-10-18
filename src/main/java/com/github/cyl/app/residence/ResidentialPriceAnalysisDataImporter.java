package com.github.cyl.app.residence;

import com.github.cyl.service.residence.ResidentialPriceAnalysisService;

public class ResidentialPriceAnalysisDataImporter {

	public static void main(String[] args) {
		new ResidentialPriceAnalysisService().fetchRPADataToMongo("D:/data/rpaUrl.txt", "utf-8");
	}

}
