package resources;

import java.net.URL;
import objects.Comment;
import java.util.ArrayList;
import java.io.InputStream;
import java.net.HttpURLConnection;
import org.xmlpull.v1.XmlPullParser;
import java.util.concurrent.Semaphore;
import org.xmlpull.v1.XmlPullParserFactory;

public class CommentProvider {
	private ArrayList<Comment> comments;
	private final Semaphore commentsAvailable;

	public CommentProvider() {
		comments = new ArrayList<Comment>();
		commentsAvailable = new Semaphore(1, true);
	}
	
	public void AddCommentByRecipeID(String recipeID, String username, String comment_body)
	{
		String url = WebserviceHelper.addCommentsByRecipeId;
		comment_body = comment_body.replaceAll(" ", "%20");
		url = url.replace("{$username}", username);
		url = url.replace("{$recipe_id}", recipeID);
		url = url.replace("{$comment_body}", comment_body);
		try {
			ExecuteGet(url, true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Fetches all ingredients of userID
	 * @param userID
	 * @return
	 */
	public ArrayList<Comment> FetchCommentsByRecipe(String recipeID) {
		try {
			GetCommentsByRecipeID(recipeID);
			commentsAvailable.acquire();
			return comments;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			commentsAvailable.release();
		}
	}

	private void GetCommentsByRecipeID(final String recipeID) throws InterruptedException{
		ExecuteGet(WebserviceHelper.getCommentByRecipeID_URL + recipeID, false);
	}
	
	private void ParseCommentsFromXML(XmlPullParser myParser)  {

		int event;
		String commentName = null, recipeId = null, commentBody = null, date = null;
		Comment comment = new Comment();
		try {
			comments = new ArrayList<Comment>();
			event = myParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name=myParser.getName();

				switch (event){
				case XmlPullParser.START_TAG:
					if(name.equals("username")) {
						if(myParser.next() == XmlPullParser.TEXT)	commentName = myParser.getText();
					}
					else if(name.equals("recipeId")) {
						if(myParser.next() == XmlPullParser.TEXT) recipeId = myParser.getText();
					}
					else if(name.equals("commentBody")) {
						if(myParser.next() == XmlPullParser.TEXT) commentBody = myParser.getText();
					}
					else if(name.equals("date")) {
						if(myParser.next() == XmlPullParser.TEXT) date = myParser.getText();
					}
					break;

				case XmlPullParser.END_TAG:

					if(name.equals("comment")){
						comment.SetUsername(commentName);
						comment.SetRecipeID(recipeId);
						comment.SetCommentBody(commentBody);
						comment.SetDate(date);
						comments.add(comment);
						comment = new Comment();
					}
				}
				event = myParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			commentsAvailable.release();
		}
	}
	
	private void ExecuteGet(final String requestUrl, final boolean release) throws InterruptedException
	{
		commentsAvailable.acquire();
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
					if(!release)ParseCommentsFromXML(myParser);
					stream.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}finally
				{
					if(release) commentsAvailable.release();
				}
			}
		});
		thread.start();
	}
}