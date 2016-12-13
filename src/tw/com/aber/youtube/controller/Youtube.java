/*
 * Copyright (c) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package tw.com.aber.youtube.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.DateTime;
import com.google.api.services.samples.youtube.cmdline.Auth;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CdnSettings;
import com.google.api.services.youtube.model.LiveBroadcast;
import com.google.api.services.youtube.model.LiveBroadcastSnippet;
import com.google.api.services.youtube.model.LiveBroadcastStatus;
import com.google.api.services.youtube.model.LiveStream;
import com.google.api.services.youtube.model.LiveStreamSnippet;
import com.google.common.collect.Lists;

/**
 * Use the YouTube Live Streaming API to insert a broadcast and a stream
 * and then bind them together. Use OAuth 2.0 to authorize the API requests.
 *
 * @author Ibrahim Ulukaya (revise)
 */
public class Youtube extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LogManager.getLogger(Youtube.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		CreateBroadcast createBroadcast = new CreateBroadcast();
		
		String groupId = request.getSession().getAttribute("group_id").toString();
		String action = request.getParameter("action");
		
		HttpSession session = request.getSession();
		
		if ("verify".equals(action)) {
			// This OAuth 2.0 access scope allows for full read/write access to the
	        // authenticated user's account.
	        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");

			try {
				// Authorize the request.
	            Credential credential = Auth.authorize(scopes, "createbroadcast");
	            
	            session.setAttribute("credential", credential);

			} catch (Exception e) {
				
			}
		} else if ("create".equals(action)) {
//			String broadcastTitle = "Virtual Business";
			String streamTitle = "Virtual Business Stream Title";
//			String startTime = "2016-12-13T10:00:00.000+08:00";
//			String endTime = "2016-12-14T00:00:00.000+08:00";
			
			String broadcastTitle = request.getParameter("broadcast_title");
			String startDate = request.getParameter("start_date");
			String startHour = request.getParameter("start_hour");
			String startMinute = request.getParameter("start_minute");
			String endDate = request.getParameter("end_date");
			String endHour = request.getParameter("end_hour");
			String endMinute = request.getParameter("end_minute");
			
			String startTime = startDate + "T" + String.format("%02d", Integer.valueOf(startHour)) + ":" + String.format("%02d", Integer.valueOf(startMinute)) + ":00.000+08:00";
			String endTime = endDate + "T" + String.format("%02d", Integer.valueOf(endHour)) + ":" + String.format("%02d", Integer.valueOf(endMinute)) + ":00.000+08:00";
			
			logger.debug("Stream Title:" + broadcastTitle);
			logger.debug("Start Time:" + startTime);
			logger.debug("End Time:" + endTime);
			
			Credential credential = (Credential) session.getAttribute("credential");
			
			String result = createBroadcast.create(broadcastTitle, streamTitle, startTime, endTime, credential);
			
			response.getWriter().write(result.toString());
		}
	}

	public class CreateBroadcast {
		
		/**
	     * Define a global instance of a Youtube object, which will be used
	     * to make YouTube Data API requests.
	     */
	    private YouTube youtube;
	    
	    private String create(String broadcastTitle, String streamTitle, String startTime, String endTime, Credential credential1) {
			// This OAuth 2.0 access scope allows for full read/write access to the
	        // authenticated user's account.
	        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");
	    	
	        try {
	            // Authorize the request.
	            Credential credential = Auth.authorize(scopes, "createbroadcast");

	            // This object is used to make YouTube Data API requests.
	            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
	                    .setApplicationName("youtube-cmdline-createbroadcast-sample").build();

	            // Prompt the user to enter a title for the broadcast.
//	            String title = getBroadcastTitle();
//	            System.out.println("You chose " + title + " for broadcast title.");

	            // Create a snippet with the title and scheduled start and end
	            // times for the broadcast. Currently, those times are hard-coded.
	            LiveBroadcastSnippet broadcastSnippet = new LiveBroadcastSnippet();
//	            broadcastSnippet.setTitle(title);
//	            broadcastSnippet.setScheduledStartTime(new DateTime("2024-01-30T00:00:00.000Z"));
//	            broadcastSnippet.setScheduledEndTime(new DateTime("2024-01-31T00:00:00.000Z"));
	            
	            //eg. "2016-12-12T17:00:00.000+08:00"
	            broadcastSnippet.setTitle(broadcastTitle);
	            broadcastSnippet.setScheduledStartTime(new DateTime(startTime));
	            broadcastSnippet.setScheduledEndTime(new DateTime(endTime));

	            // Set the broadcast's privacy status to "private". See:
	            // https://developers.google.com/youtube/v3/live/docs/liveBroadcasts#status.privacyStatus
	            LiveBroadcastStatus status = new LiveBroadcastStatus();
	            status.setPrivacyStatus("private");

	            LiveBroadcast broadcast = new LiveBroadcast();
	            broadcast.setKind("youtube#liveBroadcast");
	            broadcast.setSnippet(broadcastSnippet);
	            broadcast.setStatus(status);

	            // Construct and execute the API request to insert the broadcast.
	            YouTube.LiveBroadcasts.Insert liveBroadcastInsert =
	                    youtube.liveBroadcasts().insert("snippet,status", broadcast);
	            LiveBroadcast returnedBroadcast = liveBroadcastInsert.execute();

	            // Print information from the API response.
	            logger.debug("\n================== Returned Broadcast ==================\n");
	            logger.debug("  - Id: " + returnedBroadcast.getId());
	            logger.debug("  - Title: " + returnedBroadcast.getSnippet().getTitle());
	            logger.debug("  - Description: " + returnedBroadcast.getSnippet().getDescription());
	            logger.debug("  - Published At: " + returnedBroadcast.getSnippet().getPublishedAt());
	            logger.debug(
	                    "  - Scheduled Start Time: " + returnedBroadcast.getSnippet().getScheduledStartTime());
	            logger.debug(
	                    "  - Scheduled End Time: " + returnedBroadcast.getSnippet().getScheduledEndTime());

	            // Prompt the user to enter a title for the video stream.
//	            title = getStreamTitle();
//	            System.out.println("You chose " + title + " for stream title.");

	            // Create a snippet with the video stream's title.
	            LiveStreamSnippet streamSnippet = new LiveStreamSnippet();
//	            streamSnippet.setTitle(title);
	            streamSnippet.setTitle(streamTitle);

	            // Define the content distribution network settings for the
	            // video stream. The settings specify the stream's format and
	            // ingestion type. See:
	            // https://developers.google.com/youtube/v3/live/docs/liveStreams#cdn
	            CdnSettings cdnSettings = new CdnSettings();
	            cdnSettings.setFormat("1080p");
	            cdnSettings.setIngestionType("rtmp");

	            LiveStream stream = new LiveStream();
	            stream.setKind("youtube#liveStream");
	            stream.setSnippet(streamSnippet);
	            stream.setCdn(cdnSettings);

	            // Construct and execute the API request to insert the stream.
	            YouTube.LiveStreams.Insert liveStreamInsert =
	                    youtube.liveStreams().insert("snippet,cdn", stream);
	            LiveStream returnedStream = liveStreamInsert.execute();

	            // Print information from the API response.
	            logger.debug("\n================== Returned Stream ==================\n");
	            logger.debug("  - Id: " + returnedStream.getId());
	            logger.debug("  - Title: " + returnedStream.getSnippet().getTitle());
	            logger.debug("  - Description: " + returnedStream.getSnippet().getDescription());
	            logger.debug("  - Published At: " + returnedStream.getSnippet().getPublishedAt());

	            // Construct and execute a request to bind the new broadcast
	            // and stream.
	            YouTube.LiveBroadcasts.Bind liveBroadcastBind =
	                    youtube.liveBroadcasts().bind(returnedBroadcast.getId(), "id,contentDetails");
	            liveBroadcastBind.setStreamId(returnedStream.getId());
	            returnedBroadcast = liveBroadcastBind.execute();

	            // Print information from the API response.
	            logger.debug("\n================== Returned Bound Broadcast ==================\n");
	            logger.debug("  - Broadcast Id: " + returnedBroadcast.getId());
	            logger.debug(
	                    "  - Bound Stream Id: " + returnedBroadcast.getContentDetails().getBoundStreamId());
	            
				Map map = new HashMap();
				map.put("success", true);
				map.put("info", "直播排程設定完成!");
				map.put("broadcast_id", returnedBroadcast.getId());
				JSONObject result = new JSONObject(map);
				
				return result.toString();
	        } catch (GoogleJsonResponseException e) {
	        	String info = "GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
	                    + e.getDetails().getMessage();
	        	logger.error(info);
	            e.printStackTrace();
	            
	            Map map = new HashMap();
				map.put("success", false);
				map.put("info", "直播排程設定失敗，請截圖並通知資訊人員。<br/>" + info);
				JSONObject result = new JSONObject(map);
				
	            return result.toString();
	        } catch (IOException e) {
	        	String info = "IOException: " + e.getMessage();
	        	logger.error(info);
	            e.printStackTrace();
	            
	            Map map = new HashMap();
				map.put("success", false);
				map.put("info", info);
				JSONObject result = new JSONObject(map);
				
	            return result.toString();
	        } catch (Throwable t) {
	        	String info = "Throwable: " + t.getMessage();
	        	logger.error(info);
	            t.printStackTrace();

	            Map map = new HashMap();
				map.put("success", false);
				map.put("info", info);
				JSONObject result = new JSONObject(map);
				
	            return result.toString();
	        }
	    }

	    /*
	     * Prompt the user to enter a title for a broadcast.
	     */
	    private String getBroadcastTitle() throws IOException {
	
	        String title = "";
	
	        System.out.print("Please enter a broadcast title: ");
	        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
	        title = bReader.readLine();
	
	        if (title.length() < 1) {
	            // Use "New Broadcast" as the default title.
	            title = "New Broadcast";
	        }
	        return title;
	    }
	
	    /*
	     * Prompt the user to enter a title for a stream.
	     */
	    private String getStreamTitle() throws IOException {
	
	        String title = "";
	
	        System.out.print("Please enter a stream title: ");
	        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
	        title = bReader.readLine();
	
	        if (title.length() < 1) {
	            // Use "New Stream" as the default title.
	            title = "New Stream";
	        }
	        return title;
	    }
	}
}
