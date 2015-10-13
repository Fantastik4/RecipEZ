package com.fantastik4.recipez;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class IngredientProvider {
	private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

	public IngredientProvider()
	{
		GetIngredientsFromService();
	}
	public ArrayList<Ingredient> FetchAllIngredients()
	{
		return ingredients;
	}
	
	public void ParseIngredientsFromXML(XmlPullParser myParser) 
	{
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
					

					if(name.equals("ingredientId")){
						if(myParser.next() == XmlPullParser.TEXT)	ingredientId = myParser.getText();
					}
					else if(name.equals("ingredientName")){
						if(myParser.next() == XmlPullParser.TEXT) ingredientName = myParser.getText();
					}
					else if(name.equals("ingredientAmount"))
					{
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
		}
	}
	
	public void GetIngredientsFromService(){
		Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
            		XmlPullParserFactory xmlFactoryObject;
                    URL url = new URL("http://recipezservice-recipez.rhcloud.com/rest/ingredients");
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