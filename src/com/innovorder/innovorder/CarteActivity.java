package com.innovorder.innovorder;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.innovorder.innovorder.model.CarteItem;
import com.innovorder.innovorder.parser.CarteItemParser;
import com.innovorder.innovorder.storage.CarteItemStorage;
import com.innovorder.innovorder.storage.LoginManager;
import com.innovorder.innovorder.webservice.GetCarteItemsCall;
import com.innovorder.innovorder.webservice.WebserviceCallListener;

public class CarteActivity extends AbstractCarteActivity implements WebserviceCallListener, OnItemClickListener {	
	private CarteItemStorage carteItemStorage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carte);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle("MENU");
		
		carteItemStorage = new CarteItemStorage(this);
		

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null && extras.getBoolean("refresh")) {
				getCarteItems();
			}
		}
		
		List<CarteItem> categories = carteItemStorage.getMainCategories();
		setLeftMenu(categories);
	}

	private void getCarteItems() {
		// get username/password
		LoginManager loginManager = new LoginManager(this);
		if (!loginManager.isAthentificated()) {
			finish();
			return;
		}
		String restaurant = loginManager.getRestaurant();
		String password = loginManager.getPassword();

		GetCarteItemsCall getCarteItems = new GetCarteItemsCall();
		getCarteItems.setParentActivity(this);
		getCarteItems.execute(restaurant, password);
	}
	

	@Override
	public void onWebserviceCallFinished(String response, String tag) {

		if (tag.equals("getCarteItems")) {
			ArrayList<CarteItem> items = null;
			if (response != null) {
				items = CarteItemParser.parse(response);
			}
			
			if (items == null) {
				Toast.makeText(this, "Une erreur s'est produite", Toast.LENGTH_LONG).show();
				return;
			} else {
				carteItemStorage.clearAll();
				for (CarteItem item : items) {
					carteItemStorage.addItem(item);
				}
			}
			
			List<CarteItem> categories = carteItemStorage.getItemsByParentId(0);
			setLeftMenu(categories);
		}
	}
}
