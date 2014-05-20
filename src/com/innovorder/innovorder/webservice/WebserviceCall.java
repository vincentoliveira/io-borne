package com.innovorder.innovorder.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public abstract class WebserviceCall extends AsyncTask<String, String, String> {
	
	protected String getContent(String strurl, WsseToken wsseToken) 
	{
		BufferedReader reader = null;
		String content = null;
		try {
			Log.i("ws", strurl);
			URL url = new URL(strurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			
			if (wsseToken != null) {
				conn.setRequestProperty(WsseToken.HEADER_WSSE, wsseToken.getWsseHeader());
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
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception ex) {
				//Log.e("WebserviceError", ex.getMessage());
			}
		}
		
		return content;
	}
}
