package com.gsoeller.personalization.maps.jobs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.Thread;
import com.google.api.services.gmail.model.ListThreadsResponse;


public class ReadEmailJob implements Job {

	private static final String SCOPE = "https://www.googleapis.com/auth/gmail.readonly";
	private static final String APP_NAME = "Gmail API Quickstart";
	private static final String USER = "me";
	private static final String CLIENT_SECRET_PATH = "/Users/garysoeller/dev/src/MapsPersonalization/MapsService/src/main/resources/client_secret_685814389331-atc0iid8vd8e48dog9cd9ebmp5vpuatl.apps.googleusercontent.com.json";

	private static GoogleClientSecrets clientSecrets;

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(
					new FileReader(
							"/Users/garysoeller/dev/src/MapsPersonalization/MapsService/src/main/resources/ids.txt"));
			while ((sCurrentLine = br.readLine()) != null) {
				//handleId(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		try {
			fetchMessages();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

	public void handleId(Thread thread) {
		System.out.println(thread.getId());
	}

	public void fetchMessages() throws FileNotFoundException, IOException {

		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new com.google.api.client.json.jackson2.JacksonFactory();

		clientSecrets = GoogleClientSecrets.load(jsonFactory, new FileReader(
				CLIENT_SECRET_PATH));

		// Allow user to authorize via url.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, jsonFactory, clientSecrets, Arrays.asList(SCOPE))
				.setAccessType("online").setApprovalPrompt("auto").build();

		String url = flow.newAuthorizationUrl()
				.setRedirectUri(GoogleOAuthConstants.OOB_REDIRECT_URI).build();
		System.out
				.println("Please open the following URL in your browser then type"
						+ " the authorization code:\n" + url);

		// Read code entered by user.
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();

		// Generate Credential using retrieved code.
		GoogleTokenResponse response = flow.newTokenRequest(code)
				.setRedirectUri(GoogleOAuthConstants.OOB_REDIRECT_URI)
				.execute();
		GoogleCredential credential = new GoogleCredential()
				.setFromTokenResponse(response);

		// Create a new authorized Gmail API client
		Gmail service = new Gmail.Builder(httpTransport, jsonFactory,
				credential).setApplicationName(APP_NAME).build();
		
		System.out.println("getting responses");
		ListMessagesResponse responses = service.users().messages().list("me").execute();
		System.out.println("got responses");
	    List<Message> messages = new ArrayList<Message>();
	    while (responses.getMessages() != null) {
	      messages.addAll(responses.getMessages());
	      System.out.println("Adding message");
	      if (responses.getNextPageToken() != null) {
	        String pageToken = responses.getNextPageToken();
	        responses = service.users().messages().list("me")
	            .setPageToken(pageToken).execute();
	      } else {
	        break;
	      }
	    }

	    for (Message message : messages) {
	      //System.out.println(message.toPrettyString());
	      Message m = service.users().messages().get("me", message.getId()).execute();
	      if(!m.getSnippet().contains("Bing")) {
	    	  System.out.println(getId(m.getSnippet()));
	      }
	    }


	}
	
	private String getId(String snippet) {
		snippet = snippet.substring(3);
		return snippet.substring(0, 7).trim();
	}
}