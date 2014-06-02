package com.innovorder.innovorder.listener;

import com.innovorder.innovorder.AbstractCarteActivity;
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
import android.widget.TextView;
import android.widget.Toast;

public class AddItemToChartListener implements OnClickListener {

	private Context context;
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
		notify(item);
		
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		
		if (context instanceof AbstractCarteActivity) {
			((AbstractCarteActivity) context).reloadMenu();
		}
	}

	private CarteItem addItem(long id) {
		CarteItemStorage carteItemStorage = new CarteItemStorage(context);
		CarteItem item = carteItemStorage.find(id);

		Cart cart = Cart.getInstance();
		cart.addItem(item);
		
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
	
	private void notify(CarteItem item) {
		String text = "";
		int nb = countItem(item.getId());
		if (nb == 1) {
			text = item.getName() + " ajouté(e)";
		} else {
			text = nb + " " + item.getName() + " ajouté(e)s";
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
	}

}
