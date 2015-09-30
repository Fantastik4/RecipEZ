package com.fantastik4.recipez;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.text.TextUtils;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.support.v7.app.ActionBarActivity;

public class LoginActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button login = (Button) findViewById(R.id.signIn);
		
		login.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//Test validation
				String testUser = "nfulinarajr";
				String testPass = "testPass";
				EditText usernameCheck = (EditText) findViewById(R.id.username);
				EditText passwordCheck = (EditText) findViewById(R.id.password);
				
				EditText displayErrorMessage = (EditText) findViewById(R.id.error_message);

				if(TextUtils.isEmpty(usernameCheck.getText().toString().trim()) || 
						TextUtils.isEmpty(passwordCheck.getText().toString().trim())) {
					displayErrorMessage.setVisibility(View.VISIBLE);
					displayErrorMessage.setText(getResources().getString(R.string.emptyFields));
				} else {
					if(usernameCheck.getText().toString().trim().equals(testUser)
							&& passwordCheck.getText().toString().trim().equals(testPass)) {
						displayErrorMessage.setVisibility(View.INVISIBLE);
						Intent i = new Intent(LoginActivity.this, SearchActivity.class);
						startActivity(i);
						overridePendingTransition(R.animator.animation1, R.animator.animation2);
					} else {
						displayErrorMessage.setVisibility(View.VISIBLE);
						displayErrorMessage.setText(getResources().getString(R.string.invalidCredentials));
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
