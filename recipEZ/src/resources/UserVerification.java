package resources;

import java.net.URL;
import java.io.InputStream;
import java.net.HttpURLConnection;
import org.xmlpull.v1.XmlPullParser;
import java.util.concurrent.Semaphore;
import org.xmlpull.v1.XmlPullParserFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class UserVerification {

	private String hashword;
	private final Semaphore verificationAvailable = new Semaphore(1, true);

	public UserVerification(){
	}

	public boolean validateUsernameExists(String u) {
		try {
			GetHashword(u);
			verificationAvailable.acquire();
			if(hashword.equals("null")) return false;
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
		} finally {
			verificationAvailable.release();
		}
		return true;
	}

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
		} finally {
			verificationAvailable.release();
		}
		return false;
	}

	/*
	 * Registers User
	 */
	public void RegisterUser(final String username, final String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		try {
			ExecuteGet("http://recipezrestservice-recipez.rhcloud.com/rest/VerificationServices/RegisterUser/"+username+"/"+password);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/*
	 * Gets HashedPassword from Webservice
	 */
	private void GetHashword(final String name) throws InterruptedException, NoSuchAlgorithmException, InvalidKeySpecException {
		ExecuteGet("http://recipezrestservice-recipez.rhcloud.com/rest/VerificationServices/GetHashword/"+name);
	}

	/*
	 * Parses Hashword
	 */
	private void ParseHashword(XmlPullParser myParser) {
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
		}finally {
			verificationAvailable.release();
		}
	}
	
	private void ExecuteGet(final String requestUrl) throws InterruptedException
	{
		verificationAvailable.acquire();
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					XmlPullParserFactory xmlFactoryObject;
					URL url = new URL(requestUrl);
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

}