package com.innovorder.innovorder.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.innovorder.innovorder.model.CarteItem;

import android.util.Log;

public class CarteItemParser {
	private static final String TAG_CARTE_ITEM = "carte";

	private static final String TAG_ID = "id";
	private static final String TAG_TYPE = "type";
	private static final String TAG_NAME = "name";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_PRICE = "price";
	private static final String TAG_VAT = "vat";
	private static final String TAG_MEDIA = "media";
	private static final String TAG_MEDIA_URL = "path";
	private static final String TAG_CHILDREN = "children";

	public static ArrayList<CarteItem> parse(String jsonStr) {

		// try parse the string to a JSON object
		try {
			ArrayList<CarteItem> carteItemList = new ArrayList<CarteItem>();
			JSONObject json = new JSONObject(jsonStr);
			
			JSONArray jsonItemList = json.getJSONArray(TAG_CARTE_ITEM);

			// looping through All Contacts
			for (int i = 0; i < jsonItemList.length(); i++) {
				JSONObject jsonItem = jsonItemList.getJSONObject(i);

				CarteItem item = parseCarteItem(jsonItem, null);
				if (item != null) {
					carteItemList.add(item);
				}
			}

			return carteItemList;
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing carte item " + e.toString());

			return null;
		}
	}
	
	private static CarteItem parseCarteItem(JSONObject jsonItem, CarteItem parent)
	{

		try {
			CarteItem item = new CarteItem();
			item.setType(jsonItem.getString(TAG_TYPE));
			item.setId(jsonItem.getInt(TAG_ID));
			item.setName(jsonItem.getString(TAG_NAME));
			item.setDescription(jsonItem.getString(TAG_DESCRIPTION));
			if (jsonItem.has(TAG_PRICE)) {
				item.setPrice(jsonItem.getDouble(TAG_PRICE));
			}
			if (jsonItem.has(TAG_VAT)) {
				item.setVat(jsonItem.getDouble(TAG_VAT));
			}
			
			if (parent != null) {
				item.setParentId(parent.getId());
			} else {
				item.setParentId(0);
			}
			
			if (jsonItem.has(TAG_MEDIA)) {
				JSONObject jsonMedia = jsonItem.getJSONObject(TAG_MEDIA);
				item.setMediaUrl(jsonMedia.getString(TAG_MEDIA_URL));
			}
			
			if (jsonItem.has(TAG_CHILDREN)) {
				JSONArray jsonChildrenList = jsonItem.getJSONArray(TAG_CHILDREN);
				// looping through All Contacts
				for (int i = 0; i < jsonChildrenList.length(); i++) {
					JSONObject jsonChild = jsonChildrenList.getJSONObject(i);
	
					CarteItem child = parseCarteItem(jsonChild, item);
					if (child != null) {
						item.addChild(child);
					}
				}
			}
			
			return item;
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing item " + e.toString());
		}
		
		return null;
	}
}
