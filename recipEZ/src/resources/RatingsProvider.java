package resources;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import resources.WebserviceHelper.ParseKey;

public class RatingsProvider {
	Semaphore resourcesAvailable;
	private ArrayList<Integer> ratings;
	private double averageRating;
	
	public RatingsProvider()
	{
		resourcesAvailable = new Semaphore(1, true);
		ratings = new ArrayList<Integer>();
	}
	
	public double FetchAverageRatingByRecipeID(String recipeID)
	{
		try {
			GetAverageRatingsByRecipeID(recipeID);
			resourcesAvailable.acquire();
			return averageRating;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} finally {
			resourcesAvailable.release();
		}
	}
	
	public ArrayList<Integer> FetchRatingsByRecipeID(String recipeID)
	{
		try {
			GetRatingsByRecipeID(recipeID);
			resourcesAvailable.acquire();
			return ratings;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			resourcesAvailable.release();
		}
	}
	
	public void AddRatingByRecipeID(String recipeID, int rating, String username)
	{
		String url = WebserviceHelper.addRatingsByRecipeID_URL;
		url = url.replace("{$recipeId}", recipeID);
		url = url.replace("{$rating}", Integer.toString(rating));
		url = url.replace("{$username}", username);
		try {
			ExecuteGet(url, ParseKey.NoParse);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void GetAverageRatingsByRecipeID(String recipeID)
	{
		String url = WebserviceHelper.getAverageRatingsByRecipeID_URL + recipeID;
		try {
			ExecuteGet(url, ParseKey.AverageRating);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private void GetRatingsByRecipeID(String recipeId) throws InterruptedException
	{
		String url = WebserviceHelper.getRatingsByRecipeID_URL + recipeId;
		ExecuteGet(url, ParseKey.Rating);
	}
	
	private void ExecuteGet(final String requestUrl, final ParseKey parseKey) throws InterruptedException
	{
		resourcesAvailable.acquire();
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
					switch(parseKey){

					case Rating:
					{
						ParseRatingsFromXML(myParser);
						break;
					}
					case AverageRating:
					{
						ParseAverageRatingFromXML(myParser);
						break;
					}
					case NoParse:
					{
						resourcesAvailable.release();
						break;
					}
					}
					stream.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}
	
	

	private void ParseAverageRatingFromXML(XmlPullParser myParser) {
		int event;
		try {
			double rating = -1;
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name=myParser.getName();
				switch (event){
				case XmlPullParser.START_TAG:

					if(name.equals("average_rating"))
					{
						if(myParser.next() == XmlPullParser.TEXT) rating = Double.parseDouble(myParser.getText());
					}

					break;
				case XmlPullParser.END_TAG:
					this.averageRating = rating;
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			resourcesAvailable.release();
		}
	}
	
	private void ParseRatingsFromXML(XmlPullParser myParser) {
		int event;
		try {
			int rating = 0;
			ratings = new ArrayList<Integer>();
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name=myParser.getName();
				switch (event){
				case XmlPullParser.START_TAG:

					if(name.equals("rating"))
					{
						if(myParser.next() == XmlPullParser.TEXT) rating = Integer.parseInt(myParser.getText());
					}

					break;
				case XmlPullParser.END_TAG:
					if(name.equals("rating")){
						ratings.add(rating);
					}
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			resourcesAvailable.release();
		}
	}
}
