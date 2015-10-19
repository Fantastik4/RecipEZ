package com.fantastik4.recipez;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class UserVerification {
	private boolean isValid = false;
	private boolean registered = false;
	private final Semaphore verificationAvailable = new Semaphore(1, true);
	public UserVerification(){}
	
	public boolean validate(String u, String p){
		try {
			ValidateUser(u, p);
			verificationAvailable.acquire();
			return isValid;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally
		{
			verificationAvailable.release();
		}
		
	}

	public void RegisterUser(final String username, final String userId)
	{
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					XmlPullParserFactory xmlFactoryObject;
					URL url = new URL("http://recipezrestservice-recipez.rhcloud.com/rest/VerificationServices/RegisterUser/"+username+"/"+userId);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();

					conn.setReadTimeout(10000 /* milliseconds */);
					conn.setConnectTimeout(15000 /* milliseconds */);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					conn.connect();

					InputStream stream = conn.getInputStream();

					xmlFactoryObject = XmlPullParserFactory.newInstance();
					XmlPullParser myParser = xmlFactoryObject.newPullParser();

					myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
					myParser.setInput(stream, null);
					//
					stream.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	
	public void ValidateUser (final String name, final String pass) throws InterruptedException {
		verificationAvailable.acquire();
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					XmlPullParserFactory xmlFactoryObject;
					URL url = new URL("http://recipezrestservice-recipez.rhcloud.com/rest/VerificationServices/VerifyUser/"+name+"/"+pass);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();

					conn.setReadTimeout(10000 /* milliseconds */);
					conn.setConnectTimeout(15000 /* milliseconds */);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					conn.connect();

					InputStream stream = conn.getInputStream();

					xmlFactoryObject = XmlPullParserFactory.newInstance();
					XmlPullParser myParser = xmlFactoryObject.newPullParser();

					myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
					myParser.setInput(stream, null);
					ParseVerification(myParser);
					stream.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	public void ParseVerification(XmlPullParser myParser) 
	{
		int event;
		String value = "", text = "";
		try {
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name=myParser.getName();
				switch (event){
				case XmlPullParser.START_TAG:
					if(name.equals("verificationResponse")){
						if(myParser.next() == XmlPullParser.TEXT) 
						{ 
							text = myParser.getText();
						}
						value = text;
					}
					break;
				case XmlPullParser.END_TAG:
					if(value.equals("true")){
						isValid = true;
					}
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			verificationAvailable.release();
		}
	}
}
