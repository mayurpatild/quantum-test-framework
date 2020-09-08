package com.frmwrk.base;

import javax.naming.AuthenticationException;

//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;

public class RestClientAPI {
	
	/** 
	   * This variable is used for providing the Jira URL. In future please move it to the properties file where
	   * all Jira URLs, credentials etc can be stored
	*/
	private static String BASE_URL = "http://01hw612539vm1:8080";
	
	/** 
	   * This is used to add comments to Jira using RestAPI  
	   * @param String - comment data
	   * @param String - Jira Issue ID for which comment needs to be added
	*/
	public static void RestAddComment(String strComment, String strIssueID) {
		try {
		String auth = new String(Base64.encode("admin:admin"));
		String createCommentData = "{\"body\": \"" + strComment + "\"}\"";
		String comment = invokePostMethod(auth, BASE_URL+"/rest/api/2/issue/" + strIssueID + "/comment", createCommentData);
		System.out.println(comment);
		JSONObject issueObj = new JSONObject(comment);
		String newKey = issueObj.getString("id");
		System.out.println("id:"+newKey);
		} catch (AuthenticationException e) {
			System.out.println("Username or Password wrong!");
			e.printStackTrace();
		} catch (ClientHandlerException e) {
			System.out.println("Error invoking REST method");
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("Invalid JSON output");
			e.printStackTrace();
		}
	}
	
	/** 
	   * This function is used for invoking the rest api get method  
	   * @param String - authentication data
	   * @param String - url to invoke the corresponding Jira API
	*/
	@SuppressWarnings("unused")
	private static String invokeGetMethod(String auth, String url) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").get(ClientResponse.class);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
		return response.getEntity(String.class);
	}

	/** 
	   * This function is used for invoking the rest api post method  
	   * @param String - authentication data
	   * @param String - url to invoke the corresponding Jira API
	   * @param String - data to be posted which is JSON format but received by this function as string
	*/	
	private static String invokePostMethod(String auth, String url, String data) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").post(ClientResponse.class, data);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
		return response.getEntity(String.class);
	}

	/** 
	   * This function is used for invoking the rest api put method  
	   * @param String - authentication data
	   * @param String - url to invoke the corresponding Jira API
	   * @param String - data to be posted which is JSON format but received by this function as string
	*/	
	@SuppressWarnings("unused")
	private static void invokePutMethod(String auth, String url, String data) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").put(ClientResponse.class, data);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
	}

	/** 
	   * This function is used for invoking the rest api delete method  
	   * @param String - authentication data
	   * @param String - url to invoke the corresponding Jira API for deleting an entity in Jira
	*/	
	@SuppressWarnings("unused")
	private static void invokeDeleteMethod(String auth, String url) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").delete(ClientResponse.class);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
	}
}
