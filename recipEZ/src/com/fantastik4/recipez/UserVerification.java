package com.fantastik4.recipez;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class UserVerification {
	private boolean isValid = false;
	
	public UserVerification(){}
	
	public boolean validate(String u, String p){
		ValidateUser(u, p);
		return isValid;
	}
	
	public void ValidateUser(final String name, final String pass){
		Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
            		XmlPullParserFactory xmlFactoryObject;
                    URL url = new URL("http://recipezservice-recipez.rhcloud.com/rest/VerificationServices/VerifyUser/"+name+"/"+pass);
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
                    ParseUsersFromXML(myParser);
                    stream.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
	
	public void ParseUsersFromXML(XmlPullParser myParser) 
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
		}
	}
}
