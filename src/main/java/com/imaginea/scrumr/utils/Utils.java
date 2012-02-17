package com.imaginea.scrumr.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.imaginea.scrumr.entities.User;

public abstract class Utils {

	public static int getCurrentSprint(Date start, int duration){
		return (int) Math.ceil((((new Date().getTime() - start.getTime()) / (1000 * 60 * 60 * 24))/(7*duration))) + 1;
	}

	public static int getSprintCount(Date start, Date end, int duration){
		int count = (int)(((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24))/(7*duration));
		int rem = (int)(((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24))%(7*duration));
		if(rem > 0){
			return count + 1;
		}
		return count;
	}

	public static String getAbsoluteURI(HttpServletRequest request){
		String file = request.getRequestURI();
		URL reconstructedURL;
		try {
			reconstructedURL = new URL(request.getScheme(),
					request.getServerName(),
					request.getServerPort(),
					file);
			return URLEncoder.encode(reconstructedURL.toString(),"UTF-8");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	public static String getAbsoluteURL(HttpServletRequest request){
		String file = request.getRequestURI();
		if (request.getQueryString() != null) {
			file += '?' + request.getQueryString();
		}
		URL reconstructedURL;
		try {
			reconstructedURL = new URL(request.getScheme(),
					request.getServerName(),
					request.getServerPort(),
					file);
			return reconstructedURL.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}

	}

}
