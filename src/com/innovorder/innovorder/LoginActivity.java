package com.innovorder.innovorder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.innovorder.innovorder.Toast.CustomToast;
import com.innovorder.innovorder.storage.LoginManager;
import com.innovorder.innovorder.webservice.CheckLoginCall;
import com.innovorder.innovorder.webservice.GetSaltCall;
import com.innovorder.innovorder.webservice.WebserviceCallListener;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class LoginActivity extends Activity implements OnClickListener, WebserviceCallListener {
	protected LoginManager loginManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		loginManager = new LoginManager(this);
		if (loginManager.isAthentificated()) {
			String restaurant = loginManager.getRestaurant();
			String password = loginManager.getPassword();
			
			CheckLoginCall checkLoginCall = new CheckLoginCall();
			checkLoginCall.setParentActivity(this);
			checkLoginCall.execute(restaurant, password);
		}
		
		Button button = (Button) findViewById(R.id.login_button);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String restaurant = ((EditText) findViewById(R.id.identEditText)).getText().toString();
		
		GetSaltCall getSaltCall = new GetSaltCall();
		getSaltCall.setParentActivity(this);
		getSaltCall.execute(restaurant);
	}

	@Override
	public void onWebserviceCallFinished(String response, String tag) {
		String restaurant = ((EditText) findViewById(R.id.identEditText)).getText().toString();
		String plainPassword = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
		
		if (tag == "salt") {
			String salt = null;
			if (response != null) {
				// json regex
				String jsonPattern = "\"([^\"]*)\":\"([^\"]*)\"";
				Pattern re = Pattern.compile(jsonPattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
				
				Matcher matcher = re.matcher(response);
				while (matcher.find()) {
					String key = matcher.group(1);
					String value = matcher.group(2);
					if (key.equalsIgnoreCase("salt")) {
						salt = value;
					}
			    }
			}
			
			if (salt == null) {
				CustomToast.makeText(this, R.string.bad_user_toast, Toast.LENGTH_SHORT).show();
				return;
			}
			
			loginManager.setRestaurant(restaurant);
			loginManager.setSalt(salt);
			String password = loginManager.hashPassword(plainPassword);
			loginManager.setPassword(password);
			
			CheckLoginCall checkLoginCall = new CheckLoginCall();
			checkLoginCall.setParentActivity(this);
			checkLoginCall.execute(restaurant, password);
		} else if (tag == "login") {
			boolean login = response != null && response.endsWith("{\"login\":true}");
			if (login == false) {
				CustomToast.makeText(this, R.string.bad_user_toast, Toast.LENGTH_SHORT).show();
				loginManager.clear();
				return;
			}

			Intent intent = new Intent(this, WelcomeActivity.class);
			startActivity(intent);
			finish();
		}
	}

}
