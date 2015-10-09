package com.fantastik4.recipez;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.content.Intent;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.support.v7.app.ActionBarActivity;

public class LoginActivity extends ActionBarActivity {
	Button login;
	UserVerification userVerify = new UserVerification();
	@Override
	protected void onCreate(Bundle savedInsanceState) {
		super.onCreate(savedInsanceState);
		setContentView(R.layout.activity_main);
		login = (Button) findViewById(R.id.signIn);
		login.setClickable(false);
		new LongRunningGetIO().execute();
		
		//****PARSE XML AND RETRIEVE USERNAME AND PASSWORD****
		
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//****TESTING VALIDATION ONLY****
				//****REPLACE TEST DATA WITH PARSED DATA

				EditText usernameCheck = (EditText) findViewById(R.id.username);
				EditText passwordCheck = (EditText) findViewById(R.id.password);

				EditText displayErrorMessage = (EditText) findViewById(R.id.error_message);

				if(TextUtils.isEmpty(usernameCheck.getText().toString().trim()) || TextUtils.isEmpty(passwordCheck.getText().toString().trim())) {
					
					//Empty fields
					displayErrorMessage.setVisibility(View.VISIBLE);
					displayErrorMessage.setText(getResources().getString(R.string.emptyFields));
					
				} else {
					
					if(ValidateUserCredentials(usernameCheck.getText().toString().trim(), passwordCheck.getText().toString().trim())) {
						
						//Valid credentials
						displayErrorMessage.setVisibility(View.INVISIBLE);
						Intent i = new Intent(LoginActivity.this, SearchActivity.class);
						
						//****GO TO PAGE TWO UPON SUCCESS USERNAME AND PASSWORD MATCH****
						
						startActivity(i);
						overridePendingTransition(R.animator.animation1, R.animator.animation2);
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
		return userVerify.validate(username, password);
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
	
	private class LongRunningGetIO extends AsyncTask <Void, Void, String> {

		protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n>0) {
				byte[] b = new byte[4096];
				n =  in.read(b);
				if (n>0) out.append(new String(b, 0, n));
			}
			
			// ****XML STRING****
			// ****PRINTING OUT FOR TESTING PURPOSES ONLY****
			System.out.println("OUT TO STRING: " + out.toString());
			return out.toString();
		}

		@Override
		protected String doInBackground(Void... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet("http://recipezservice-recipez.rhcloud.com/rest/users");
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
			return text;
		}
	}
}
