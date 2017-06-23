package com.github.privacystreams.communication;

import android.content.Context;
import android.os.AsyncTask;

import com.easilydo.sift.api.ApiException;
import com.easilydo.sift.crypto.Signatory;
import com.easilydo.sift.model.Connection;
import com.easilydo.sift.model.ShipmentStatus;
import com.easilydo.sift.model.Sift;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.easilydo.sift.model.Connection.EmailType;

/**
 * Primary class in the Sift Java SDK; used for interacting with the sift server.
 * After constructing an instance of the class with their api and secret keys,
 * the sift developer can make sift api calls by invoking the corresponding method.
 * All apis in the <a href="https://developer.easilydo.com/sift/documentation">Sift API docs</a>
 * are implemented, with the exception of the Connect Tokens and Connect Emails, which are intended
 * for a front end web flow.
 * <p>
 * This class handles all the common parameter packaging as well as request signature generation.
 * Any errors that occur during the api call (timeouts, http errors, etc.) 
 * are thrown as {@link ApiException}.
 */
public class SIFTApiManager {

	public static final String API_ENDPOINT = "https://api.easilydo.com";

	private String apiKey;
	private Signatory signatory;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	/**
	* Sole constructor
	* @param apiKey	sift developer's api key
	* @param secretKey	sift developer's secret key
	*/
	public SIFTApiManager(String apiKey, String secretKey) {
		this.apiKey = apiKey;
		this.signatory = new Signatory(secretKey);
	}

	/**
	* Get a list of sifts for the given user
	* @param username	username of the user to fetch sifts for
	* @return 	list of sifts in descending order of last update time
	*/
	public List<Sift> listSifts(String username,
								OnTaskCompleted listener) {
		return listSifts(username, null, null, null, listener);
	}

	/**
	* Get a list of sifts for the given user since a certain date
	* @param username	username of the user to fetch sifts for
	* @param lastUpdateTime	only return sifts updated since this date
	* @return 	list of sifts in descending order of last update time
	*/
	public List<Sift> listSifts(String username,
								Date lastUpdateTime,
								OnTaskCompleted listener) {
		return listSifts(username, lastUpdateTime, null, null, listener);
	}

	/**
	* Get a list of sifts for the given user since a certain date
	* @param username	username of the user to fetch sifts for
	* @param lastUpdateTime	only return sifts updated since this date
	* @param offset	used for paging, where to start
	* @param limit	maximum number of results to return
	* @return 	list of sifts in descending order of last update time
	*/
	public List<Sift> listSifts(String username,
								Date lastUpdateTime,
								Integer offset,
								Integer limit,
								OnTaskCompleted listener) {
		List<Sift> sifts = new ArrayList<>();
		String path = String.format("/v1/users/%s/sifts", username);

		HashMap<String,Object> params = new HashMap<>();

		if(lastUpdateTime != null) {
			params.put("last_update_time", getEpochSecs(lastUpdateTime));
		}

		if(offset != null) {
			params.put("offset", offset);
		}

		if(limit != null) {
			params.put("limit", limit);
		}

		addCommonParams("GET", path, params);

		executeRequest(API_ENDPOINT + path, "GET", listener, params);

//		for(JsonNode result: results) {
//			sifts.add(Sift.unmarshallSift(result));
//		}

		return sifts;
	}

	/**
	* Get a particular sift
	* @param username	username of the user to fetch sifts for
	* @param siftId	numeric id of the sift to be fetched
	* @return 	the sift corresponding to the provided id
	*/
	public Sift getSift(String username,
						long siftId,
						OnTaskCompleted listener) {
		String path = String.format("/v1/users/%s/sifts/%d", username, siftId);

		HashMap<String,Object> params = new HashMap<String,Object>();

		//TODO: add include_eml handling
		addCommonParams("GET", path, params);
		executeRequest(API_ENDPOINT + path, "GET", listener, params);

//		JsonNode result =

//
//		Sift sift = Sift.unmarshallSift(result);

		return null;
	}

	/**
	* Get the status of a discovered shipment
	* @param username	username of the user
	* @param shipmentId	numeric id of the sift to be fetched
	* @return 	the status of the shipment
	*/
	public ShipmentStatus getShipmentStatus(String username,
											long shipmentId,
											OnTaskCompleted listener) {
		String path = String.format("/v1/users/%s/shipments/%d", username, shipmentId);

		HashMap<String,Object> params = getCommonParams("GET", path);

//		JsonNode results =
		executeRequest(API_ENDPOINT + path, "GET",listener,params);

		ShipmentStatus shipmentStatus = null;
//		if(results.size() > 0) {
//			shipmentStatus = ShipmentStatus.getShipmentStatus(results.findValue("orderStatus").asText());
//		}

		return shipmentStatus;
	}

	/**
	* Register a new user
	* @param username	username of the new user
	* @return 	the numeric user id of the newly created user
	*/
	public long addUser(String username,
						OnTaskCompleted listener) {
		String path = "/v1/users";

		HashMap<String,Object> params = new HashMap<>();
		params.put("username", username);

		addCommonParams("POST", path, params);

//		JsonNode result =
		executeRequest(API_ENDPOINT + path,"POST",listener,params);

//		return result.get("user_id").asLong();
		return -1;
	}

