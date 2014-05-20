package com.innovorder.innovorder.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import com.innovorder.innovorder.R;

public class CheckLoginCall extends WebserviceCall {
	private ProgressDialog dialog;
	private Context context;
	private String wsCheckUrl;

	public void setParentActivity(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		if (context != null) {
			dialog = new ProgressDialog(context);
			dialog.setMessage("Login...");
			dialog.show();

			wsCheckUrl = context.getString(R.string.ws_base) + context.getString(R.string.ws_check_login);
		}
	}

	/**
	 * @return String encoded password
	 */
	@Override
	protected String doInBackground(String... params) {
		if (wsCheckUrl == null || params.length < 2) {
			return null;
		}

		String restaurant = params[0];
		String password = params[1];
		
		WsseToken wsseToken = new WsseToken(restaurant, password);
		return getContent(wsCheckUrl, wsseToken);

	}

	@Override
	protected void onPostExecute(String response) {
		dialog.dismiss();
		
		if (context instanceof WebserviceCallListener) {
			((WebserviceCallListener) context).onWebserviceCallFinished(response, "login");
		}
	}
}
