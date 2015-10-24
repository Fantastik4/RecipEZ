package activities;

import com.fantastik4.recipez.R;
import com.fantastik4.recipez.R.id;
import com.fantastik4.recipez.R.layout;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends Activity {
	
	Button clickToRegister;
	EditText hint_email, hint_password;
	SpannableString changeLogoNameColor;
	TextView logoName, hint_confirmPassword;
	Typeface walkwaySemiBold, walkwayUltraBold;

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
		hint_email = (EditText)findViewById(R.id.et_registerEmail);
		clickToRegister = (Button)findViewById(R.id.btn_clickToSignUp);
		hint_password = (EditText)findViewById(R.id.et_registerPassword);
		hint_confirmPassword = (EditText)findViewById(R.id.et_confirmPassword);
		
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
	}
}
