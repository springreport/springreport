package com.springreport.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;



public class MongoClientUtil {

	/**
	* 获取MongoClient客户端
	* @param url 连接mongo的URL
	* 未配置账号时URL为："mongodb://IP:PORT/databasename" (如：mongodb://127.0.0.1:27017/springreport)
	* 已配置账号时URL为："mongodb://USERNAME:PASSWORD@IP:PORT/databasename" (如：mongodb://root:root@127.0.0.1:27017/springreport)
	* @return
	*/
	public static MongoClient getMongoClient(String url) {
		ConnectionString connString = new ConnectionString(url);
	    MongoClientSettings settings = MongoClientSettings.builder()
	                .applyConnectionString(connString)
	                .retryWrites(false)
	                .build();
	    MongoClient mongoClient = MongoClients.create(settings);
	    return mongoClient;
	}

	
	public static void mongoConnectionTest(String url) {
		ConnectionString connString = new ConnectionString(url);
	    MongoClientSettings settings = MongoClientSettings.builder()
	                .applyConnectionString(connString)
	                .retryWrites(false)
	                .build();
	    MongoClient mongoClient = MongoClients.create(settings);
	    ListDatabasesIterable<Document> listDatabasesIterable = mongoClient.listDatabases();
	    for (Document database: listDatabasesIterable) {
//            System.out.println("DataBase : " + database.get("name"));
        }
	    close(mongoClient);
	}
	public static MongoDatabase getMongoClientDatabase(MongoClient mongoClient,String url) {
	    String databaseName = url.split("/")[url.split("/").length-1];
	    return mongoClient.getDatabase(databaseName);
	}
	
	/**
	* 获取数据库中的集合
	* 
	* @param url 连接mongo的URL
	* 未配置账号时URL为："mongodb://IP:PORT/" (如：mongodb://127.0.0.1:27017)
	* 已配置账号时URL为："mongodb://USERNAME:PASSWORD@IP:PORT/" (如：mongodb://root:root@127.0.0.1:27017)
	* @return
	*/
	public static List<Map<String, String>> getCollectionNames(String url) {
		List<Map<String, String>> result = new ArrayList<>();
	    MongoClient mongoClient = getMongoClient(url);
	    MongoDatabase mongoDatabase = getMongoClientDatabase(mongoClient, url);
	    MongoIterable<String> collectionNames = mongoDatabase.listCollectionNames();
	    for (String collectionName: collectionNames) {
	    	Map<String, String> map = new HashMap<>();
			map.put("name", collectionName);
			map.put("value", collectionName);
			result.add(map);
	    }
	    close(mongoClient);
	    return result;
	}

	
	/**
	* 获取集合中的字段及格式
	* 
	* @param mongoDatabase 数据库
	* @param collectionName 集合名称
	*/
	public static List<Map<String, Object>> getFields(String url, String collectionName) {
		List<Map<String, Object>> result = new ArrayList<>();
		MongoClient mongoClient = getMongoClient(url);
	    MongoDatabase mongoDatabase = getMongoClientDatabase(mongoClient, url);
	    Document doc = mongoDatabase.getCollection(collectionName).find().first();
	    if (Objects.nonNull(doc)) {
	        BsonDocument document = BsonDocument.parse(doc.toJson());
	        for(Map.Entry<String, BsonValue> entry : document.entrySet()){
	            BsonValue value = entry.getValue();
	            String type = value.getBsonType().name();
//	            System.out.println("Column : " + entry.getKey() + " ==== ColumnType : " + type);
	            Map<String, Object> column = new HashMap<String, Object>();
	      	  	column.put("columnName", entry.getKey());
	      	  	column.put("name", entry.getKey());
	      	  	column.put("dataType", type);
	      	  	result.add(column);
	        }
	    }
	    close(mongoClient);
	    return result;
	}
	
