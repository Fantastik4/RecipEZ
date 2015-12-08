package activities;

import objects.Comment;
import objects.Recipe;
import resources.CommentProvider;
import resources.RatingsProvider;
import resources.RecipeResourceProvider;
import resources.UserFavoritesProvider;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
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
	boolean isFavorite;
	CommentProvider commentProvider;
	RatingsProvider ratingsProvider;
	UserFavoritesProvider favoritesProvider;
	Typeface walkwaySemiBold, walkwayUltraBold;
	Button commentButton;
	EditText commentTextField;
	ImageView setToFavorite;

	ImageView favToggleButton;
	RecipeResourceProvider recipeResourceProvider;
	TableLayout commentsTable;
	TextView recipeDescription, recipeIngredients, recipeDirections, commentHint;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_recipe);

		username = (String) getIntent().getSerializableExtra("username");
		selectedRecipe = (Recipe) getIntent().getSerializableExtra("SelectedRecipe");

		/**
		 * Custom Font Creation
		 */
		walkwaySemiBold = Typeface.createFromAsset(getAssets(), "fonts/Walkway_SemiBold.ttf");
		walkwayUltraBold = Typeface.createFromAsset(getAssets(), "fonts/Walkway_UltraBold.ttf");

		/**
		 * Apply Custom Fonts
		 */
		recipeDescription = (TextView) findViewById(R.id.tv_recipeDescriptionLabel);
		recipeDescription.setTypeface(walkwaySemiBold);
		recipeIngredients = (TextView) findViewById(R.id.tv_ingredientsLabel);
		recipeIngredients.setTypeface(walkwaySemiBold);
		recipeDirections = (TextView) findViewById(R.id.tv_directionsLabel);
		recipeDirections.setTypeface(walkwaySemiBold);
		commentHint = (TextView) findViewById(R.id.commentTextField);
		commentHint.setTypeface(walkwayUltraBold);

		recipeResourceProvider = new RecipeResourceProvider();
		commentProvider = new CommentProvider();
		ratingsProvider = new RatingsProvider();
		favoritesProvider = new UserFavoritesProvider();

		commentButton = (Button) findViewById(R.id.addComment);
		commentButton.setTypeface(walkwayUltraBold);
		commentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addComment();
			}
		});

		favToggleButton = (ImageView) findViewById(R.id.favoriteSelection);
		isFavorite = favoritesProvider.IsRecipeAlreadyFavorited(username, selectedRecipe.getRecipeID());

		if (isFavorite == true)
			favToggleButton.setImageResource(R.drawable.favoritetrue);

		favToggleButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (isFavorite == false) {
					recipeResourceProvider.AddRecipeToFavorites(username, selectedRecipe.getRecipeID());
					favToggleButton.setImageResource(R.drawable.favoritetrue);
				} else {
					recipeResourceProvider.RemoveRecipeFromFavorites(username, selectedRecipe.getRecipeID());
					favToggleButton.setImageResource(R.drawable.favoritefalse);
				}
			}
		});

		ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar rb, float rating, boolean fromUser) {
				if (fromUser) {
					ratingsProvider.AddRatingByRecipeID(selectedRecipe.getRecipeID(), (int) rating, username);
				}
			}
		});

		displayRecipe();
	}

	public void displayRecipe() {

		String[] tokensForList, tokensForSteps;
		String list = "", steps = "", delim = "\",\"";

		// Display Recipe Name
		TextView tv1 = (TextView) findViewById(R.id.tv_recipeName);
		tv1.setTypeface(walkwaySemiBold);
		tv1.setText(selectedRecipe.getName());

		// Display Recipe Description
		TextView tv2 = (TextView) findViewById(R.id.tv_recipeDescription);
		tv2.setTypeface(walkwayUltraBold);
		tv2.setText(selectedRecipe.getRecipeDescription() + "\n");

		// Display Ingredients
		TextView tv3 = (TextView) findViewById(R.id.tv_recipeIngredients);
		tv3.setTypeface(walkwayUltraBold);

		String ingredients = selectedRecipe.getRecipeIngredients();
		tokensForList = ingredients.substring(2, ingredients.length() - 2).split(delim);

		for (int i = 0; i < tokensForList.length; i++) {
			if (i == 0)
				list = "• " + tokensForList[i] + "\n";
			else
				list = list + "• " + tokensForList[i] + "\n";
		}
		tv3.setText(list);

		// Display Recipe Directions
		TextView tv4 = (TextView) findViewById(R.id.tv_recipeDirections);
		tv4.setTypeface(walkwayUltraBold);

		String directions = selectedRecipe.getRecipeDirections();
		tokensForSteps = directions.substring(2, directions.length() - 2).split(delim);

		for (int i = 0; i < tokensForSteps.length; i++) {
			if (i == 0)
				steps = tokensForSteps[i] + "\n\n";
			else
				steps = steps + tokensForSteps[i] + "\n\n";
		}
		tv4.setText(steps);

		float rating = (float) ratingsProvider.FetchAverageRatingByRecipeID(selectedRecipe.getRecipeID());

		ratingBar.setRating(rating);

		// set comment adding fields
		commentTextField = (EditText) findViewById(R.id.commentTextField);

		DisplayComments();
	}

	@SuppressWarnings("deprecation")
	private void DisplayComments() {
		// set comment display table
		commentsTable = (TableLayout) findViewById(R.id.tableLayout);
		ArrayList<Comment> comments = commentProvider.FetchCommentsByRecipe(selectedRecipe.getRecipeID());

		TableRow tableRow = new TableRow(this);

		commentsTable.addView(tableRow);

		for (int i = 0; i < comments.size(); i++) {
			tableRow = new TableRow(this);

			TextView tableRowUsername = new TextView(this);
			tableRowUsername.setTypeface(walkwaySemiBold);
			tableRowUsername.setTextColor(getResources().getColor(R.color.darkBlue));
			tableRowUsername.setText(comments.get(i).GetUsername() + "\t\t" + "\n");
			tableRow.addView(tableRowUsername);

			TextView tableRowCommentBody = new TextView(this);
			tableRowCommentBody.setTypeface(walkwaySemiBold);
			tableRowCommentBody.setText(comments.get(i).GetCommentBody() + "\t\t");
			tableRow.addView(tableRowCommentBody);

			TextView tableRowCommentDate = new TextView(this);
			tableRowCommentDate.setTypeface(walkwaySemiBold);
			Date d = comments.get(i).GetDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			String date = cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "."
					+ cal.get(Calendar.DAY_OF_MONTH);
			tableRowCommentDate.setText(date);
			tableRow.addView(tableRowCommentDate);

			commentsTable.addView(tableRow, i + 1);
		}
	}

	public void addComment() {
		commentProvider.AddCommentByRecipeID(selectedRecipe.getRecipeID(), username,
				commentTextField.getText().toString());
		commentTextField.setText("");
		commentsTable.removeAllViews();
		DisplayComments();
	}
}