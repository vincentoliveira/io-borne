package com.innovorder.innovorder.webservice;

import com.innovorder.innovorder.R;

import android.app.ProgressDialog;
import android.content.Context;

public class GetCarteItemsCall extends WebserviceCall {
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
}
