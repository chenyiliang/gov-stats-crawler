package com.github.cyl.dao.ppi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.bson.Document;

import com.github.cyl.dao.cpi.CPIDao;
import com.mongodb.MongoClient;

public class PPIDao {
	private static final Properties props;

	static {
		InputStream inputStream = CPIDao.class.getClassLoader().getResourceAsStream("mongodb.properties");
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

	public void insertOnePpiDoc(Map<String, Object> map) {
		client.getDatabase("autonews").getCollection("ppi").insertOne(new Document(map));
	}
}
