package com.imaginea.scrumr.utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SprintManager implements ServletContextListener {

	private SprintThread myThread = null;

	public void contextInitialized(ServletContextEvent sce) {
		if (myThread == null) {
			myThread= new SprintThread();
			Timer timer = new Timer();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY,13);
			cal.set(Calendar.MINUTE,57);
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MILLISECOND,0);

			Date d = cal.getTime();
			timer.scheduleAtFixedRate(myThread, d, 24*60*60*1000);
		}
	}

	public void contextDestroyed(ServletContextEvent sce){
		try {
			myThread.cancel();
		} catch (Exception ex) {
		}
	}
	
	
	
}