	public static List<Map<String, Object>> getaggregateFields(String url, String collectionName,String queryJson) {
		List<Map<String, Object>> result = new ArrayList<>();
		if(StringUtil.isNullOrEmpty(queryJson)) {
    		queryJson = "{}";
    	}
		MongoClient mongoClient = getMongoClient(url);
	    MongoDatabase mongoDatabase = getMongoClientDatabase(mongoClient, url);
	    Object queryObj = JSON.parse(queryJson);
	    List<Document> aggregateList = new ArrayList<Document>();
	    if(queryObj instanceof JSONObject) {
	    	if(!StringUtil.isEmptyMap((JSONObject)queryObj)){
	    		Document query = Document.parse(queryJson);
		    	aggregateList.add(query);
	    	}
	    }else if(queryObj instanceof JSONArray) {
	    	JSONArray queryArray = (JSONArray) queryObj;
	    	for (int i = 0; i < queryArray.size(); i++) {
	    		Document query = Document.parse(JSON.toJSONString(queryArray.get(i)));
	    		aggregateList.add(query);
			}
	    }
	    Document skip = Document.parse("{\"$skip\":0,}");
	    aggregateList.add(skip);
	    Document limit = Document.parse("{\"$limit\":1,}");
	    aggregateList.add(limit);
	    AggregateIterable<Document> queryResult = mongoDatabase.getCollection(collectionName).aggregate(aggregateList);
	    MongoCursor<Document> iterator = queryResult.iterator();
	    while (iterator.hasNext()) {
	        Document document = iterator.next();
	        BsonDocument bsonDocument = BsonDocument.parse(document.toJson());
	        for(Map.Entry<String, BsonValue> entry : bsonDocument.entrySet()){
	        	BsonValue value = entry.getValue();
	            String type = value.getBsonType().name();
	            Map<String, Object> column = new HashMap<String, Object>();
	      	  	column.put("columnName", entry.getKey());
	      	  	column.put("name", entry.getKey());
	      	  	column.put("dataType", type);
	      	  	result.add(column);
	        }
	    }
	    close(mongoClient);
	    return result;
	}
	
	// 5.获取集合中的数据
    public static void getDocument(MongoDatabase mongoDatabase, String collectionName) {
        MongoCollection<Document> collection =  mongoDatabase.getCollection(collectionName);
        // 查询限制50条
        FindIterable<Document> findIterable = collection.find().limit(50);
        System.out.println("CollectionName : " + collectionName);
        for (Document document : findIterable) {
            System.out.println(document.get("name"));
            
        }
    }
    
    /**  
     * @MethodName: findGetData
     * @Description: find方式查询数据
     * @author caiyang
     * @param url
     * @param collectionName
     * @param queryJson
     * @param sortJson
     * @return List<Map<String,Object>>
     * @date 2025-06-30 07:17:43 
     */ 
    public static List<Map<String, Object>> findGetData(String url,String collectionName,String queryJson,String sortJson) {
    	List<Map<String, Object>> result = new ArrayList<>();
    	if(StringUtil.isNullOrEmpty(queryJson)) {
    		queryJson = "{}";
    	}
    	if(StringUtil.isNullOrEmpty(sortJson)) {
    		sortJson = "{}";
    	}
    	MongoClient mongoClient = getMongoClient(url);
	    MongoDatabase mongoDatabase = getMongoClientDatabase(mongoClient, url);
	    MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
	    Document query = Document.parse(queryJson);
	    Document sort = Document.parse(sortJson);
	    FindIterable<Document> queryResult = collection.find(query).sort(sort);

	    MongoCursor<Document> iterator = queryResult.iterator();
	    while (iterator.hasNext()) {
	        Document document = iterator.next();
	        result.add(document);
	    }
	    close(mongoClient);
	    return result;
    }
    
    /**  
     * @MethodName: findGetPageData
     * @Description: find方式分页查询数据
     * @author caiyang
     * @param url
     * @param collectionName
     * @param queryJson
     * @param sortJson
     * @return List<Map<String,Object>>
     * @date 2025-06-30 07:18:16 
     */ 
    public static List<Map<String, Object>> findGetPageData(String url,String collectionName,String queryJson,String sortJson,int pageSize,int pageNumber) {
    	List<Map<String, Object>> result = new ArrayList<>();
    	if(StringUtil.isNullOrEmpty(queryJson)) {
    		queryJson = "{}";
    	}
    	if(StringUtil.isNullOrEmpty(sortJson)) {
    		sortJson = "{}";
    	}
    	MongoClient mongoClient = getMongoClient(url);
	    MongoDatabase mongoDatabase = getMongoClientDatabase(mongoClient, url);
	    MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
	    Document query = Document.parse(queryJson);
	    Document sort = Document.parse(sortJson);
	    int skip = (pageNumber - 1) * pageSize;
	    FindIterable<Document> queryResult = collection.find(query).sort(sort).skip(skip).limit(pageSize);
	    MongoCursor<Document> iterator = queryResult.iterator();
	    while (iterator.hasNext()) {
	        Document document = iterator.next();
	        result.add(document);
	    }
	    close(mongoClient);
	    return result;
    }
    
