package com.imaginea.scrumr.controllers;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class HomeAction extends GenericActionSupport {

	public static final Logger LOGGER = Logger.getLogger(HomeAction.class);

	@Override
	public void prepare() throws Exception {
		super.prepare();
	}

	public String prepareHome() {

		return SUCCESS;
	}

	public String prepareStaticPage() {

		return SUCCESS;
	}

}
