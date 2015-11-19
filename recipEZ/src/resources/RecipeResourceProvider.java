package resources;

import java.net.URL;

import objects.Comment;
import objects.Recipe;
import java.io.InputStream;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import org.xmlpull.v1.XmlPullParser;
import java.util.concurrent.Semaphore;
import org.xmlpull.v1.XmlPullParserFactory;

public class RecipeResourceProvider{
	private ArrayList<Recipe> recipes;
	private final Semaphore recipesAvailable;

	public RecipeResourceProvider() {
		recipes = new ArrayList<Recipe>();
		recipesAvailable = new Semaphore(1, true);
	}

	public ArrayList<Recipe> FetchAllRecipes() {
		try {
			GetAllRecipesFromService();
			recipesAvailable.acquire();
			return recipes;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			recipesAvailable.release();
		}
	}

	public ArrayList<Recipe> FetchRecipesByIngredientID(String ingredientID) {
		try {
			GetRecipesByIngredientID(ingredientID);
			recipesAvailable.acquire();
			return recipes;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			recipesAvailable.release();
		}
	}	
	
	public ArrayList<Recipe> FetchFavoriteRecipes(String username) {
		try {
			GetFavoriteRecipes(username);
			recipesAvailable.acquire();
			return recipes;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			recipesAvailable.release();
		}
	}
	
	public void AddRecipeToFavorites(String username, String recipeId)
	{
		try {
			ExecuteGet("http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/AddRecipeToFavorites/" + username + "/" + recipeId);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void RemoveRecipeFromFavorites(String username, String recipeId)
	{
		try {
			ExecuteGet("http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/RemoveRecipeFromFavorites/" + username +"/" + recipeId);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void GetRecipesByIngredientID(final String ingredientID) throws InterruptedException {
		ExecuteGet("http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/FetchRecipesByIngredientID/" + ingredientID);
	}
	
	private void GetFavoriteRecipes(final String username) throws InterruptedException {
		ExecuteGet("http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/FetchFavoriteRecipes/" + username);
	}

	private void GetAllRecipesFromService() throws InterruptedException {
		ExecuteGet("http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/FetchAllRecipes/");
	}
	
	private void ExecuteGet(final String requestUrl) throws InterruptedException
	{
		recipesAvailable.acquire();
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
					ParseRecipesFromXML(myParser);
					stream.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	
	private void ParseRecipesFromXML(XmlPullParser myParser) {
		int event;
		String recipeId = null, recipeName = null, recipeDescription = null, recipeIngredientList = null, recipeDirections = null;
		Recipe recipe = new Recipe();
		try {
			recipes = new ArrayList<Recipe>();
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name=myParser.getName();
				switch (event){
				case XmlPullParser.START_TAG:

					if(name.equals("id"))
					{
						if(myParser.next() == XmlPullParser.TEXT) recipeId = myParser.getText();
					}
					else if(name.equals("name"))
					{
						if(myParser.next() == XmlPullParser.TEXT) recipeName = myParser.getText();
					}
					else if(name.equals("description"))
					{
						if(myParser.next() == XmlPullParser.TEXT) recipeDescription = myParser.getText();
					}
					else if(name.equals("directions"))
					{
						if(myParser.next() == XmlPullParser.TEXT) recipeDirections = myParser.getText();
					}
					else if(name.equals("ingredientList"))
					{
						if(myParser.next() == XmlPullParser.TEXT) recipeIngredientList = myParser.getText();
					}

					break;
				case XmlPullParser.END_TAG:
					if(name.equals("recipe")){
						recipe.setName(recipeName);
						recipe.setRecipeID(recipeId);
						recipe.setRecipeDirections(recipeDirections);
						recipe.setRecipeIngredientList(recipeIngredientList);
						recipe.setRecipeDescription(recipeDescription);
						recipes.add(recipe);
						recipe = new Recipe();
					}
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			recipesAvailable.release();
		}
	}
}