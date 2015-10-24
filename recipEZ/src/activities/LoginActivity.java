package activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.text.Spannable;
import android.text.TextUtils;

import com.fantastik4.recipez.R;
import com.fantastik4.recipez.R.anim;
import com.fantastik4.recipez.R.id;
import com.fantastik4.recipez.R.layout;
import com.fantastik4.recipez.R.string;

import android.content.Intent;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;
import resources.UserVerification;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.view.View.OnClickListener;
import android.text.style.ForegroundColorSpan;
import android.support.v7.app.ActionBarActivity;

@SuppressWarnings("deprecation")
public class LoginActivity extends ActionBarActivity {

	boolean valid;
	Button clickToLogin;
	UserVerification userVerify;
	SpannableString changeLogoNameColor;
	EditText usernameCheck, passwordCheck;
	Typeface walkwaySemiBold, walkwayUltraBold;
	TextView logoName, hint_username, hint_password, clickToSignUp, errorMessage, displayErrorMessage;

	@Override
	protected void onCreate(Bundle savedInsanceState) {
		super.onCreate(savedInsanceState);
		setContentView(R.layout.activity_main);

		/**
		 * LABELS:	Logo - iv_recipezLogo
		 * 			Logo Name - tv_logoName
		 * 			Sign Up Button - tv_signUp
		 * 			Login Button - btn_clickToLogin
		 * 			Error Message - tv_errorMessage
		 * 			Username Text Field - et_enterUsername
		 * 			Password Text Field - et_enterPassword
		 */
		userVerify = new UserVerification();
		logoName = (TextView)findViewById(R.id.tv_logoName);
		clickToSignUp = (TextView)findViewById(R.id.tv_signUp);
		clickToLogin = (Button) findViewById(R.id.btn_clickToLogin);
		hint_username = (EditText) findViewById(R.id.et_enterUsername);
		hint_password = (EditText) findViewById(R.id.et_enterPassword);
		displayErrorMessage = (TextView) findViewById(R.id.tv_errorMessage);

		/**
		 * Custom Font Creation
		 */
		walkwaySemiBold = Typeface.createFromAsset(getAssets(), "fonts/Walkway_SemiBold.ttf");
		walkwayUltraBold = Typeface.createFromAsset(getAssets(), "fonts/Walkway_UltraBold.ttf");

		/**
		 * Apply Custom Fonts
		 */
		logoName.setTypeface(walkwaySemiBold);
		clickToLogin.setTypeface(walkwayUltraBold);
		hint_username.setTypeface(walkwayUltraBold);
		hint_password.setTypeface(walkwayUltraBold);
		clickToSignUp.setTypeface(walkwayUltraBold);
		displayErrorMessage.setTypeface(walkwayUltraBold);

		/**
		 * Color Properties for RecipEZ
		 */
		changeLogoNameColor = new SpannableString(logoName.getText());
		changeLogoNameColor.setSpan(new ForegroundColorSpan(Color.parseColor("#F5C04F")), 5, logoName.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		logoName.setText(changeLogoNameColor);

		/**
		 * Hide Action Bar - Remove Shadow
		 */
		getSupportActionBar().hide();
		getSupportActionBar().setElevation(0);

		/**
		 * Login Button
		 */
		clickToLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				usernameCheck = (EditText) findViewById(R.id.et_enterUsername);
				String username = usernameCheck.getText().toString().trim();
				passwordCheck = (EditText) findViewById(R.id.et_enterPassword);
				String password = passwordCheck.getText().toString().trim();

				if (TextUtils.isEmpty(usernameCheck.getText().toString().trim()) || TextUtils.isEmpty(passwordCheck.getText().toString().trim())) {

					//Empty fields
					displayErrorMessage.setVisibility(View.VISIBLE);
					displayErrorMessage.setText(getResources().getString(R.string.errorMessage_EmptyFields));
				} else {
					if(ValidateUserCredentials(username, password)) {

						//Valid credentials
						displayErrorMessage.setVisibility(View.INVISIBLE);
						Intent i = new Intent(LoginActivity.this, PortalActivity.class);
						i.putExtra("CurrentUsername", username);

						startActivity(i);
						overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
					} else {

						//Invalid credentials
						displayErrorMessage.setVisibility(View.VISIBLE);
						displayErrorMessage.setText(getResources().getString(R.string.errorMessage_InvalidCredentials));
					}
				}
			}
		});

		/**
		 * Sign Up Button
		 */
		clickToSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent j = new Intent(LoginActivity.this, SignUpActivity.class);

				startActivity(j);
				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}
		});
	}

	/**
	 * Username and Password
	 * @param username
	 * @param password
	 * @return
	 */
	private boolean ValidateUserCredentials(String username, String password) {
		valid = userVerify.validate(username, password);
		return valid;
	}
}
