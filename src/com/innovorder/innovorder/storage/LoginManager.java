package com.innovorder.innovorder.storage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

public class LoginManager {
	protected SharedPreferences storage;
	
	public LoginManager(Context context) {
		storage = context.getSharedPreferences("userprefs", Activity.MODE_PRIVATE);
	}
	
	public boolean isAthentificated()
	{
		return getRestaurant() != null && getPassword() != null && getSalt() != null;
	}
	
	public String getRestaurant()
	{
		return storage.getString("restaurant", null);
	}
	
	public String getPassword()
	{
		return storage.getString("password", null);
	}
	
	public String getSalt()
	{
		return storage.getString("salt", null);
	}
	
	public void setRestaurant(String restaurant)
	{
		storage.edit().putString("restaurant", restaurant).commit();
	}
	
	public void setPassword(String password)
	{
		storage.edit().putString("password", password).commit();
	}
	
	public void setSalt(String salt)
	{
		storage.edit().putString("salt", salt).commit();
	}
	
	public String hashPassword(String plainPassword)
	{
		String hash = "";
		String salted = plainPassword + '{' + getSalt() + '}';
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-512");
			byte sha[] = md.digest(salted.getBytes());
			for (int i = 1; i < 742; i++) {
				byte c[] = new byte[sha.length + salted.getBytes().length];
				System.arraycopy(sha, 0, c, 0, sha.length);
				System.arraycopy(salted.getBytes(), 0, c, sha.length, salted.getBytes().length);
				
				sha = md.digest(c);
			}
			hash = new String(Base64.encode(sha, Base64.NO_WRAP));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return hash;
	}

	public void clear() {
		setPassword(null);
	}
}
