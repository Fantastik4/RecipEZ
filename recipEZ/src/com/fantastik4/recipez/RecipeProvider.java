package com.fantastik4.recipez;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class RecipeProvider {
	private ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	private final Semaphore recipesAvailable = new Semaphore(1, true);
	public RecipeProvider()
	{
	}

	public ArrayList<Recipe> FetchAllRecipes()
	{
		try {
			GetAllRecipesFromService();
			recipesAvailable.acquire();
			return recipes;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally
		{
			recipesAvailable.release();
		}
	}

	public ArrayList<Recipe> FetchRecipesByIngredientID(String ingredientID)
	{
		try {
			GetRecipesByIngredientID(ingredientID);
			recipesAvailable.acquire();
			return recipes;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally
		{
			recipesAvailable.release();
		}
	}



	private void ParseRecipesFromXML(XmlPullParser myParser) 
	{
		int event;
		Step recipeStep = null;
		String recipeId = null, recipeName = null, text = null;
		ArrayList<Step> recipeSteps = new ArrayList<Step>();
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
					else if(name.equals("instructions"))
					{
						if(myParser.next() == XmlPullParser.TEXT) {
							recipeStep = new Step(myParser.getText());
							recipeSteps.add(recipeStep);
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if(name.equals("recipe")){
						recipe.setName(recipeName);
						recipe.setRecipeID(recipeId);
						recipe.setRecipeSteps(recipeSteps);
						recipes.add(recipe);
						recipe = new Recipe();
					}
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			recipesAvailable.release();
		}
	}

	private void GetRecipesByIngredientID(final String ingredientID) throws InterruptedException
	{
		recipesAvailable.acquire();
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					XmlPullParserFactory xmlFactoryObject;
					String urlString = "http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/FetchRecipesByIngredientID/" + ingredientID;
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

	private void GetAllRecipesFromService() throws InterruptedException{
		recipesAvailable.acquire();
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					XmlPullParserFactory xmlFactoryObject;
					URL url = new URL("http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/FetchAllRecipes/");
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
}