package com.innovorder.innovorder.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.NameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public abstract class WebserviceCall extends AsyncTask<String, String, String> {
	protected String method = "GET";
	protected String postData = "";
	protected String contentType = "application/json";
	
	
	protected String getContent(String strurl, WsseToken wsseToken) 
	{
		BufferedReader reader = null;
		String content = null;
		try {
			Log.i("ws", this.method + ": " + strurl);
			URL url = new URL(strurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);
			
			if (!contentType.isEmpty()) {
				conn.setRequestProperty("Content-Type", contentType);
			}
			
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			
			if (wsseToken != null) {
				conn.setRequestProperty(WsseToken.HEADER_WSSE, wsseToken.getWsseHeader());
			}
			
			if (!postData.isEmpty()) {
				Log.i("POST DATA", postData);
				OutputStream os = conn.getOutputStream();
				os.write(postData.getBytes("UTF-8"));
				os.flush();
			}

			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = "";

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			content = sb.toString();
			Log.i("ws", content);
		} catch (Exception ex) {
			Log.e("WebserviceError", ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception ex) {
			}
		}
		
		return content;
	}

	/**
	 * Get query string from param list	
	 * 
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	protected String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;

	    for (NameValuePair pair : params)
	    {
	        if (first) {
	            first = false;
	        }
	        else {
	            result.append("&");
	        }
	        
	        result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
	        result.append("=");
	        result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
	    }

	    return result.toString();
	}
}
