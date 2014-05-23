package com.innovorder.innovorder.listener;

import java.util.Date;

import com.innovorder.innovorder.model.Cart;
import com.innovorder.innovorder.storage.LoginManager;
import com.innovorder.innovorder.webservice.OrderCall;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class OrderListener implements OnClickListener {

	private Context context;

	public OrderListener(Context context) {
		this.context = context;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		
		Cart cart = Cart.getInstance();
		cart.setEndOrderDate(new Date());

		LoginManager loginManager = new LoginManager(context);
		String restaurant = loginManager.getRestaurant();
		String password = loginManager.getPassword();

		OrderCall call = new OrderCall();
		call.setParentActivity(context);
		call.execute(restaurant, password);
	}

}
