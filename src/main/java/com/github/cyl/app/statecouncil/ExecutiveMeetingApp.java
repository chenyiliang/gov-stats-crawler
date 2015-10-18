package com.github.cyl.app.statecouncil;

import com.github.cyl.service.statecouncil.ExecutiveMeetingService;

public class ExecutiveMeetingApp {

	public static void main(String[] args) {
		new ExecutiveMeetingService().fetchExeMeetDataToMongo("D:/data/exemeetUrl.txt", "utf-8");
	}

}
