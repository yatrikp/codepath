package com.codepath.googleimagesearch.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {

	private static final long serialVersionUID = -8405867704917952791L;
	public String url;
	public String thumbUrl;
	public String title;

	public ImageResult(JSONObject json){
		try{
			this.url = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");
			this.title = json.getString("title");
		}
		catch(JSONException e){
			e.printStackTrace();
		}
	}
	
	public static ArrayList<ImageResult> fromJSONArray(JSONArray array){
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for(int count = 0; count < array.length(); count++){
			try{
				results.add(new ImageResult(array.getJSONObject(count)));
			}
			catch(JSONException e){
				e.printStackTrace();
			}
		}
		return results;
	}

}
