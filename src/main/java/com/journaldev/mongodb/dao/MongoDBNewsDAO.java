package com.journaldev.mongodb.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.journaldev.mongodb.converter.NewsConverter;
import com.journaldev.mongodb.converter.PersonConverter;
import com.journaldev.mongodb.model.News;
import com.journaldev.mongodb.model.Person;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

//DAO class for different MongoDB CRUD operations
//take special note of "id" String to ObjectId conversion and vice versa
//also take note of "_id" key for primary key
public class MongoDBNewsDAO {

	private DBCollection col;

	public MongoDBNewsDAO(MongoClient mongo) {
		this.col = mongo.getDB("journaldev").getCollection("News");
	}

	public News createNews(News n) {
		DBObject doc = NewsConverter.toDBObject(n);
		this.col.insert(doc);
		ObjectId id = (ObjectId) doc.get("_id");
		n.setId(id.toString());
		return n;
	}

	public void updateNews(News n) {
		DBObject query = BasicDBObjectBuilder.start()
				.append("_id", new ObjectId(n.getId())).get();
		this.col.update(query, NewsConverter.toDBObject(n));
	}

	public List<News> readAllNews() {
		List<News> data = new ArrayList<News>();
		DBCursor cursor = col.find();
		while (cursor.hasNext()) {
			DBObject doc = cursor.next();
			News n = NewsConverter.toNews(doc);
			data.add(n);
		}
		return data;
	}
	
	public List<News> readNewsByType(String type) {
		DBObject query = BasicDBObjectBuilder.start()
				.append("tipo", type).get();
		
		List<News> data = new ArrayList<News>();
		DBCursor cursor = col.find(query);
		while (cursor.hasNext()) {
			DBObject doc = cursor.next();
			News n = NewsConverter.toNews(doc);
			data.add(n);
		}
		return data;
	}

	public void deleteNews(News n) {
		DBObject query = BasicDBObjectBuilder.start()
				.append("_id", new ObjectId(n.getId())).get();
		this.col.remove(query);
	}

	public News readNews(News n) {
		DBObject query = BasicDBObjectBuilder.start()
				.append("_id", new ObjectId(n.getId())).get();
		DBObject data = this.col.findOne(query);
		return NewsConverter.toNews(data);
	}
	

}
