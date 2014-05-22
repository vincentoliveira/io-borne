package com.innovorder.innovorder.utils;

import android.annotation.SuppressLint;

public class PriceFormatter {
	@SuppressLint("DefaultLocale")
	public static String format(double price) {
		return String.format("%.2f€", price);
	}
}
