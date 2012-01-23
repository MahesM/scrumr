package com.imaginea.scrumr.services;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.imaginea.scrumr.entities.Status;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;
import com.imaginea.scrumr.interfaces.StatusManager;

public class StatusManagerImpl implements StatusManager {

	public static final Logger LOGGER = Logger.getLogger(StatusManagerImpl.class);

	private IDao<IEntity, Integer> genericDao;

	@Transactional
	public void createStatus(Status Status) {
		if(Status != null) {

			genericDao.save(Status);

		}
	}

	public Status readStatus(Integer pkey) {

		return genericDao.find(Status.class, pkey);

	}

	@Transactional
	public void updateStatus(Status Status) {
		if(Status != null) {

			genericDao.update(Status);

		}
	}

	@Transactional
	public void deleteStatus(Status Status) {
		if(Status != null) {

			genericDao.delete(Status);

		}
	}

	/* Getters and Setters */

	public IDao<IEntity, Integer> getGenericDao() {
		return genericDao;
	}

	public void setGenericDao(IDao<IEntity, Integer> genericDao) {
		this.genericDao = genericDao;
	}

}
