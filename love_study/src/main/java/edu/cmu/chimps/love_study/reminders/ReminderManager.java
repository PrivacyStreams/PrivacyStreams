package edu.cmu.chimps.love_study.reminders;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import edu.cmu.chimps.love_study.Constants;
import edu.cmu.chimps.love_study.QualtricActivity;
import edu.cmu.chimps.love_study.R;


public class ReminderManager extends BroadcastReceiver {

	public static final int REMINDER_TYPE_DAILY = 1;
	public static final int REMINDER_TYPE_WEEKLY = 2;
	public static final int REMINDER_TYPE_DAILY_RANDOM = 3;

	public static final int DAY_IN_MILLISECONDS = 24*60*60*1000;
	public static final int WEEK_IN_MILLISECONDS = 7*24*60*60*1000;

	public static final String KEY_REMINDER_ACTION = "REMINDER_ACTION";
	public static final String KEY_ALARM_TYPE = "alarm_type";
	public static final String KEY_REMINDER_ID = "reminder_id";
	public static final String ALARM_TYPE_DELAY = "alarm_type_delay";
	public static final String ALARM_TYPE_REMINDER = "alarm_type_reminder";
	public static final String ALARM_TYPE_REMOVE_NOTIFICATION = "alarm_type_remove_notification";
	
	private static final String PREF_SAVED_REMINDERS = "preference_saved_reminders";
	
