package com.innovorder.innovorder.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.innovorder.innovorder.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class GetCarteItemsCall extends AsyncTask<String, String, String> {
	private ProgressDialog dialog;
	private Context context;

	private String wsUrl;

	public void setParentActivity(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		if (context != null) {
			dialog = new ProgressDialog(context);
			dialog.setMessage(context.getString(R.string.msg_get_menu));
			dialog.show();

			wsUrl = context.getString(R.string.ws_base) + context.getString(R.string.ws_get_carte_items);
		}
	}

	/**
	 * @return String encoded password
	 */
	@Override
	protected String doInBackground(String... params) {
		if (wsUrl == null ||  params.length < 2) {
			return null;
		}

		String restaurant = params[0];
		String password = params[1];
		WsseToken wsseToken = new WsseToken(restaurant, password);
		
		return getContent(wsUrl, wsseToken);
	}

	@Override
	protected void onPostExecute(String response) {
		dialog.dismiss();
		
		if (context instanceof WebserviceCallListener) {
			((WebserviceCallListener) context).onWebserviceCallFinished(response, "getCarteItems");
		}
	}
	
	protected String getContent(String strurl, WsseToken wsseToken) 
	{
		BufferedReader reader = null;
		String content = null;
		try {
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
		} catch (Exception ex) {
			Log.d("WebserviceError", ex.getMessage());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception ex) {
				Log.d("WebserviceError", ex.getMessage());
			}
		}

		Log.i("ws", content != null ? content : "null");
		return content;
	}
}
