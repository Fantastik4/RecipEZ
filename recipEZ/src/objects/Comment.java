package objects;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Comment implements Serializable{
	
	private String username;
	private String recipeId;
	private String commentBody;
	private Date date;
	
	public Comment()
	{
		
	}
	
	public Comment(String username, String recipeId, String commentBody, String date)
	{
		this.username = username;
		this.recipeId = recipeId;
		this.commentBody = commentBody;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		try {
			this.date = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String GetUsername()
	{
		return username;
	}
	public void SetUsername(String u) {
		this.username = u;
	}
	
	public String GetRecipeID()
	{
		return recipeId;
	}
	public void SetRecipeID(String r) {
		this.recipeId = r;
	}
	
	public String GetCommentBody()
	{
		return commentBody;
	}
	public void SetCommentBody(String c) {
		this.commentBody = c;
	}
	
	public Date GetDate()
	{
		return date;
	}
	public void SetDate(String d) throws ParseException{	
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date x = format.parse(d);
		this.date = x;
	}

}
