package com.imaginea.scrumr.daos;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;

public class GenericJpaDao<E extends IEntity, K extends Serializable> implements IDao<E, K> {

	protected EntityManager entityManager;
	public static final Logger LOGGER = Logger.getLogger(GenericJpaDao.class);
	
	/* Getters and Setters */
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	
	
	
	/* Basic CRUD methods --- Save Find Update Delete */
	
	public void save(E inEntity) throws DataAccessException {
		if(inEntity != null)
			entityManager.persist(inEntity);
		
	}

	public <E extends IEntity> E find(Class<E> inEntityClass, K inPkey)	throws DataAccessException {
		return entityManager.find(inEntityClass, inPkey);
	}

	public <E extends IEntity> E update(E inEntity) throws DataAccessException {
		if(inEntity != null)
			return entityManager.merge(inEntity);
		
		return null;
	}

	public void delete(E inEntity) throws DataAccessException {
		if(inEntity != null)
			entityManager.remove(inEntity);
	}
	
	
	
	/* Other Methods */

	public <E extends IEntity> List<E> findAllEntities(Class<E> inEntityClass) {
		return findAllEntities(inEntityClass, null);
	}

	public <E extends IEntity> List<E> findAllEntities(Class<E> inEntityClass, String orderby) {
		
		String entityClsName = inEntityClass.getSimpleName();
		
		Entity entity = inEntityClass.getAnnotation(Entity.class);
		if (entity != null) {
			String eName = entity.name();
			if (eName != null && eName.length() > 0) {
				entityClsName = eName;
			}
		}

		String sQuery = "SELECT instance FROM " + entityClsName + " instance";
		if (orderby == null) {
			sQuery = sQuery + " order by instance.createdOn ASC";
		} else {
			sQuery = sQuery + " order by instance." + orderby;
		}
			
		Query query = entityManager.createQuery(sQuery);
		return (List<E>) query.getResultList();
	}

	
	public <E extends IEntity> List<Object> getResultsByFieldName(Class<E> inEntityClass, String fieldName) throws DataAccessException {
		
		String entityClsName = inEntityClass.getSimpleName();
		
		Entity entity = inEntityClass.getAnnotation(Entity.class);
		if (entity != null) {
			String eName = entity.name();
			if (eName != null && eName.length() > 0) {
				entityClsName = eName;
			}
		}
		
		String sQuery = "";
		if(fieldName != null) {
			sQuery = "SELECT instance." + fieldName + " FROM " + entityClsName + " instance";
		}
		
		Query query = entityManager.createQuery(sQuery);
		
		return (List) query.getResultList();
	}
	
	
	
	public Object getResult(String queryName, Hashtable<String, Object> criteria) throws DataAccessException {
		
		Query qry = entityManager.createNamedQuery(queryName);
        Enumeration<String> keys = criteria.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            qry.setParameter(key, criteria.get(key));
        }
        Object result;
        try {
            result = qry.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
        return result;
	}

	public Object getResult(StringBuffer query,	Hashtable<String, Object> criteria) throws DataAccessException {
		
		Query qry = entityManager.createQuery(query.toString());
		Enumeration<String> keys = criteria.keys();
		while (keys.hasMoreElements()) {
		    String key = keys.nextElement();
		    qry.setParameter(key, criteria.get(key));
		}
		Object result;
		try {
		    result = qry.getSingleResult();
		} catch (NoResultException nre) {
		    return null;
		}
		return result;
	}

	public List getResults(String queryName, Hashtable<String, Object> criteria) throws DataAccessException {
		
		Query qry = entityManager.createNamedQuery(queryName);
		Enumeration<String> keys = criteria.keys();
		while (keys.hasMoreElements()) {
		    String key = keys.nextElement();
		    qry.setParameter(key, criteria.get(key));
		}
		List result;
		try {
		    result = qry.getResultList();
		} catch (NoResultException nre) {
		    return null;
		}
		return result;
	}

	public <E extends IEntity, obj> E getEntity(Class<E> inElementClass, String queryName, Hashtable<String, obj> criteria)	throws DataAccessException {
		Query qry = entityManager.createNamedQuery(queryName);
		Enumeration<String> keys = criteria.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			qry.setParameter(key, criteria.get(key));
		}
		Object result;
		try {
			result = qry.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
		return (E) result;
	}

	public <E extends IEntity, obj> List<E> getEntities(Class<E> inElementClass, String queryName, Hashtable<String, obj> criteria) throws DataAccessException {
		Query qry = entityManager.createNamedQuery(queryName);
		Enumeration<String> keys = criteria.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			qry.setParameter(key, criteria.get(key));
		}
		Object result;
		try {
			result = qry.getResultList();
		} catch (NoResultException nre) {
			return null;
		}
		return (List<E>) result;
	}
	
	
	public <E extends IEntity, obj> List<E> getEntities(Class<E> inElementClass, String queryName, Hashtable<String, obj> criteria, Integer pageNumber, Integer pageSize) throws DataAccessException {
		Query qry = entityManager.createNamedQuery(queryName);
		Enumeration<String> keys = criteria.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			qry.setParameter(key, criteria.get(key));
		}
		Object result;
		try {
			qry = qry.setFirstResult(pageSize * (pageNumber - 1));
			qry.setMaxResults(pageSize);
			result = qry.getResultList();
		} catch (NoResultException nre) {
			return null;
		}
		return (List<E>) result;
	}


	public <E extends IEntity, obj> List<E> getEntities(Class<E> inElementClass, StringBuffer query, Hashtable<String, obj> criteria) {
		Query qry = entityManager.createQuery(query.toString());
		Enumeration<String> keys = criteria.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			qry.setParameter(key, criteria.get(key));
		}
		Object result;
		try {
			result = qry.getResultList();
		} catch (NoResultException nre) {
			return null;
		}
		return (List<E>) result;
	}

	public <E extends IEntity, obj> List<E> getEntities(Class<E> inElementClass, StringBuffer query, Hashtable<String, obj> criteria, Integer pageNumber, Integer pageSize) {
		Query qry = entityManager.createQuery(query.toString());
		Enumeration<String> keys = criteria.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			qry.setParameter(key, criteria.get(key));
		}
		Object result;
		try {
			if(pageNumber != -1 && pageSize != -1) {
				qry = qry.setFirstResult(pageSize * (pageNumber - 1));
				qry.setMaxResults(pageSize);
			}
			result = qry.getResultList();
		} catch (NoResultException nre) {
			return null;
		}
		return (List<E>) result;
	}

	public <E extends IEntity, obj> List<E> getEntities(Class<E> inElementClass, StringBuffer query) throws DataAccessException {
		Query qry = entityManager.createQuery(query.toString());
		Object result;
		try {
			result = qry.getResultList();
		} catch (NoResultException nre) {
			return null;
		}
		return (List<E>) result;
	}

	
	
}
