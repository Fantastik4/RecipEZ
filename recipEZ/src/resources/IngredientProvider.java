package resources;

import java.net.URL;
import objects.Ingredient;
import java.util.ArrayList;
import java.io.InputStream;
import java.net.HttpURLConnection;
import org.xmlpull.v1.XmlPullParser;
import java.util.concurrent.Semaphore;
import org.xmlpull.v1.XmlPullParserFactory;

public class IngredientProvider {
	private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
	private final Semaphore ingredientsAvailable = new Semaphore(1, true);

	public IngredientProvider() {
	}

	/**
	 * Fetches all ingredients
	 * @return
	 */
	public ArrayList<Ingredient> FetchAllIngredients() {
		try {
			GetIngredientsFromService();
			ingredientsAvailable.acquire();
			return ingredients;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			ingredientsAvailable.release();
		}
	}

	/**
	 * Fetches all ingredients of userID
	 * @param userID
	 * @return
	 */
	public ArrayList<Ingredient> FetchIngredientsByUserID(String userID) {
		try {
			GetIngredientsByUserID(userID);
			ingredientsAvailable.acquire();
			return ingredients;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			ingredientsAvailable.release();
		}
	}

	/**
	 * Fetches all ingredients of recipeID
	 * @param recipeID
	 * @return
	 */
	public ArrayList<Ingredient> FetchIngredientsByRecipeID(String recipeID) {
		try {
			GetIngredientsByRecipeID(recipeID);
			ingredientsAvailable.acquire();
			return ingredients;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			ingredientsAvailable.release();
		}
	}

	/**
	 * Adds ingredientId and userId to User_Ingredients table of DB
	 * @param userId
	 * @param ingredientId
	 */
	public void AddIngredientToUser(final String username, final String ingredientId) {

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					String urlString = "http://recipezrestservice-recipez.rhcloud.com/rest/IngredientServices/AddIngredientToUser/" + username + "/" + ingredientId;
					URL url = new URL(urlString);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();

					conn.setReadTimeout(10000 /* milliseconds */);
					conn.setConnectTimeout(15000 /* milliseconds */);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					conn.connect();

					InputStream stream = conn.getInputStream();
					stream.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	private void ParseIngredientsFromXML(XmlPullParser myParser)  {

		int event;
		String ingredientName = null, ingredientId = null, ingredientAmount = null;
		Ingredient ingredient = new Ingredient();
		try {
			ingredients = new ArrayList<Ingredient>();
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name=myParser.getName();

				switch (event){
				case XmlPullParser.START_TAG:
					if(name.equals("ingredientId")) {
						if(myParser.next() == XmlPullParser.TEXT)	ingredientId = myParser.getText();
					}
					else if(name.equals("ingredientName")) {
						if(myParser.next() == XmlPullParser.TEXT) ingredientName = myParser.getText();
					}
					else if(name.equals("ingredientAmount")) {
						if(myParser.next() == XmlPullParser.TEXT) ingredientAmount = myParser.getText();
					}
					break;

				case XmlPullParser.END_TAG:

					if(name.equals("ingredient")){
						ingredient.setIngredientID(ingredientId);
						ingredient.setName(ingredientName);
						ingredient.setAmount(ingredientAmount);
						ingredients.add(ingredient);
						ingredient = new Ingredient();
					}
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ingredientsAvailable.release();
		}
	}

	private void GetIngredientsByRecipeID(final String recipeID) throws InterruptedException{
		ingredientsAvailable.acquire();
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					XmlPullParserFactory xmlFactoryObject;
					String urlString = "http://recipezrestservice-recipez.rhcloud.com/rest/IngredientServices/FetchIngredientsByRecipeID/" + recipeID;
					URL url = new URL(urlString);
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
					ParseIngredientsFromXML(myParser);
					stream.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	private void GetIngredientsFromService() throws InterruptedException{
		ingredientsAvailable.acquire();
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					XmlPullParserFactory xmlFactoryObject;
					URL url = new URL("http://recipezrestservice-recipez.rhcloud.com/rest/IngredientServices/FetchAllIngredients/");
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
					ParseIngredientsFromXML(myParser);
					stream.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	private void GetIngredientsByUserID(final String userID) throws InterruptedException {
		ingredientsAvailable.acquire();
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					XmlPullParserFactory xmlFactoryObject;
					String urlString = "http://recipezrestservice-recipez.rhcloud.com/rest/IngredientServices/FetchIngredientsByUserID/" + userID;
					URL url = new URL(urlString);
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
					ParseIngredientsFromXML(myParser);
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