package com.github.cyl.dao.pmi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.bson.Document;

import com.github.cyl.dao.analysis.AnalysisDao;
import com.mongodb.MongoClient;

public class PMIAnalysisDao {
	private static final Properties props;

	static {
		InputStream inputStream = AnalysisDao.class.getClassLoader().getResourceAsStream("mongodb.properties");
		props = new Properties();
		try {
			props.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("mongodb.properties read failly!");
		}
	}

	private static final String MONGO_HOST = props.getProperty("mongo.host");
	private static final int MONGO_PORT = Integer.valueOf(props.getProperty("mongo.port"));
	private static MongoClient client = new MongoClient(MONGO_HOST, MONGO_PORT);

	public void insertOnePMIAnalysisDoc(Map<String, Object> map) {
		client.getDatabase("autonews").getCollection("pmiAnalysis").insertOne(new Document(map));
	}
}
