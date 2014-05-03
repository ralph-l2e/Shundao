package com.huahuostudio.sd.helper;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper {

	public static HashMap<String, Object> JSONObject2HashMap(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (Iterator<String> keys = jsonObject.keys(); keys.hasNext();) {
			try {
				String key = keys.next();
				
				if (jsonObject.get(key) instanceof JSONObject) {

					JSONObject2HashMap((JSONObject) jsonObject.get(key));
					continue;
				}else{
					map.put(key, jsonObject.get(key));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		
		return map;
	}

}
