package activities;

import objects.Comment;
import objects.Recipe;
import resources.CommentProvider;
import resources.RatingsProvider;
import resources.RecipeResourceProvider;
import resources.UserFavoritesProvider;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.fantastik4.recipez.R;

public class DisplayRecipeActivity extends Activity {

	private String username;
	private RatingBar ratingBar;
	private Recipe selectedRecipe;
	CommentProvider commentProvider;
	RatingsProvider ratingsProvider;
	UserFavoritesProvider favoritesProvider;
	Button commentButton;
	EditText commentTextField;
	private ToggleButton favToggleButton;
	RecipeResourceProvider recipeResourceProvider;
	TableLayout commentsTable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		username = (String) getIntent().getSerializableExtra("username");
		selectedRecipe = (Recipe) getIntent().getSerializableExtra("SelectedRecipe");

		setContentView(R.layout.activity_display_recipe);

		recipeResourceProvider = new RecipeResourceProvider();
		commentProvider = new CommentProvider();
		ratingsProvider = new RatingsProvider();
		favoritesProvider = new UserFavoritesProvider();

		ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){

			@Override
			public void onRatingChanged(RatingBar rb, float rating, boolean fromUser) {
				// TODO Auto-generated method stub
				if(fromUser)
				{
					// TODO add if(recipeHasBeenRatedByUserAlready)
					ratingsProvider.AddRatingByRecipeID(selectedRecipe.getRecipeID(),(int) rating, username);
				}
			}
		});
		
		commentButton = (Button) findViewById(R.id.addComment);
		commentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addComment();
			}
		});
		
		favToggleButton = (ToggleButton) findViewById(R.id.favToggleButton);
		boolean pressed = favoritesProvider.IsRecipeAlreadyFavorited(username, selectedRecipe.getRecipeID());
		if(pressed)favToggleButton.toggle();
		favToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				if(isChecked) recipeResourceProvider.AddRecipeToFavorites(username, selectedRecipe.getRecipeID());
				else recipeResourceProvider.RemoveRecipeFromFavorites(username, selectedRecipe.getRecipeID());
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

		float rating = (float) ratingsProvider.FetchAverageRatingByRecipeID(selectedRecipe.getRecipeID());

		ratingBar.setRating(rating);

		//set comment adding fields
		commentTextField = (EditText) findViewById(R.id.commentTextField);

		DisplayComments();
		
	}
	
	private void DisplayComments()
	{
		//set comment display table
		commentsTable = (TableLayout)findViewById(R.id.tableLayout);
		
		ArrayList<Comment> comments = commentProvider.FetchCommentsByRecipe(selectedRecipe.getRecipeID());
		
		TableRow tableRow = new TableRow(this);
		
		TextView tableRowUsernameTitle = new TextView(this);
		tableRowUsernameTitle.setText("Username");
		tableRow.addView(tableRowUsernameTitle);

		TextView tableRowCommentBodyTitle = new TextView(this);
		tableRowCommentBodyTitle.setText("Comment");
		tableRow.addView(tableRowCommentBodyTitle);

		TextView tableRowCommentDateTitle = new TextView(this);
		tableRowCommentDateTitle.setText("Date");
		tableRow.addView(tableRowCommentDateTitle);
		
		commentsTable.addView(tableRow);
		for (int i = 0; i < comments.size(); i++) 
		{
			tableRow = new TableRow(this);
			
			TextView tableRowUsername = new TextView(this);
			tableRowUsername.setText(comments.get(i).GetUsername() + "\t\t");
			tableRow.addView(tableRowUsername);
			
			TextView tableRowCommentBody = new TextView(this);
			tableRowCommentBody.setText(comments.get(i).GetCommentBody() + "\t\t");
			tableRow.addView(tableRowCommentBody);
			
			TextView tableRowCommentDate = new TextView(this);
			Date d = comments.get(i).GetDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			String date = cal.get(Calendar.YEAR) + " - " + (cal.get(Calendar.MONTH)+1) + " - " + cal.get(Calendar.DAY_OF_MONTH);
			tableRowCommentDate.setText(date);
			tableRow.addView(tableRowCommentDate);
			
			commentsTable.addView(tableRow,i+1);
		}
	}
	
	public void addComment() 
	{
		commentProvider.AddCommentByRecipeID(selectedRecipe.getRecipeID(), username, commentTextField.getText().toString());
		commentTextField.setText("");
		commentsTable.removeAllViews();
		DisplayComments();
	}
}