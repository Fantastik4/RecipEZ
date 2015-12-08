package resources;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Semaphore;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import resources.WebserviceHelper.ParseKey;


public class UserFavoritesProvider {

	Semaphore resourcesAvailable;
	private boolean isFavorited;
	
	public UserFavoritesProvider()
	{
		resourcesAvailable = new Semaphore(1, true);
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
	
	private void RecipeIsFavorited(String username, String recipeID) throws InterruptedException {
		String url = WebserviceHelper.RecipeAlreadyInFavorites;
		url = url.replace("{$username}", username);
		url = url.replace("{$recipe_id}", recipeID);
		
		ExecuteGet(url, false);
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
		}finally
		{
			resourcesAvailable.release();
		}
	}
	
	private void ExecuteGet(final String requestUrl, final boolean release) throws InterruptedException
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
					ParseIsFavorited(myParser);
					stream.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}finally
				{
					if(release)resourcesAvailable.release();
				}
			}
		});
		thread.start();
	}
}
