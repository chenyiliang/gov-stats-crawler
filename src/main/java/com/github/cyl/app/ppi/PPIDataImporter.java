package com.github.cyl.app.ppi;

import com.github.cyl.service.ppi.PPIService;

public class PPIDataImporter {

	public static void main(String[] args) {
		new PPIService().fetchPPIDataToMongo("D:/yicai_workspace/2014PPI.txt", "utf-8");
	}

}
