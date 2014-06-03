package com.innovorder.innovorder.adapter;

import java.util.ArrayList;
import java.util.List;

import com.innovorder.innovorder.R;
import com.innovorder.innovorder.listener.AddItemToCartListener;
import com.innovorder.innovorder.listener.RemoveItemFromChartListener;
import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.model.CarteItem;
import com.innovorder.innovorder.utils.PriceFormatter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ReceiptListAdapter extends BaseAdapter {
	private Context context;
	private Cart cart;
	private AddItemToCartListener addListener;
	private RemoveItemFromChartListener removeListener;
	private List<Pair<CarteItem, Integer>> values;

	public ReceiptListAdapter(Context context, Cart cart) {
		this.context = context;
		this.cart = cart;

		addListener = new AddItemToCartListener(context);
		addListener.setAdapter(this);
		removeListener = new RemoveItemFromChartListener(context);
		removeListener.setAdapter(this);

		parseItems();
	}

	@SuppressLint("UseValueOf")
	@SuppressWarnings("unchecked")
	public void parseItems() {
		if (values != null) {
			values.clear();
		}

		values = new ArrayList<Pair<CarteItem, Integer>>();
		ArrayList<CarteItem> ids = new ArrayList<CarteItem>();
		ArrayList<CarteItem> copyitems = (ArrayList<CarteItem>) cart.getItems().clone();
		for (CarteItem item : copyitems) {
			if (ids.contains(item)) {
				continue;
			}

			int value = 0;
			for (CarteItem i : cart.getItems()) {
				if (i.getId() == item.getId()) {
					value = value + 1;
				}
			}
			values.add(Pair.create(item, new Integer(value)));
			ids.add(item);
		}
	}

	@Override
	public void notifyDataSetChanged() {
		parseItems();
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public Object getItem(int position) {
		return values.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = new View(context);
		view = inflater.inflate(R.layout.cart_item, null);

		TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
		TextView priceTextView = (TextView) view.findViewById(R.id.priceTextView);
		TextView countTextView = (TextView) view.findViewById(R.id.countTextView);
		Button removeButton = (Button) view.findViewById(R.id.removeButton);
		Button addButton = (Button) view.findViewById(R.id.addButton);

		Pair<CarteItem, Integer> pair = values.get(position);
		final CarteItem item = pair.first;
		final Integer count = pair.second;

		nameTextView.setTypeface(Typeface.DEFAULT);
		nameTextView.setText(item.getName());
		priceTextView.setText(PriceFormatter.format(item.getPrice()));
		countTextView.setText(count.toString());

		removeButton.setTag(item.getId());
		removeButton.setOnClickListener(removeListener);
		removeButton.setVisibility(View.VISIBLE);

		addButton.setTag(item.getId());
		addButton.setOnClickListener(addListener);
		addButton.setVisibility(View.VISIBLE);

		return view;
	}
}
