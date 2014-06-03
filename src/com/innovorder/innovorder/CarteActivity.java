package com.innovorder.innovorder;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.innovorder.innovorder.Toast.CustomToast;
import com.innovorder.innovorder.adapter.CategoriesListAdapter;
import com.innovorder.innovorder.adapter.ItemListAdapter;
import com.innovorder.innovorder.listener.CategoryListListener;
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

		carteItemStorage = new CarteItemStorage(this);

		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null && extras.getBoolean("refresh")) {
				getCarteItems();
			}
		}

		List<CarteItem> categories = carteItemStorage.getMainCategories();
		setLeftMenu(categories);

		// mDrawerLayout.openDrawer(mDrawerList);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Aachen_bt.ttf");
		TextView promotedTextView = (TextView) findViewById(R.id.promotedTextView);
		TextView laCarteTextView = (TextView) findViewById(R.id.laCarteTextView);
		promotedTextView.setTypeface(custom_font);
		laCarteTextView.setTypeface(custom_font);

		setPromotedProduct();
		setCategories();
	}

	public void setPromotedProduct() {
		CarteItem item1 = carteItemStorage.find(32);
		CarteItem item2 = carteItemStorage.find(31);
		CarteItem item3 = carteItemStorage.find(39);
		ArrayList<CarteItem> list = new ArrayList<CarteItem>();
		if (item1 != null) {
			list.add(item1);
		}
		if (item2 != null) {
			list.add(item2);
		}
		if (item3 != null) {
			list.add(item3);
		}

		GridView gridView = (GridView) findViewById(R.id.promotedGridView);

		ItemListAdapter gridViewAdapter = new ItemListAdapter(this, list, R.layout.dish_item);
		gridView.setAdapter(gridViewAdapter);
	}
	
	public void setCategories() {
		GridView categoriesGridView = (GridView) findViewById(R.id.categoriesGridView);

		List<CarteItem> categories = carteItemStorage.getItemsByParentId(0);
		CategoriesListAdapter gridViewAdapter = new CategoriesListAdapter(this, categories, R.layout.category_item);
		categoriesGridView.setAdapter(gridViewAdapter);
		OnItemClickListener categoryListListener = new CategoryListListener(this, gridViewAdapter);
		categoriesGridView.setOnItemClickListener(categoryListListener);
	}

	@Override
	public void onBackPressed() {
		cancelOrder();
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
				CustomToast.makeText(this, R.string.error_unknown, Toast.LENGTH_LONG).show();
				return;
			} else {
				carteItemStorage.clearAll();
				for (CarteItem item : items) {
					carteItemStorage.addItem(item);
				}
			}

			List<CarteItem> categories = carteItemStorage.getItemsByParentId(0);
			setLeftMenu(categories);
			setCategories();
		} else {
			super.onWebserviceCallFinished(response, tag);
		}
	}
}
