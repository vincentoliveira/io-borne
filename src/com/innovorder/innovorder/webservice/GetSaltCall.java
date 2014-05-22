package com.innovorder.innovorder.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import com.innovorder.innovorder.R;

public class GetSaltCall extends WebserviceCall {
	private ProgressDialog dialog;
	private Context context;
	private String wsSaltUrl;

	public void setParentActivity(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		if (context != null) {
			dialog = new ProgressDialog(context);
			dialog.setMessage("Login...");
			dialog.show();

			wsSaltUrl = context.getString(R.string.ws_base) + context.getString(R.string.ws_get_salt);
		}
	}

	/**
	 * @return String encoded password
	 */
	@Override
	protected String doInBackground(String... params) {
		if (wsSaltUrl == null || params.length == 0) {
			return null;
		}

		String username = params[0];
		
		wsSaltUrl = wsSaltUrl.replace("%username%", username);
		return getContent(wsSaltUrl, null);
	}

	@Override
	protected void onPostExecute(String response) {
		dialog.dismiss();
		
		if (context instanceof WebserviceCallListener) {
			((WebserviceCallListener) context).onWebserviceCallFinished(response, "salt");
		}
	}
}
