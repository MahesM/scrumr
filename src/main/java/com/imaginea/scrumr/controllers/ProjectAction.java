package com.imaginea.scrumr.controllers;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class ProjectAction extends GenericActionSupport {

	public static final Logger LOGGER = Logger.getLogger(ProjectAction.class);

	@Override
	public void prepare() throws Exception {
		super.prepare();
	}

	public String prepareProjectHome() {

		return SUCCESS;
	}

	public String prepareStaticPage() {

		return SUCCESS;
	}

}
