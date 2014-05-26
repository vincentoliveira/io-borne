package com.innovorder.innovorder.webservice;

import java.text.SimpleDateFormat;

import com.innovorder.innovorder.R;
import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.model.CarteItem;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;

public class OrderCall extends WebserviceCall {
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
			dialog.setMessage(context.getString(R.string.msg_order_in_progress));
			dialog.show();

			wsUrl = context.getString(R.string.ws_base) + context.getString(R.string.ws_order);
		}
	}

	/**
	 * @return String encoded password
	 */
	@SuppressLint("SimpleDateFormat")
	@Override
	protected String doInBackground(String... params) {
		if (wsUrl == null ||  params.length < 2) {
			return null;
		}

		String restaurant = params[0];
		String password = params[1];
		WsseToken wsseToken = new WsseToken(restaurant, password);

		Cart cart = Cart.getInstance();

		StringBuilder itemsBuilder = new StringBuilder();
		for (CarteItem item : cart.getItems()) {
			if (itemsBuilder.length() != 0) {
				itemsBuilder.append(",");
			}
			
			itemsBuilder.append("{\"id\":")
			.append(item.getId())
			.append("}");
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append("{\"name\":\"")
		.append(cart.getOrderName())
		.append("\",\"start_date\":\"")
		.append(formatter.format(cart.getStartOrderDate()))
		.append("\",\"items\":[")
		.append(itemsBuilder).
		append("]}");
		
		this.method = "POST";
		this.postData = dataBuilder.toString();
		
		return getContent(wsUrl, wsseToken);
	}

	@Override
	protected void onPostExecute(String response) {
		if (dialog != null) {
			dialog.dismiss();
		}
		
		if (context instanceof WebserviceCallListener) {
			((WebserviceCallListener) context).onWebserviceCallFinished(response, "order");
		}
	}
}
