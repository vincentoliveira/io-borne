package com.innovorder.innovorder.parser;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class LydiaPaymentParser {

	private static final String TAG_ERROR = "error";
	private static final String TAG_TRANSACTION_ID = "transaction_identifier";
	private static final String TAG_MESSAGE = "message";

	private String transactionId;
	private String message;
	private int error = -1;

	public boolean parse(String jsonStr) {

		// try parse the string to a JSON object
		try {
			JSONObject json = new JSONObject(jsonStr);

			error = json.getInt(TAG_ERROR);
			if (json.has(TAG_TRANSACTION_ID)) {
				transactionId = json.getString(TAG_TRANSACTION_ID);
			}
			if (json.has(TAG_MESSAGE)) {
				message = json.getString(TAG_MESSAGE);
			}
			
			return true;
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing Lydia payment response " + e.toString());

			message = "BAD RESULT:" + jsonStr;
			error = -1;
			return false;
		}
	}

	public int getError() {
		return error;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getMessage() {
		return message;
	}

}
