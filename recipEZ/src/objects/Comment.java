package objects;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
	
	public String GetRecipeID()
	{
		return recipeId;
	}
	
	public String GetCommentBody()
	{
		return commentBody;
	}
	
	public Date GetDate()
	{
		return date;
	}

}
