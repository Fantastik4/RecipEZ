package activities;

import objects.Recipe;
import resources.RecipeResourceProvider;
import resources.SocialResourceProvider;
import android.os.Bundle;
import android.app.Activity;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fantastik4.recipez.R;

public class DisplayRecipeActivity extends Activity {

	private Recipe selectedRecipe;

	private RatingBar ratingBar;
	private ToggleButton favToggleButton;
	private String username;
	RecipeResourceProvider recipeResourceProvider;
	SocialResourceProvider socialResourceProvider;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		username = (String) getIntent().getSerializableExtra("username");
		selectedRecipe = (Recipe) getIntent().getSerializableExtra("SelectedRecipe");
		
		setContentView(R.layout.activity_display_recipe);

		socialResourceProvider = new SocialResourceProvider();
		recipeResourceProvider = new RecipeResourceProvider();

		ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){

			@Override
			public void onRatingChanged(RatingBar rb, float rating, boolean fromUser) {
				// TODO Auto-generated method stub
				if(fromUser)
				{
					// TODO add if(recipeHasBeenRatedByUserAlready)
					socialResourceProvider.AddRatingByRecipeID(selectedRecipe.getRecipeID(),(int) rating, username);
				}
				
			}
			
		});
		
		favToggleButton = (ToggleButton) findViewById(R.id.favToggleButton);
		favToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				if(isChecked) recipeResourceProvider.AddRecipeToFavorites(username, selectedRecipe.getRecipeID());
				// TODO add Remove Recipe from favorites
			}
			
		});
		
		
		displayRecipe();
		
	}

	public void displayRecipe() {
		
		String[] tokensForList, tokensForSteps;
		String list = "", steps = "", delim = "\",\"";
		
		//Display Recipe Name
		TextView tv1 = (TextView)findViewById(R.id.tv_recipeName);
		tv1.setText(selectedRecipe.getName() + "\n");

		//Display Recipe Description
		TextView tv2 = (TextView)findViewById(R.id.tv_recipeDescription);
		tv2.setText(selectedRecipe.getRecipeDescription() + "\n");

		//Display Ingredients
		TextView tv3 = (TextView)findViewById(R.id.tv_recipeIngredients);
		
		String ingredients = selectedRecipe.getRecipeIngredients();
		tokensForList = ingredients.substring(2, ingredients.length()-2).split(delim);
		
		for (int i = 0; i < tokensForList.length; i++) {
			if (i == 0) list = "- " + tokensForList[i] + "\n";
			else list = list + "- " + tokensForList[i] + "\n";
		}
		tv3.setText(list);

		//Display Recipe Directions
		TextView tv4 = (TextView)findViewById(R.id.tv_recipeDirections);
		
		String directions = selectedRecipe.getRecipeDirections();
		tokensForSteps = directions.substring(2, directions.length()-2).split(delim);
		
		for (int i = 0; i < tokensForSteps.length; i++) {
			if (i == 0) steps = "- " + tokensForSteps[i] + "\n\n";
			else steps = steps + "- " + tokensForSteps[i] + "\n\n";
		}
		tv4.setText(steps);
		
		float rating = (float) socialResourceProvider.FetchAverageRatingByRecipeID(selectedRecipe.getRecipeID());
		 
		ratingBar.setRating(rating);
		

	}
}