	/**
	* Deletes a user
	* @param username	username of the user to delete
	*/
	public void deleteUser(String username,
						   OnTaskCompleted listener) {
		String path = String.format("/v1/users/%s", username);

		HashMap<String,Object> params = getCommonParams("DELETE", path);

		executeRequest(API_ENDPOINT + path,"DELETE",listener,params);
	}

	/**
	* List a user's email connections
	* @param username	username of the new user 
	* @return 	the list of the user's connsctions
	*/
	public List<Connection> listConnections(String username,
											OnTaskCompleted listener) {
		return listConnections(username, null, null, listener);
	}

	/**
	* List a user's email connections
	* @param username	username of the user whose connections are to be fetched
	* @param offset	used for paging, where to start
	* @param limit	maximum number of results to return
	* @return 	the list of the user's connsctions
	*/
	public List<Connection> listConnections(String username,
											Integer offset,
											Integer limit,
											OnTaskCompleted listener) {
		List<Connection> conns = new ArrayList<>();
		String path = String.format("/v1/users/%s/email_connections", username);

		HashMap<String,Object> params = new HashMap<>();

		if(offset != null) {
			params.put("offset", offset);
		}

		if(limit != null) {
			params.put("limit", limit);
		}

		addCommonParams("GET", path, params);

//		JsonNode results =
		executeRequest(API_ENDPOINT + path,
				"GET",listener,params);

//		for(JsonNode result: results) {
//			conns.add(Connection.unmarshallConnection(result));
//		}

		return conns;
	}

	/**
	* Add a Gmail connection to the given user account
	* @param username	username of the user 
	* @param account	email address 
	* @param refreshToken	oauth refresh token of the account
	* @return 	a generated numeric id for the connection 
	*/
	public long addGmailConnection(String username, String account, String refreshToken, Context context) {
		Map<String,Object> credentials = new HashMap<String,Object>();
		credentials.put("refresh_token", refreshToken);
//		return addEmailConnection(username, account, EmailType.GOOGLE, credentials);
		return -1;
	}



	protected void addEmailConnection(String username,
									  EmailType type,
									  Map<String,Object> credentials,
									  OnTaskCompleted listener) {
		String path = String.format("/v1/users/%s/email_connections", username);

		HashMap<String,Object> params = new HashMap<>(credentials);
		params.put("account_type", type.toString());

		addCommonParams("POST", path, params);

		executeRequest(API_ENDPOINT + path, "POST",listener,params);
//		JsonNode result =
//		return result.get("id").asLong();
	}

	/**
	* Deletes an email connection
	* @param username	username of the user to delete
	* @param connId	numeric id of the email connection
	*/
	public void deleteEmailConnection(String username, long connId, OnTaskCompleted listener) {
		String path = String.format("/v1/users/%s/email_connections/%d", username, connId);

		HashMap<String,Object> params = getCommonParams("DELETE", path);

		executeRequest(API_ENDPOINT + path,"DELETE",listener,params);
	}

	/**
	* Extracts available domain data from the provided eml file.
	* @param emlStream	a stream of eml data
	* @return 	list of sifts objects with extracted data
	*/
	public List<Sift> discovery(InputStream emlStream, OnTaskCompleted listener) {
		List<Sift> sifts = new ArrayList<>();
		String path = "/v1/discovery";
	
		HashMap<String,Object> params = new HashMap<>();
		params.put("email", streamToString(emlStream).trim());

		addCommonParams("POST", path, params);

		executeRequest(API_ENDPOINT + path,"POST",listener,params);

//		Iterator<JsonNode> iter = result.elements();
//		while(iter.hasNext()) {
//			sifts.add(Sift.unmarshallSift(iter.next()));
//		}

		return sifts;
	}

	class NetworkRequestTask extends AsyncTask<HashMap<String,Object>, Void, JSONObject> {
		JSONParser jsonParser = new JSONParser();
		String url;
		String action;
		OnTaskCompleted listener;

		NetworkRequestTask(String url, String action, OnTaskCompleted listener){
			this.url = url;
			this.listener=listener;
			this.action = action;
		}

		@Override
		protected JSONObject doInBackground(HashMap<String, Object>... params) {

			try {
				return jsonParser.makeHttpRequest(
						url, action, params[0]);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(JSONObject json) {

			listener.onTaskCompleted(json);
		}

	}

	protected void executeRequest(String url,
								  String action,
								  OnTaskCompleted listener,
								  HashMap<String,Object> params){
		new NetworkRequestTask(url,action,listener).execute(params);
	}


	protected HashMap<String,Object> getCommonParams(String method, String path) {
		return addCommonParams(method, path, new HashMap<String,Object>());
	}

	protected HashMap<String,Object> addCommonParams(String method, String path, HashMap<String,Object> params) {
		params.put("api_key", apiKey);
		params.put("timestamp", System.currentTimeMillis() / 1000);
		params.put("signature", signatory.generateSignature(method, path, params));

		return params;
	}

	protected static long getEpochSecs(Date date) {
		return date.getTime() / 1000;
	}

	protected static String streamToString(InputStream stream) {
		try {
			char[] buf = new char[1024];
			InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
			StringWriter writer = new StringWriter();

			int numRead;
			while((numRead = reader.read(buf)) != -1) {
				writer.write(buf, 0, numRead);
			}

			reader.close();
			writer.flush();

			return writer.toString();
		} catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}