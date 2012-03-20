package com.imaginea.scrumr.resources;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imaginea.scrumr.utils.MessageLevel;
import com.imaginea.scrumr.utils.ScrumrException;

public class ResourceUtil {

    private static final Logger logger = LoggerFactory.getLogger(ResourceUtil.class);
    public static final String FAILURE_JSON_MSG = "{\"result\":\"failure\"}";
    public static final String SUCCESS_JSON_MSG = "{\"result\":\"success\"}";
    
    public static int stringToIntegerConversion(String parameterName, String parameterValue) {
        Integer value = null;
        try{
            value = Integer.parseInt(parameterValue);
        }catch(NumberFormatException e){
            logger.error(e.getMessage(), e);
            String exceptionMsg = "Error occured while converting the " +parameterName+" "+parameterValue+ " to integer"  ;
            ScrumrException.create(exceptionMsg, MessageLevel.SEVERE, e);
        }
        return value;
    }

    public static boolean isNotEmpty(List<?> list) {
        return (list != null && list.size() > 0);
    }

}
