package com.fantastik4.recipez;

import java.net.URL;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.Semaphore;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class UserVerification {
	private boolean registered = false;
	private String hashword;
	private final Semaphore verificationAvailable = new Semaphore(1, true);
	public UserVerification(){}

	public boolean validate(String u, String p){
		try {
			GetHashword(u);
			verificationAvailable.acquire();
			if(hashword.equals("null")) return false;
			return PasswordHash.validatePassword(p, hashword);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			verificationAvailable.release();
		}
		return false;
	}

	/*
	 * Registers User
	 */
	public void RegisterUser(final String username, final String password) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		final String passHash = PasswordHash.createHash(password);

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					XmlPullParserFactory xmlFactoryObject;
					URL url = new URL("http://recipezrestservice-recipez.rhcloud.com/rest/VerificationServices/RegisterUser/"+username+"/"+passHash);
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

	//	private void ValidateUser (final String name, final String pass) throws InterruptedException, NoSuchAlgorithmException, InvalidKeySpecException {
	//		verificationAvailable.acquire();
	//		Thread thread = new Thread(new Runnable(){
	//			@Override
	//			public void run() {
	//				try {
	//					XmlPullParserFactory xmlFactoryObject;
	//					URL url = new URL("http://recipezrestservice-recipez.rhcloud.com/rest/VerificationServices/VerifyUser/"+name+"/"+pass);
	//					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	//
	//					conn.setReadTimeout(10000 /* milliseconds */);
	//					conn.setConnectTimeout(15000 /* milliseconds */);
	//					conn.setRequestMethod("GET");
	//					conn.setDoInput(true);
	//					conn.connect();
	//
	//					InputStream stream = conn.getInputStream();
	//
	//					xmlFactoryObject = XmlPullParserFactory.newInstance();
	//					XmlPullParser myParser = xmlFactoryObject.newPullParser();
	//
	//					myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	//					myParser.setInput(stream, null);
	//					ParseVerification(myParser);
	//					stream.close();
	//				}
	//				catch (Exception e) {
	//					e.printStackTrace();
	//				}
	//			}
	//		});
	//		thread.start();
	//	}

	/*
	 * Gets HashedPassword from Webservice
	 */
	private void GetHashword(final String name) throws InterruptedException, NoSuchAlgorithmException, InvalidKeySpecException {
		verificationAvailable.acquire();
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					XmlPullParserFactory xmlFactoryObject;
					URL url = new URL("http://recipezrestservice-recipez.rhcloud.com/rest/VerificationServices/GetHashword/"+name);
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
					ParseHashword(myParser);
					stream.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	/*
	 * Parses Hashword
	 */
	private void ParseHashword(XmlPullParser myParser) 
	{
		int event;
		String value = "", text = "";
		try {
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name=myParser.getName();
				switch (event){
				case XmlPullParser.START_TAG:
					if(name.equals("hashword")){
						if(myParser.next() == XmlPullParser.TEXT) 
						{ 
							text = myParser.getText();
						}
						value = text;
					}
					break;
				case XmlPullParser.END_TAG:
					hashword = value;
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			verificationAvailable.release();
		}
	}

	//	private void ParseVerification(XmlPullParser myParser) 
	//	{
	//		int event;
	//		String value = "", text = "";
	//		try {
	//			event = myParser.getEventType();
	//			while (event != XmlPullParser.END_DOCUMENT) {
	//				String name=myParser.getName();
	//				switch (event){
	//				case XmlPullParser.START_TAG:
	//					if(name.equals("verificationResponse")){
	//						if(myParser.next() == XmlPullParser.TEXT) 
	//						{ 
	//							text = myParser.getText();
	//						}
	//						value = text;
	//					}
	//					break;
	//				case XmlPullParser.END_TAG:
	//					if(value.equals("true")){
	//						isValid = true;
	//					}
	//				}
	//				event = myParser.next();
	//			}
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}finally{
	//			verificationAvailable.release();
	//		}
	//	}
}