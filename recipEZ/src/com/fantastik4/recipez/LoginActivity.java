package com.fantastik4.recipez;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.text.TextUtils;
import android.content.Intent;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.support.v7.app.ActionBarActivity;

@SuppressWarnings("deprecation")
public class LoginActivity extends ActionBarActivity {
	Button login;
	UserVerification userVerify = new UserVerification();
	@Override
	protected void onCreate(Bundle savedInsanceState) {
		super.onCreate(savedInsanceState);
		setContentView(R.layout.activity_main);
		login = (Button) findViewById(R.id.signIn);
		login.setClickable(false);

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText usernameCheck = (EditText) findViewById(R.id.username);
				EditText passwordCheck = (EditText) findViewById(R.id.password);

				EditText displayErrorMessage = (EditText) findViewById(R.id.error_message);

				if(TextUtils.isEmpty(usernameCheck.getText().toString().trim()) || TextUtils.isEmpty(passwordCheck.getText().toString().trim())) {
					System.out.println("empty...");
					//Empty fields
					displayErrorMessage.setVisibility(View.VISIBLE);
					displayErrorMessage.setText(getResources().getString(R.string.emptyFields));

				} else {
					if(ValidateUserCredentials(usernameCheck.getText().toString().trim(), passwordCheck.getText().toString().trim())) {
						//Valid credentials

						displayErrorMessage.setVisibility(View.INVISIBLE);
						Intent i = new Intent(LoginActivity.this, SearchActivity.class);

						startActivity(i);
						overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
					} else {
						//Invalid credentials
						displayErrorMessage.setVisibility(View.VISIBLE);
						displayErrorMessage.setText(getResources().getString(R.string.invalidCredentials));
					}
				}
			}
		});
	}

	private boolean ValidateUserCredentials(String username, String password)
	{
		boolean valid = userVerify.validate(username, password);
		return valid;
	}
}