    public static long findGetDataCount(String url,String collectionName,String queryJson,String sortJson) {
    	if(StringUtil.isNullOrEmpty(queryJson)) {
    		queryJson = "{}";
    	}
    	if(StringUtil.isNullOrEmpty(sortJson)) {
    		sortJson = "{}";
    	}
    	MongoClient mongoClient = getMongoClient(url);
	    MongoDatabase mongoDatabase = getMongoClientDatabase(mongoClient, url);
	    MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
	    Document query = Document.parse(queryJson);
	    long count = collection.countDocuments(query);
	    close(mongoClient);
	    return count;
    }

    /**
    * 获取数据库列表
    * 
    * @param url 连接mongo的URL
    * 未配置账号时URL为："mongodb://IP:PORT/" (如：mongodb://127.0.0.1:27017)
    * 已配置账号时URL为："mongodb://USERNAME:PASSWORD@IP:PORT/" (如：mongodb://root:root@127.0.0.1:27017)
    * @return
    */
    public static ListDatabasesIterable<Document> getDataBases(String url) {
        MongoClient mongoClient = getMongoClient(url);
        ListDatabasesIterable<Document> listDatabasesIterable = mongoClient.listDatabases();
        for (Document database: listDatabasesIterable) {
            System.out.println("DataBase : " + database.get("name"));
        }
        return listDatabasesIterable;
    }
    
    /**  
     * @MethodName: aggregateGetData
     * @Description: 聚合查询数据
     * @author caiyang
     * @param url
     * @param collectionName
     * @param queryJson
     * @param sortJson
     * @return List<Map<String,Object>>
     * @date 2025-07-01 10:08:05 
     */ 
    public static List<Map<String, Object>> aggregateGetData(String url,String collectionName,String queryJson) {
    	List<Map<String, Object>> result = new ArrayList<>();
    	if(StringUtil.isNullOrEmpty(queryJson)) {
    		queryJson = "{}";
    	}
    	MongoClient mongoClient = getMongoClient(url);
	    MongoDatabase mongoDatabase = getMongoClientDatabase(mongoClient, url);
	    MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
	    List<Document> aggregateList = new ArrayList<Document>();
	    Object queryObj = JSON.parse(queryJson);
	    if(queryObj instanceof JSONObject) {
	    	Document query = Document.parse(queryJson);
	    	aggregateList.add(query);
	    }else if(queryObj instanceof JSONArray) {
	    	JSONArray queryArray = (JSONArray) queryObj;
	    	for (int i = 0; i < queryArray.size(); i++) {
	    		Document query = Document.parse(JSON.toJSONString(queryArray.get(i)));
	    		aggregateList.add(query);
			}
	    }
	    AggregateIterable<Document> queryResult =collection.aggregate(aggregateList);
	    MongoCursor<Document> iterator = queryResult.iterator();
	    while (iterator.hasNext()) {
	        Document document = iterator.next();
	        result.add(document);
	    }
	    close(mongoClient);
	    return result;
    }

	/**  
	 * @MethodName: close
	 * @Description: 关闭客户端连接
	 * @author caiyang
	 * @param mongoClient void
	 * @date 2025-06-29 09:36:53 
	 */ 
	public static void close(MongoClient mongoClient) {
		if (Objects.nonNull(mongoClient)) {
			mongoClient.close();
		}
	}

	
	public static void main(String[] args) {
		findGetData("mongodb://127.0.0.1:27017/springreport","userinfo","",null);
//		MongoClient mongoClient = MongoClientUtil.getMongoClient("mongodb://127.0.0.1:27017/springreport");
//		MongoDatabase mongoDatabase = MongoClientUtil.getMongoClientDatabase(mongoClient, "mongodb://127.0.0.1:27017/springreport");
//		MongoClientUtil.getDocument(mongoDatabase, "userinfo");
//		MongoClientUtil.close(mongoClient);
	}
}
