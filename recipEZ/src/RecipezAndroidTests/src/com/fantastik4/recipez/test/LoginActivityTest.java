package com.fantastik4.recipez.test;

import activities.LoginActivity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivityTest extends ActivityUnitTestCase{

	public LoginActivityTest() {
		super(activities.LoginActivity.class);
		// TODO Auto-generated constructor stub
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		
		Intent mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), LoginActivity.class);
        startActivity(mLaunchIntent, null, null);

	}
	
	public void testCorrectCredentialsLogin()
	{
		TextView username = (TextView) getActivity().findViewById(com.fantastik4.recipez.R.id.et_enterUsername);
		TextView password = (TextView) getActivity().findViewById(com.fantastik4.recipez.R.id.et_enterPassword);
		username.setText("brianchan");
		password.setText("password");
		Button signin = (Button) getActivity().findViewById(com.fantastik4.recipez.R.id.btn_clickToLogin);
		signin.performClick();
		
		TextView errorMessage = (TextView) getActivity().findViewById(com.fantastik4.recipez.R.id.tv_errorMessage);
		assertEquals("Error message should not be visible", errorMessage.getVisibility(), errorMessage.INVISIBLE);
	}
	
	public void testIncorrectPasswordLogin()
	{
		TextView username = (TextView) getActivity().findViewById(com.fantastik4.recipez.R.id.et_enterUsername);
		TextView password = (TextView) getActivity().findViewById(com.fantastik4.recipez.R.id.et_enterPassword);
		username.setText("brianchan");
		password.setText("passwords");
		Button signin = (Button) getActivity().findViewById(com.fantastik4.recipez.R.id.btn_clickToLogin);
		signin.performClick();
		
		TextView errorMessage = (TextView) getActivity().findViewById(com.fantastik4.recipez.R.id.tv_errorMessage);
		assertEquals("Error message should not be visible", errorMessage.getVisibility(), errorMessage.VISIBLE);
	}

}
