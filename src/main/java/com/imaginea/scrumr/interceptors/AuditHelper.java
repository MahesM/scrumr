package com.imaginea.scrumr.interceptors;

import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.imaginea.scrumr.entities.IAuditTrail;
import com.imaginea.scrumr.entities.IAuditable;
import com.imaginea.scrumr.interfaces.IDao;
import com.imaginea.scrumr.interfaces.IEntity;

public class AuditHelper {

	public static final Logger LOGGER = Logger.getLogger(AuditHelper.class);

	protected static AuditHelper theHelper = null;
	private static Log log = null;
	private IDao<IEntity, Integer> auditDao;

	public IDao<IEntity, Integer> getAuditDao() {
		return auditDao;
	}

	public void setAuditDao(IDao<IEntity, Integer> auditDao) {
		this.auditDao = auditDao;
	}

	/**
	 * Constructor
	 */
	public AuditHelper() {
		theHelper = this;
	}

	public void doAudit(Object obj) throws Exception {
		log = LogFactory.getLog(getClass());
		if (obj instanceof IAuditable) {
			String objClass = obj.getClass().getSimpleName();
			log.info("Entering full audit for Class: " + objClass);
			String auditClassName = "com.liquidation.lpid.audit.entities."
					+ "Audit_" + objClass;
			log.debug("Full Audit started for Class name: " + auditClassName);
			Class<?> auditClz = Class.forName(auditClassName);
			try {
				IAuditTrail auditObj = null;
				Constructor<?> constructor = auditClz.getConstructor(
						obj.getClass(), auditDao.getClass());
				auditObj = (IAuditTrail) constructor.newInstance(obj, auditDao);
				auditDao.save((IEntity) auditObj);
				log.debug("Full Audit done for Class name: " + auditClassName);
			} catch (NoSuchMethodException e) {
				log.error(
						"Couldn't perform full Audit, copy constructer missing for Audit Class: "
								+ auditClassName, e);
			}
		}
	}

}
