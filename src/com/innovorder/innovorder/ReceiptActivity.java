package com.innovorder.innovorder;

import java.util.List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.innovorder.innovorder.adapter.CategoriesListAdapter;
import com.innovorder.innovorder.adapter.ReceiptListAdapter;
import com.innovorder.innovorder.listener.CategoryListListener;
import com.innovorder.innovorder.listener.OrderListener;
import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.model.CarteItem;
import com.innovorder.innovorder.storage.CarteItemStorage;
import com.innovorder.innovorder.utils.PriceFormatter;
import com.innovorder.innovorder.webservice.WebserviceCallListener;

public class ReceiptActivity extends AbstractCarteActivity implements WebserviceCallListener, OnItemClickListener {
	private CarteItemStorage carteItemStorage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		carteItemStorage = new CarteItemStorage(this);

		List<CarteItem> categories = carteItemStorage.getMainCategories();
		setLeftMenu(categories);
		setCategories();

		// mDrawerLayout.openDrawer(mDrawerList);

		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Aachen_bt.ttf");
		TextView receiptTextView = (TextView) findViewById(R.id.receiptTextView);
		TextView laCarteTextView = (TextView) findViewById(R.id.laCarteTextView);
		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		Button confirmButton = (Button) findViewById(R.id.confirmButton);
		receiptTextView.setTypeface(custom_font);
		laCarteTextView.setTypeface(custom_font);
		cancelButton.setTypeface(custom_font);
		confirmButton.setTypeface(custom_font);
		
		confirmButton.setOnClickListener(new OrderListener(this));
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ReceiptActivity.this.cancelOrder();
			}
		});

		Cart cart = Cart.getInstance();
		TextView totalPriceTextView = (TextView) findViewById(R.id.totalPriceTextView);
		totalPriceTextView.setText(PriceFormatter.format(cart.getTotal()));

		ListAdapter adapter = new ReceiptListAdapter(this, cart);
		ListView receiptListView = (ListView) findViewById(R.id.receiptListView);
		receiptListView.setAdapter(adapter);

		StringBuilder title = new StringBuilder();
		title.append(getString(R.string.receipt_title)).append(" (").append(cart.getOrderName()).append(")");

		receiptTextView.setText(title.toString());

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
	public void onItemAdd() {
		ListView receiptListView = (ListView) findViewById(R.id.receiptListView);
		receiptListView.invalidate();
	}
}
