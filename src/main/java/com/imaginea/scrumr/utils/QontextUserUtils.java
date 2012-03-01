package com.imaginea.scrumr.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONException;

import com.imaginea.scrumr.qontextclient.QontextRestApiInvocationUtil;
import com.imaginea.scrumr.qontextclient.Settings;

public class QontextUserUtils {

    private static QontextRestApiInvocationUtil qontextRestApiInvocationUtil;

    static {
        qontextRestApiInvocationUtil = new QontextRestApiInvocationUtil();
    }

    public static String populateUsers(int startIndex, int count) throws Exception {
        try {

            String users = qontextRestApiInvocationUtil.searchPeople(startIndex, count).toString();
            return users;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String searchUser(String sortType, boolean showTotalCount, int startIndex,
                                    int count) throws Exception {
        try {
            String users = qontextRestApiInvocationUtil.searchBasicProfile(sortType, startIndex, count).toString();
            return users;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static boolean initialize(HttpServletRequest request, Settings mySettings) {
        boolean isInitialized = false;
        if (qontextRestApiInvocationUtil.hasRequiredParameters(request)) {
            try {
                qontextRestApiInvocationUtil.init(request, mySettings);
                isInitialized = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // request.setAttribute("helper", helper);

        } else {
            isInitialized = false;

            // request.getRequestDispatcher("/configure.jsp").include(request, response);
        }
        return isInitialized;
    }

    public static QontextRestApiInvocationUtil getQontextRestApiInvocationUtil() {
        return qontextRestApiInvocationUtil;
    }
}
