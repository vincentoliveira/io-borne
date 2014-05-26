package com.innovorder.innovorder;

import com.innovorder.innovorder.adapter.CartListAdapter;
import com.innovorder.innovorder.listener.OrderListener;
import com.innovorder.innovorder.model.Cart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ListAdapter;

public class ChartDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		Cart cart = Cart.getInstance();
		ListAdapter adapter = new CartListAdapter(getActivity(), cart);
		
		StringBuilder title = new StringBuilder();
		title.append(getString(R.string.mon_panier))
		.append(" (")
		.append(cart.getOrderName())
		.append(")");
		
		builder.setTitle(title.toString())
		.setPositiveButton(R.string.btn_valider_commande, new OrderListener(this.getActivity()))
		.setCancelable(false)
		.setNegativeButton(R.string.btn_annuler, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		})
		.setAdapter(adapter, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {

		    }
		});
		
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
