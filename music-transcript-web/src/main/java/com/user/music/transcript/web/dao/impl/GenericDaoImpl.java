package com.user.music.transcript.web.dao.impl;

import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.bulk.UpdateRequest;
import com.user.music.transcript.web.dao.IGenericDao;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class GenericDaoImpl implements IGenericDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public <T> boolean upsert(T item, Map<String, Object> objectMap, Class<T> clazz) {
        Query query = new Query();
        objectMap.forEach((k,v)->{query.addCriteria(Criteria.where(k).is(v));});
        Document document = new Document();
        mongoTemplate.getConverter().write(item, document);
        Update update = Update.fromDocument(document);
        UpdateResult updateResult = mongoTemplate.upsert(query, update, clazz);
        return updateResult.getMatchedCount() > 0 || updateResult.getUpsertedId() != null;
    }

    @Override
    public <T> Optional<T> findById(Map<String, Object> objectMap, Class<T> clazz) {
       Query query = new Query();
       objectMap.forEach((k,v) -> {query.addCriteria(Criteria.where(k).is(v));});
       return Optional.ofNullable(mongoTemplate.findOne(query, clazz));
    }

    @Override
    public <T> List<T> findAll(Map<String, Object> objectMap, Class<T> clazz) {
        Query query = new Query();
        objectMap.forEach((k,v) -> {query.addCriteria(Criteria.where(k).is(v));});
        return mongoTemplate.find(query, clazz);
    }
}
