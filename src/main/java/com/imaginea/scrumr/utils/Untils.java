package com.imaginea.scrumr.utils;

import java.util.Date;

public abstract class Untils {
	
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
}
