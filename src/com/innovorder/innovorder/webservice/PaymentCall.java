package com.innovorder.innovorder.webservice;


import java.text.SimpleDateFormat;

import com.innovorder.innovorder.R;
import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.model.Payment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;

public class PaymentCall extends WebserviceCall {
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
			dialog.setMessage(context.getString(R.string.msg_payment_in_progress));
			dialog.show();
		}
	}

	/**
	 * @return String encoded password
	 */
	@SuppressLint("SimpleDateFormat")
	@Override
	protected String doInBackground(String... params) {
		Cart cart = Cart.getInstance();
		Payment payment = cart.getPayment();
		if (cart.getServerId() == 0 || payment == null || params.length < 2) {
			return null;
		}
		
		wsUrl = context.getString(R.string.ws_base) + context.getString(R.string.ws_payment, cart.getServerId());

		String restaurant = params[0];
		String password = params[1];
		WsseToken wsseToken = new WsseToken(restaurant, password);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append("{\"date\":\"")
		.append(formatter.format(payment.getDate()))
		.append("\",\"amount\":")
		.append(payment.getAmount())
		.append(",\"transaction_id\":\"")
		.append(payment.getTransactionId())
		.append("\",\"type\":\"")
		.append(payment.getType())
		.append("\",\"status\":\"")
		.append(payment.getStatus())
		.append("\",\"comments\":\"")
		.append(payment.getComments())
		.append("\"}");
		
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
			((WebserviceCallListener) context).onWebserviceCallFinished(response, "payment");
		}
	}
}
