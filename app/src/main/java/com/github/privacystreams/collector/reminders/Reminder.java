package com.github.privacystreams.collector.reminders;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class Reminder {


	Integer id;
	String notifTitle; // Title to show on notification
	String notifText;  // Text of notification
	Integer hour;  // Hour of the day to deliver reminder
	Integer minute; // Minute of the hour to deliver reminder
	String url;
	Integer type;
	
	public static final String EXTRA_REMINDER_ID = "reminder_id";
	private static final String KEY_ID = "id";
	private static final String KEY_HOUR = "hour";
	private static final String KEY_MIN = "minute";
	private static final String KEY_TITLE = "title";
	private static final String KEY_TEXT = "text";
	private static final String KEY_URL = "url";
	
	public Reminder(){
		Random r = new Random();
		this.id = r.nextInt();
	}
	
	public JSONObject toJson(){
		
		JSONObject json = new JSONObject();
		try {
			json.put(KEY_ID, id);
			json.put(KEY_HOUR, hour);
			json.put(KEY_MIN, minute);
			json.put(KEY_TITLE, notifTitle);
			json.put(KEY_TEXT, notifText);
			json.put(KEY_URL,url);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	public void fromJson(JSONObject json){
		try {
			this.id = json.getInt(KEY_ID);
			this.hour = json.getInt(KEY_HOUR);
			this.minute = json.getInt(KEY_MIN);
			this.notifTitle = json.getString(KEY_TITLE);
			this.notifText = json.getString(KEY_TEXT);
			this.url = json.getString(KEY_URL);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
