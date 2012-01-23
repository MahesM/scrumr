package com.imaginea.scrumr.controllers;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class SprintAction extends GenericActionSupport {

	public static final Logger LOGGER = Logger.getLogger(SprintAction.class);

	@Override
	public void prepare() throws Exception {
		super.prepare();
	}

	public String prepareSprintHome() {

		return SUCCESS;
	}

	public String prepareSprintStaticPage() {

		return SUCCESS;
	}

}
