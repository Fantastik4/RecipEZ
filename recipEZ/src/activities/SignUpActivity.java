package activities;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.widget.Button;
import android.graphics.Color;
import android.text.Spannable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import com.fantastik4.recipez.R;
import android.graphics.Typeface;
import resources.UserVerification;
import android.text.SpannableString;
import android.view.View.OnClickListener;
import android.text.style.ForegroundColorSpan;

public class SignUpActivity extends Activity {

	boolean valid;
	Button clickToRegister;
	UserVerification userVerify;
	SpannableString changeLogoNameColor;
	Typeface walkwaySemiBold, walkwayUltraBold;
	EditText hint_email, hint_password, hint_confirmPassword;
	TextView logoName, usernameCheck, passwordCheck, confirmPasswordCheck, displayErrorMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		/**
		 * LABELS:	Logo - tv_recipezLogo
		 * 			Logo Name - tv_logoName
		 * 			Email - et_registerEmail
		 * 			Password - et_registerPassword
		 * 			Sign Up Button - btn_clickToSignUp
		 * 			Confirm Password - et_confirmPassword
		 */
		logoName = (TextView)findViewById(R.id.tv_logoName);
		hint_email = (EditText)findViewById(R.id.et_registerUsername);
		clickToRegister = (Button)findViewById(R.id.btn_clickToSignUp);
		hint_password = (EditText)findViewById(R.id.et_registerPassword);
		hint_confirmPassword = (EditText)findViewById(R.id.et_confirmPassword);

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
		hint_email.setTypeface(walkwayUltraBold);
		hint_password.setTypeface(walkwayUltraBold);
		clickToRegister.setTypeface(walkwayUltraBold);
		hint_confirmPassword.setTypeface(walkwayUltraBold);

		/**
		 * Color Properties for RecipEZ
		 */
		changeLogoNameColor = new SpannableString(logoName.getText());
		changeLogoNameColor.setSpan(new ForegroundColorSpan(Color.parseColor("#F5C04F")), 5, logoName.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		logoName.setText(changeLogoNameColor);

		/**
		 * Login Button
		 */
		clickToRegister.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				usernameCheck = (EditText) findViewById(R.id.et_registerUsername);
				String username = usernameCheck.getText().toString().trim();
				passwordCheck = (EditText) findViewById(R.id.et_registerPassword);
				String password = passwordCheck.getText().toString().trim();
				confirmPasswordCheck = (EditText) findViewById(R.id.et_confirmPassword);
				String confirmPassword = confirmPasswordCheck.getText().toString().trim();

				//Check if all fields have been properly filled out
				if (TextUtils.isEmpty(usernameCheck.getText().toString().trim()) || TextUtils.isEmpty(passwordCheck.getText().toString().trim()) || TextUtils.isEmpty(confirmPasswordCheck.getText().toString().trim())) {

					//Empty fields
					displayErrorMessage.setVisibility(View.VISIBLE);
					displayErrorMessage.setText(getResources().getString(R.string.errorMessage_EmptyFields));
				}
				else {

					//Check if username has already been taken
					if(ValidateUserCredentials(username) == true) {

						//Invalid credentials
						displayErrorMessage.setVisibility(View.VISIBLE);
						displayErrorMessage.setText(getResources().getString(R.string.errorMessage_UsernameTaken));

					} else {

						//Passwords do not match
						if (password.equals(confirmPassword) == false) {
							//Invalid credentials
							displayErrorMessage.setVisibility(View.VISIBLE);
							displayErrorMessage.setText(getResources().getString(R.string.errorMessage_PasswordsMustMatch));
						}
						else {
							if (username.length() >= 6 && username.length() <= 20) {
								if (password.length() >= 6 && password.length() <= 20) {

									//Requirements have been met - Create User
									//displayErrorMessage.setVisibility(View.INVISIBLE);
									//Intent i = new Intent(SignUpActivity.this, PortalActivity.class);
									//i.putExtra("CurrentUsername", username);

									//startActivity(i);
									//overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

									System.out.println("User created!");
								}
							}
							else {
								//Invalid credentials
								displayErrorMessage.setVisibility(View.VISIBLE);
								displayErrorMessage.setText(getResources().getString(R.string.errorMessage_SignUpRequirements));
							}
						}
					}
				}
			}
		});
	}

	/**
	 * Username and Password
	 * @param username
	 * @param password
	 * @return
	 */
	private boolean ValidateUserCredentials(String username) {
		valid = userVerify.validateUsernameExists(username);
		return valid;
	}
}
