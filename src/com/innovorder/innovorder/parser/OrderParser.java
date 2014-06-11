package com.innovorder.innovorder.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.innovorder.innovorder.model.Cart;

import android.util.Log;

public class OrderParser {
	private static final String TAG_ORDER_ITEM = "order";

	private static final String TAG_ID = "id";
	private static final String TAG_PRICE = "total_price";

	public static boolean parse(String jsonStr) {

		// try parse the string to a JSON object
		try {
			JSONObject json = new JSONObject(jsonStr);
			JSONObject jsonOrder = json.getJSONObject(TAG_ORDER_ITEM);

			int serverId = jsonOrder.getInt(TAG_ID);
			double serverPrice = jsonOrder.getDouble(TAG_PRICE);
			
			Cart cart = Cart.getInstance();
			cart.setServerId(serverId);
			cart.setServerPrice(serverPrice);

			return true;
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing order " + e.toString());

			return false;
		}
	}
	
}
