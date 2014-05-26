package com.innovorder.innovorder.listener;

import java.util.ArrayList;

import com.innovorder.innovorder.R;
import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.model.CarteItem;
import com.innovorder.innovorder.storage.CarteItemStorage;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddItemToChartListener implements OnClickListener {

	private Context context;
	private ArrayList<View> views = new ArrayList<View>();
	private BaseAdapter adapter;

	public AddItemToChartListener(Context context) {
		this.context = context;
	}
	
	public void setAdapter(BaseAdapter adapter)
	{
		this.adapter = adapter;
	}

	@Override
	public void onClick(View v) {
		CarteItem item = addItem((Long) v.getTag());
		
		String text = "";
		int nb = countItem((Long) v.getTag());
		if (nb == 1) {
			text = "L'article \"" + item.getName() + "\" a été ajouté";
		} else {
			text = nb + " articles \"" + item.getName() + "\" ont été ajouté";
		}
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.cart_toast,null);

		TextView textView = (TextView) layout.findViewById(R.id.text);
		textView.setText(text);
		
		Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Aachen_bt.ttf");
		textView.setTypeface(custom_font);
		
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 76);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
		
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}
	
	public void clearViews() {
		for (View v : views) {
			if (v instanceof Button) {
				((Button) v).setText(context.getString(R.string.btn_ajouter_panier));
				((Button) v).setTextColor(context.getResources().getColor(R.color.numa_pink));
				((Button) v).setTextSize(30);
			} else if (v instanceof TextView) {
				((TextView) v).setText(context.getString(R.string.btn_ajouter_panier));
				((TextView) v).setTextColor(context.getResources().getColor(R.color.numa_pink));
				((TextView) v).setTextSize(30);
				v.setBackgroundResource(android.R.color.white);
			}
		}
		
		views.clear();
	}

	private CarteItem addItem(long id) {
		CarteItemStorage carteItemStorage = new CarteItemStorage(context);
		CarteItem item = carteItemStorage.find(id);

		Cart cart = Cart.getInstance();
		cart.addItem(item);
		
		this.clearViews();
		
		return item;
	}

	private int countItem(long id) {
		int count = 0;
		
		Cart cart = Cart.getInstance();
		for (CarteItem item1 : cart.getItems()) {
			if (item1.getId() == id) {
				count++;
			}
		}
		
		return count;
	}

}
