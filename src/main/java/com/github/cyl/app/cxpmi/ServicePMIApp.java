package com.github.cyl.app.cxpmi;

import com.github.cyl.service.cxpmi.ServicePMIService;

public class ServicePMIApp {

	public static void main(String[] args) {
		new ServicePMIService().fetchSvPMIDataToMongo("D:/data/cxsvpmiUrl.txt", "utf-8");
	}

}
