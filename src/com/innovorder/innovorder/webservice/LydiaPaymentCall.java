package com.innovorder.innovorder.webservice;


import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.innovorder.innovorder.R;
import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.model.Payment;

import android.app.ProgressDialog;
import android.content.Context;

public class LydiaPaymentCall extends WebserviceCall {
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
			
			wsUrl = context.getString(R.string.ws_lydia_payment);
		}
	}

	@Override
	protected String doInBackground(String... params) {
		Cart cart = Cart.getInstance();
		Payment payment = cart.getPayment();
		if (cart.getServerId() == 0 || payment.getAmount() <= 0 || params.length < 1) {
			return null;
		}
		
		String data = params[0];
		
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator('.');
		NumberFormat formatter = new DecimalFormat("#0.00", otherSymbols);
		formatter.setGroupingUsed(false);

		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair("phone", context.getString(R.string.lydia_phone)));
		postParams.add(new BasicNameValuePair("auth_token", context.getString(R.string.lydia_auth_token)));
		postParams.add(new BasicNameValuePair("vendor_token", context.getString(R.string.lydia_vendor_token)));
		postParams.add(new BasicNameValuePair("provider_token", context.getString(R.string.lydia_provider_token)));
		postParams.add(new BasicNameValuePair("paymentData", data));
		postParams.add(new BasicNameValuePair("amount", formatter.format(payment.getAmount())));
		postParams.add(new BasicNameValuePair("currency", "EUR"));
		postParams.add(new BasicNameValuePair("order_id", "io-" + cart.getServerId()));
		
		this.contentType = "";
		this.method = "POST";
		try {
			this.postData = this.getQuery(postParams);
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
			return null;
		}
		
		return getContent(wsUrl, null);
	}


	@Override
	protected void onPostExecute(String response) {
		if (dialog != null) {
			dialog.dismiss();
		}
		
		if (context instanceof WebserviceCallListener) {
			((WebserviceCallListener) context).onWebserviceCallFinished(response, "lydiaPayment");
		}
	}
}