	private static Context mContext;
	
	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			mContext = context;
			if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
				this.scheduleAllReminders();
			} else if (intent.getAction().equals(KEY_REMINDER_ACTION)) {
				// Deliver a notification
				Reminder reminder = this.getReminder(intent.getExtras().getInt(KEY_REMINDER_ID));

				Intent surveyIntent = new Intent();
				surveyIntent.setClass(mContext, QualtricActivity.class);
				surveyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this is required for calling an activity when outside of an activity
				surveyIntent.putExtra(Constants.KEY_SURVEY_URL,reminder.url);
				PendingIntent contentIntent = PendingIntent.getActivity(mContext.getApplicationContext(),
						reminder.id, surveyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

				Notification noti = new Notification.Builder(mContext)
		         .setContentTitle(reminder.notifTitle)
		         .setContentText(reminder.notifText)
		         .setSmallIcon(R.drawable.ic_launcher)
		         .setDefaults(Notification.DEFAULT_ALL)
		         .setAutoCancel(true)
		         .setContentIntent(contentIntent)
		         .build();
				
				NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.notify(reminder.id, noti);
			}
		} catch (Exception e){
			e.printStackTrace();
		}

	}
	
	public ReminderManager(){
		// This is only here for BroadcastReceiver
	}

	public ReminderManager(Context context){
		mContext = context;
	}

	public static void scheduleAllSurveyReminders(){

		Reminder endOfTheDaySurveyReminder = new Reminder();
		endOfTheDaySurveyReminder.hour = 22;
		endOfTheDaySurveyReminder.minute = 0;
		endOfTheDaySurveyReminder.type = REMINDER_TYPE_DAILY;
		endOfTheDaySurveyReminder.url = Constants.END_OF_THE_DAY_EMA_URL;
		endOfTheDaySurveyReminder.notifText = "Self report";
		endOfTheDaySurveyReminder.notifTitle = "Survey";

		scheduleReminder(endOfTheDaySurveyReminder);

		Reminder dailyRandomSurveyReminder = new Reminder();
		dailyRandomSurveyReminder.type = REMINDER_TYPE_DAILY_RANDOM;
		dailyRandomSurveyReminder.url = Constants.DAILY_EMA_URL;
		dailyRandomSurveyReminder.notifText = "Self report";
		dailyRandomSurveyReminder.notifTitle = "Survey";

		scheduleReminder(dailyRandomSurveyReminder);

		Reminder weeklySurveyReminder = new Reminder();
		weeklySurveyReminder.hour = 22;
		weeklySurveyReminder.minute = 0;
		weeklySurveyReminder.type = REMINDER_TYPE_DAILY;
		weeklySurveyReminder.url = Constants.WEEKLY_EMA_URL;
		weeklySurveyReminder.notifText = "Self report";
		weeklySurveyReminder.notifTitle = "Survey";

		scheduleReminder(weeklySurveyReminder);

	}

	public static void scheduleReminder(Reminder reminder){
		// Setup the pending intent to come back here
		AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(mContext, ReminderManager.class);
		i.setAction(KEY_REMINDER_ACTION);
		i.putExtra(KEY_ALARM_TYPE, ALARM_TYPE_REMINDER);
		i.putExtra(KEY_REMINDER_ID, reminder.id);
		PendingIntent pi = PendingIntent.getBroadcast(mContext, reminder.id, i, PendingIntent.FLAG_UPDATE_CURRENT); // identified by reminder ID, so only one alarm per reminder

		Date deliveryTime = getNextOccurrence(reminder);

		switch (reminder.type){
			case REMINDER_TYPE_DAILY:
				mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, deliveryTime.getTime(), DAY_IN_MILLISECONDS, pi); // repeat daily
				break;
			case REMINDER_TYPE_WEEKLY:
				mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, deliveryTime.getTime(), WEEK_IN_MILLISECONDS, pi); // repeat daily
				break;
			case REMINDER_TYPE_DAILY_RANDOM:
				mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, deliveryTime.getTime(), DAY_IN_MILLISECONDS, pi); // repeat daily
				break;
			default:
				break;
		}
	}
	
	public void scheduleAllReminders(){
		ArrayList<Reminder> reminders = this.getAllReminders();
		for (Reminder it : reminders){
			this.scheduleReminder(it);
		}
	}
	
	public void unscheduleReminder(Reminder reminder){
		AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(mContext, ReminderManager.class);
		i.setAction(KEY_REMINDER_ACTION);
		i.putExtra(KEY_ALARM_TYPE, ALARM_TYPE_REMINDER);
		i.putExtra(KEY_REMINDER_ID, reminder.id);
		PendingIntent pi = PendingIntent.getBroadcast(mContext, reminder.id, i, PendingIntent.FLAG_UPDATE_CURRENT); // identified by reminder ID, so only one alarm per reminder

		mAlarmManager.cancel(pi);
	}
	
	public static Date getNextOccurrence(Reminder reminder){
		Calendar setTo = Calendar.getInstance();
		setTo.set(Calendar.SECOND, 0);
		Calendar now = Calendar.getInstance();

		switch (reminder.type){
			case REMINDER_TYPE_DAILY:
				setTo.set(Calendar.HOUR_OF_DAY, reminder.hour);
				setTo.set(Calendar.MINUTE, reminder.minute);
				if (now.getTimeInMillis() > setTo.getTimeInMillis()){
					// previous time today, so set for tomorrow
					setTo.add(Calendar.DAY_OF_YEAR, 1);
				}
				break;
			case REMINDER_TYPE_DAILY_RANDOM:
				Random r = new Random();
				int random_hour = r.nextInt(23 - 10) + 10;
				int random_minute = r.nextInt(60);
				setTo.set(Calendar.HOUR_OF_DAY, random_hour);
				setTo.set(Calendar.MINUTE, random_minute);
				if (now.getTimeInMillis() > setTo.getTimeInMillis()){
					// previous time today, so set for tomorrow
					setTo.add(Calendar.DAY_OF_YEAR, 1);
				}
				break;
			case REMINDER_TYPE_WEEKLY:
				setTo.set(Calendar.HOUR_OF_DAY, reminder.hour);
				setTo.set(Calendar.MINUTE, reminder.minute);
				if (now.getTimeInMillis() > setTo.getTimeInMillis()){
					// previous time today, so set for tomorrow
					setTo.add(Calendar.DAY_OF_YEAR, 7);
				}
				break;
			default:
				break;
		}

		return setTo.getTime();
	}
	
	public Reminder getReminder(Integer id){
		ArrayList<Reminder> reminders = this.getAllReminders();
		for(Reminder it : reminders){
			if (it.id.equals(id)){
				return it;
			}
		}
		return null;
	}
	
	public ArrayList<Reminder> getAllReminders(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		ArrayList<Reminder> reminders = new ArrayList<Reminder>();
		try {
			JSONArray jsons = new JSONArray(prefs.getString(PREF_SAVED_REMINDERS, "[]"));
			for(int i = 0; i < jsons.length(); i++){
				Reminder r = new Reminder();
				r.fromJson(jsons.getJSONObject(i));
				reminders.add(r);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return reminders;
	}

	public void insertReminder(Reminder reminder){
		ArrayList<Reminder> reminders = this.getAllReminders();
		reminders.add(reminder);
		this.scheduleReminder(reminder);
		this.saveAllReminders(reminders);
	}

	public void insertReminder(int hour, int minute, String notificationTitle, String notificationText){
		Reminder reminder = new Reminder();
		reminder.hour = hour;
		reminder.minute = minute;
		reminder.notifTitle = notificationTitle;
		reminder.notifText = notificationText;

		ArrayList<Reminder> reminders = this.getAllReminders();
		reminders.add(reminder);
		this.scheduleReminder(reminder);
		this.saveAllReminders(reminders);

	}

	public void removeReminder(Reminder reminder){
		ArrayList<Reminder> reminders = this.getAllReminders();
		for(Reminder it : reminders){
			if (it.id.equals(reminder.id)){
				this.unscheduleReminder(it);
				reminders.remove(it);
				break;
			}
		}
		this.saveAllReminders(reminders);
	}


	private void saveAllReminders(ArrayList<Reminder> reminders){
		JSONArray jsons = new JSONArray();
		for(Reminder it : reminders){
			jsons.put(it.toJson());
		}
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		prefs.edit().putString(PREF_SAVED_REMINDERS, jsons.toString()).apply();
		
	}

	public void removeAllReminders(){
		ArrayList<Reminder> reminders = this.getAllReminders();
		for(Reminder it : reminders){
			this.unscheduleReminder(it);
		}
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		prefs.edit().putString(PREF_SAVED_REMINDERS, "[]").commit();
	}
	
}
