package com.github.cyl.app.pmi;

import com.github.cyl.service.pmi.PMIWuliuService;

public class PMIWuliuDataImporter {

	public static void main(String[] args) {
		new PMIWuliuService().fetchPMIWuliuDataToMongo("D:/data/pmiWuliuUrl.txt", "utf-8");
	}

}
