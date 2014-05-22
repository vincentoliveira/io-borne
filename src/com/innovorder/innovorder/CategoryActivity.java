package com.innovorder.innovorder;

import java.util.List;

import com.innovorder.innovorder.adapter.ItemListAdapter;
import com.innovorder.innovorder.listener.AddItemToChartListener;
import com.innovorder.innovorder.model.CarteItem;
import com.innovorder.innovorder.storage.CarteItemStorage;
import com.innovorder.innovorder.utils.PriceFormatter;
import com.innovorder.innovorder.webservice.DownloadImageTask;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryActivity extends AbstractCarteActivity implements OnItemClickListener, OnClickListener {

	private View detailsView;
	private GridView gridView;
	private ListAdapter gridViewAdapter;
	private Button closeButton;
	private Button orderButton;
	private List<CarteItem> children;
	private CarteItem child;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		CarteItemStorage itemStorage = new CarteItemStorage(this);
		List<CarteItem> categories = itemStorage.getMainCategories();
		setLeftMenu(categories);

		String categoryName;
		long categoryId = 0;
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if (extras == null) {
				categoryName = null;
			} else {
				categoryName = extras.getString("category_name");
				categoryId = extras.getLong("category_id");
			}
		} else {
			categoryName = (String) savedInstanceState.getSerializable("category_name");
			categoryId = (Long) savedInstanceState.getSerializable("category_id");
		}

		children = itemStorage.getItemsByParentId(categoryId);
		TextView titleTextView = (TextView) findViewById(R.id.categoryTextView);
		titleTextView.setText(categoryName);
		
		Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Aachen_bt.ttf");
		titleTextView.setTypeface(custom_font);

		detailsView = (View) findViewById(R.id.dish_container);
		gridView = (GridView) findViewById(R.id.dishesGridView);

		detailsView.setVisibility(View.GONE);
		gridViewAdapter = new ItemListAdapter(this, children, R.layout.dish_item);
		gridView.setAdapter(gridViewAdapter);
		gridView.setOnItemClickListener(this);

		closeButton = (Button) findViewById(R.id.closeButton);
		closeButton.setOnClickListener(this);

		orderButton = (Button) findViewById(R.id.commanderButton);
		orderButton.setOnClickListener(this);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent.getId() == gridView.getId()) {
			child = (CarteItem) gridViewAdapter.getItem(position);
			TextView titleTextView = (TextView) detailsView.findViewById(R.id.dishTitleTextView);
			TextView descTextView = (TextView) detailsView.findViewById(R.id.dishDescriptionTextView);
			TextView dishPriceTextView = (TextView) detailsView.findViewById(R.id.dishPriceTextView);
			ImageView imageView = (ImageView) detailsView.findViewById(R.id.dishImageView);

			titleTextView.setText(child.getName());
			descTextView.setText(child.getDescription());
			dishPriceTextView.setText(PriceFormatter.format(child.getPrice()));

			imageView.setImageResource(R.drawable.logo);
			if (child.getMediaUrl() != null) {
				String mediaBaseUrl = getString(R.string.ws_base) + getString(R.string.ws_media);
				new DownloadImageTask(imageView).execute(mediaBaseUrl + child.getMediaUrl());
			}
			orderButton.setTag(child.getId());
			AddItemToChartListener listener = new AddItemToChartListener(this);
			orderButton.setOnClickListener(listener);

			detailsView.setVisibility(View.VISIBLE);
		} else {
			super.onItemClick(parent, view, position, id);
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == closeButton.getId()) {
			detailsView.setVisibility(View.GONE);
			child = null;
		} else if (view.getId() == orderButton.getId()) {
			detailsView.setVisibility(View.GONE);
		}
	}
}
