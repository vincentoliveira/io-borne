package com.innovorder.innovorder.adapter;

import java.util.ArrayList;
import java.util.List;

import com.innovorder.innovorder.R;
import com.innovorder.innovorder.listener.AddItemToChartListener;
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

public class CartListAdapter extends BaseAdapter {
	private Context context;
	private Cart cart;
	private AddItemToChartListener addListener;
	private RemoveItemFromChartListener removeListener;
	private List<Pair<CarteItem, Integer>> values;

	public CartListAdapter(Context context, Cart cart) {
		this.context = context;
		this.cart = cart;
		
		addListener = new AddItemToChartListener(context);
		addListener.setAdapter(this);
		removeListener = new RemoveItemFromChartListener(context);
		removeListener.setAdapter(this);
		
		parseItems();
	}
	
	@SuppressLint("UseValueOf")
	@SuppressWarnings("unchecked")
	public void parseItems()
	{
		if (values != null) {
			values.clear();
		}
		
		values = new ArrayList<Pair<CarteItem,Integer>>();
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
		return values.size() + 2;
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

		View view;
		if (convertView == null) {
			view = new View(context);
			view = inflater.inflate(R.layout.cart_item, null);
		} else {
			view = (View) convertView;
		}

		TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
		TextView priceTextView = (TextView) view.findViewById(R.id.priceTextView);
		TextView countTextView = (TextView) view.findViewById(R.id.countTextView);
		Button removeButton = (Button) view.findViewById(R.id.removeButton);
		Button addButton = (Button) view.findViewById(R.id.addButton);
		
		if (position == getCount() - 2) { // empty line
			nameTextView.setText("");
			countTextView.setText("");
			priceTextView.setText("");
			removeButton.setVisibility(View.INVISIBLE);
			addButton.setVisibility(View.INVISIBLE);
		} else if (position == getCount() - 1) { // total
			nameTextView.setText("Total");
			nameTextView.setTypeface(Typeface.DEFAULT_BOLD);
			priceTextView.setText(PriceFormatter.format(cart.getTotal()));
			priceTextView.setTypeface(Typeface.DEFAULT_BOLD);
			countTextView.setText("");
			removeButton.setVisibility(View.INVISIBLE);
			addButton.setVisibility(View.INVISIBLE);
		} else {
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
		}
		
		return view;
	}
}
