package com.fantastik4.recipez;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class RecipeProvider {
	private ArrayList<Recipe> recipes = new ArrayList<Recipe>();

	public RecipeProvider()
	{
		GetRecipesFromService();
	}
	public ArrayList<Recipe> FetchAllRecipes()
	{
		return recipes;
	}
	
	public void ParseRecipesFromXML(XmlPullParser myParser) 
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
		}
	}
	
	public void GetRecipesFromService(){
		Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
            		XmlPullParserFactory xmlFactoryObject;
                    URL url = new URL("http://recipezservice-recipez.rhcloud.com/rest/recipes");
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