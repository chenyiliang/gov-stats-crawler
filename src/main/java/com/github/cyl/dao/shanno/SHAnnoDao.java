package com.github.cyl.dao.shanno;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.github.cyl.dao.analysis.AnalysisDao;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;

public class SHAnnoDao {
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

	public void insertOneSHAnnoDoc(Map<String, Object> map) {
		client.getDatabase("autonews").getCollection("shanno").insertOne(new Document(map));
	}

	public List<Document> getUnDocByNum(int num) {
		List<Document> docList = new ArrayList<Document>();
		FindIterable<Document> iterable = client.getDatabase("autonews").getCollection("shanno")
				.find(Filters.exists("doc", false)).limit(num);
		for (Document document : iterable) {
			docList.add(document);
		}
		return docList;
	}

	public void appendDoc(ObjectId objId, String docStr) {
		client.getDatabase("autonews").getCollection("shanno").updateOne(new Document("_id", objId),
				new Document("$set", new Document("doc", docStr)));
	}
}
