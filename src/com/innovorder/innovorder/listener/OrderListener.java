package com.innovorder.innovorder.listener;

import com.innovorder.innovorder.R;
import com.innovorder.innovorder.Toast.CustomToast;
import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.storage.LoginManager;
import com.innovorder.innovorder.webservice.OrderCall;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

public class OrderListener implements android.content.DialogInterface.OnClickListener, android.view.View.OnClickListener {

	private Context context;

	public OrderListener(Context context) {
		this.context = context;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		order();
	}

	@Override
	public void onClick(View view) {
		order();
	}
	
	private void order() {
		Cart cart = Cart.getInstance();

		LoginManager loginManager = new LoginManager(context);
		String restaurant = loginManager.getRestaurant();
		String password = loginManager.getPassword();
		
		if (cart.getItems().isEmpty()) {
			CustomToast.makeText(context, R.string.error_empty_cart, Toast.LENGTH_LONG, R.drawable.icon_warning).show();
			return;
		}

		OrderCall call = new OrderCall();
		call.setParentActivity(context);
		call.execute(restaurant, password);
	}

}
