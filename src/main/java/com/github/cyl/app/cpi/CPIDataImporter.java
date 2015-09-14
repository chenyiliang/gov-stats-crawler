package com.github.cyl.app.cpi;

import com.github.cyl.service.cpi.CPIService;

public class CPIDataImporter {

	public static void main(String[] args) {
		new CPIService().fetchCPIDataToMongo("D:/yicai_workspace/2014CPI.txt", "utf-8");
	}

}
