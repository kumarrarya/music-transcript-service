package com.user.music.transcript.web.dao;

import org.checkerframework.checker.units.qual.K;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IGenericDao {
    <T> boolean upsert(T item, Map<String, Object> objectMap, Class<T> clazz);
    <T> Optional<T> findById(Map<String,Object> objectMap, Class<T> clazz);
    <T> List<T> findAll(Map<String,Object> objectMap, Class<T> clazz);
}
