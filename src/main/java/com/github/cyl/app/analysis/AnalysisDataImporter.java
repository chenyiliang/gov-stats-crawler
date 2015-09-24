package com.github.cyl.app.analysis;

import com.github.cyl.service.analysis.AnalysisService;

public class AnalysisDataImporter {
	public static void main(String[] args) {
		new AnalysisService().fetchAnalysisDataToMongo("D:/yicai_workspace/2014Analysis.txt", "utf-8");
	}
}
