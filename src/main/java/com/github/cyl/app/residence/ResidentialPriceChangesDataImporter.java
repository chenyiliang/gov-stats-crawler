package com.github.cyl.app.residence;

import com.github.cyl.service.residence.ResidentialPriceChangesService;

public class ResidentialPriceChangesDataImporter {

	public static void main(String[] args) {
		new ResidentialPriceChangesService().fetchRPCDataToMongo("D:/data/rpcUrl.txt", "utf-8");
	}

}
