package com.github.cyl.app.pmi;

import com.github.cyl.service.pmi.PMIAnalysisService;

public class PMIAnalysisDataImporter {

	public static void main(String[] args) {
		new PMIAnalysisService().fetchPMIAnalysisDataToMongo("D:/data/pmiAnalysisUrl.txt", "utf-8");
	}

}
