package com.xyz.web.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by xyz in Note_Server on 2018/11/14 .
 */
public class Utils {
    public static String streamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public static JSONObject getErrJson(Exception e) {
        JSONObject retJSON = new JSONObject();
        try {
            retJSON.put("ret", 1);
            retJSON.put("msg", e.getMessage());
            retJSON.put("data", "");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return retJSON;
    }
}
