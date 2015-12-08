package resources;


public class WebserviceHelper {

	public enum ParseKey
	{
		Comment, Rating, NoParse, AverageRating, IsFavorited
	}
	
	public static final String getAverageRatingsByRecipeID_URL = "http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/GetAverageRating/";
	public static final String getCommentByRecipeID_URL = "http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/GetComments/";
	public static final String getRatingsByRecipeID_URL = "http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/GetRatings/";
	public static final String addRatingsByRecipeID_URL = "http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/AddRatingToRecipe/{$recipeId}/{$rating}/{$username}";
	public static final String addCommentsByRecipeId = "http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/AddCommentToRecipe/{$recipe_id}/{$username}/{$comment_body}/";
	public static final String RecipeAlreadyInFavorites = "http://recipezrestservice-recipez.rhcloud.com/rest/RecipeServices/RecipeAlreadyInFavorites/{$username}/{$recipe_id}";


	
}
