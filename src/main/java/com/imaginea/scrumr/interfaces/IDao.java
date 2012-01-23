package com.imaginea.scrumr.interfaces;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

import org.springframework.dao.DataAccessException;

//@SuppressWarnings({"hiding","rawtypes"})
public interface IDao<E extends IEntity, K extends Serializable> {
	
	
	/* Basic CRUD methods --- Save Find Update Delete */
	
	void save(E inEntity) throws DataAccessException;

	<E extends IEntity> E find(Class<E> inEntityClass, K inPkey) throws DataAccessException;
	
	<E extends IEntity> E update(E inEntity) throws DataAccessException;

    void delete(E inEntity) throws DataAccessException;
    
    
    
    /* Other Methods */
    
    <E extends IEntity> List<E> findAllEntities(Class<E> inEntityClass);
    
    <E extends IEntity> List<E> findAllEntities(Class<E> inEntityClass, String orderby);
    
    <E extends IEntity> List getResultsByFieldName(Class<E> inEntityClass, String fieldName) throws DataAccessException;
    
    
    Object getResult(String queryName, Hashtable<String, Object> criteria) throws DataAccessException;
    
    Object getResult(StringBuffer query, Hashtable<String, Object> criteria) throws DataAccessException;
    
    List getResults(String queryName, Hashtable<String, Object> criteria) throws DataAccessException;
    
    
    
    <E extends IEntity, obj extends Object> E getEntity(Class<E> inElementClass, String queryName, Hashtable<String, obj> criteria) throws DataAccessException;

    <E extends IEntity, obj extends Object> List<E> getEntities(Class<E> inElementClass, String queryName, Hashtable<String, obj> criteria, Integer pageNumber, Integer pageSize) throws DataAccessException;
    
    <E extends IEntity, obj extends Object> List<E> getEntities(Class<E> inElementClass, String queryName, Hashtable<String, obj> criteria) throws DataAccessException;
    
    <E extends IEntity, obj extends Object> List<E> getEntities(Class<E> inElementClass, StringBuffer query, Hashtable<String, obj> criteria);
    
    <E extends IEntity, obj extends Object> List<E> getEntities(Class<E> inElementClass, StringBuffer query, Hashtable<String, obj> criteria, Integer pageNumber, Integer pageSize);
    
    <E extends IEntity, obj extends Object> List<E> getEntities(Class<E> inElementClass, StringBuffer query) throws DataAccessException;
    
         

}
