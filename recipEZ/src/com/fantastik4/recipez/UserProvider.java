package com.fantastik4.recipez;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class UserProvider {
	private ArrayList<User> users = new ArrayList<User>();

	public UserProvider()
	{
		GetUsersFromService();
	}
	public ArrayList<User> FetchAllUsers()
	{
		return users;
	}
	
	public void ParseUsersFromXML(XmlPullParser myParser) 
	{
		int event;
		String userId = null, userName = null, text = null;
		User user = new User();
		try {
			users = new ArrayList<User>();
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name=myParser.getName();
				switch (event){
				case XmlPullParser.START_TAG:
					

					if(name.equals("userId")){
						if(myParser.next() == XmlPullParser.TEXT) 
						{ 
							text = myParser.getText();
						}
						userId = text;
					}
					else if(name.equals("userName")){
						if(myParser.next() == XmlPullParser.TEXT) 
						{ 
							text = myParser.getText();
						}
						userName = text;
					}
					else{
					}
					break;
				case XmlPullParser.END_TAG:
					if(name.equals("user")){
						user.setName(userName);
						user.setUserID(userId);
						users.add(user);
						user = new User();
					}
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void GetUsersFromService(){
		Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
            		XmlPullParserFactory xmlFactoryObject;
                    URL url = new URL("http://recipezservice-recipez.rhcloud.com/rest/users");
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
}