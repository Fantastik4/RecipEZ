package resources;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Semaphore;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import objects.Comment;
import objects.Recipe;

public class SocialResourceProvider {
	
	public enum ParseKey
	{
		Comment, Rating, NoParse, AverageRating, IsFavorited
	}

	private static final String getAverageRatingsByRecipeID_URL = "http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/GetAverageRating/";
	private static final String getCommentByRecipeID_URL = "http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/GetComments/";
	private static final String getRatingsByRecipeID_URL = "http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/GetRatings/";
	private static final String addRatingsByRecipeID_URL = "http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/AddRatingToRecipe/{$recipeId}/{$rating}/{$username}";
	private static final String addCommentsByRecipeId = "http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/AddCommentToRecipe/{$recipe_id}/{$username}/{$comment_body}/";
	private static final String RecipeAlreadyInFavorites = "http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/RecipeAlreadyInFavorites/{$username}/{$recipe_id}";
	private ArrayList<Comment> comments;
	private ArrayList<Integer> ratings;
	private double averageRating;
	private boolean isFavorited;
	
	Semaphore resourcesAvailable;
	public SocialResourceProvider()
	{
		comments = new ArrayList<Comment>();
		resourcesAvailable = new Semaphore(1, true);
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
	
	public boolean IsRecipeAlreadyFavorited(String username, String recipeID)
	{
		try {
			RecipeIsFavorited(username, recipeID);
			resourcesAvailable.acquire();
			return isFavorited;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			resourcesAvailable.release();
		}
	}

	public ArrayList<Comment> FetchCommentsByRecipeID(String recipeID) {
		try {
			GetCommentsByRecipeID(recipeID);
			resourcesAvailable.acquire();
			return comments;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
		String url = addRatingsByRecipeID_URL;
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
		String url = getAverageRatingsByRecipeID_URL + recipeID;
		try {
			ExecuteGet(url, ParseKey.AverageRating);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void RecipeIsFavorited(String username, String recipeID) throws InterruptedException {
		String url = RecipeAlreadyInFavorites;
		url = url.replace("{$username}", username);
		url = url.replace("{$recipe_id}", recipeID);
		
		ExecuteGet(url, ParseKey.IsFavorited);
	}
	
	private void GetRatingsByRecipeID(String recipeId) throws InterruptedException
	{
		String url = getRatingsByRecipeID_URL + recipeId;
		ExecuteGet(url, ParseKey.Rating);
	}
	
	public void AddCommentByRecipeID(String recipeID, String username, String comment_body) throws InterruptedException
	{
		String url = addCommentsByRecipeId;
		url = url.replace("{$username}", username);
		url = url.replace("{$recipe_id}", recipeID);
		url = url.replace("{$comment_body}", comment_body);
		ExecuteGet(url, ParseKey.NoParse);
	}
	
	private void GetCommentsByRecipeID(final String recipeID) throws InterruptedException
	{
		ExecuteGet(getCommentByRecipeID_URL + recipeID, ParseKey.Comment);
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
					switch(parseKey)
					{
					case Comment:
					{
						ParseCommentsFromXML(myParser);
						break;
					}
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
					case IsFavorited:
					{
						ParseIsFavorited(myParser);
						break;
					}
					case NoParse:
					{
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
	
	private void ParseIsFavorited(XmlPullParser myParser) {
		int event;
		try {
			int isFavorited = 0;
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = myParser.getName();
				switch (event){
				case XmlPullParser.START_TAG:

					if(name.equals("is_favorited"))
					{
						if(myParser.next() == XmlPullParser.TEXT) isFavorited = Integer.parseInt(myParser.getText());
					}

					break;
				case XmlPullParser.END_TAG:
					if(isFavorited == 1) this.isFavorited = true;
					else this.isFavorited = false;
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			resourcesAvailable.release();
		}
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
	
	private void ParseCommentsFromXML(XmlPullParser myParser) {
		int event;
		String recipeId = null, username = null, commentBody = null, date = null;
		Comment comment = new Comment();
		try {
			comments = new ArrayList<Comment>();
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name=myParser.getName();
				switch (event){
				case XmlPullParser.START_TAG:

					if(name.equals("username"))
					{
						if(myParser.next() == XmlPullParser.TEXT) username = myParser.getText();
					}
					else if(name.equals("recipeId"))
					{
						if(myParser.next() == XmlPullParser.TEXT) recipeId = myParser.getText();
					}
					else if(name.equals("commentBody"))
					{
						if(myParser.next() == XmlPullParser.TEXT) commentBody = myParser.getText();
					}
					else if(name.equals("date"))
					{
						if(myParser.next() == XmlPullParser.TEXT) date = myParser.getText();
					}


					break;
				case XmlPullParser.END_TAG:
					if(name.equals("comment")){
						comment = new Comment(username, recipeId, commentBody, date);
						comments.add(comment);
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
