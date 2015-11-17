package com.fantastik4.recipez.test;

import java.util.ArrayList;

import android.test.ActivityUnitTestCase;
import objects.Comment;
import objects.Recipe;
import resources.RecipeResourceProvider;
import resources.SocialResourceProvider;

public class SocialResourceProviderTests extends ActivityUnitTestCase{

	private SocialResourceProvider socialResourceProvider;
	
	public SocialResourceProviderTests() {
		super(activities.RecipeResultsActivity.class);
		// TODO Auto-generated constructor stub
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		socialResourceProvider = new SocialResourceProvider();
	}
	
	public void testFetchComments()
	{
		ArrayList<Comment> comments = socialResourceProvider.FetchCommentsByRecipeID("r1000");
		assertFalse("No comments returned",comments.isEmpty());
	}
	
//	public void testAddComments()
//	{
//		String recipeID = "r1000";
//		String username = "brianchan";
//		String comment_body = "test";
//		try {
//			socialResourceProvider.AddCommentByRecipeID(recipeID, username, comment_body);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	public void testAddRating()
//	{
//		String recipeId = "r1000";
//		String username = "brianchan";
//		int rating = 5;
//		
//		socialResourceProvider.AddRatingByRecipeID(recipeId, rating, username);
//	}
	
	public void testGetAverageRating()
	{
		double rating = socialResourceProvider.FetchAverageRatingByRecipeID("r1000");
		assertTrue("Error fetching rating.",rating > 0);
	}
	

}
