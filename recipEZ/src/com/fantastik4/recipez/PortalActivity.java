package com.fantastik4.recipez;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class PortalActivity extends Activity {

	ImageButton searchRecipes, searchIngredients, editList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_portal);
		
		editList = (ImageButton)findViewById(R.id.btn_editListOption);
		searchRecipes = (ImageButton)findViewById(R.id.btn_searchRecipesOption);
		searchIngredients = (ImageButton)findViewById(R.id.btn_searchIngredientsOption);
		
		/**
		 * Option 1: User chooses to search for recipes based on his current list of ingredients
		 */
		searchRecipes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(PortalActivity.this, SearchRecipesActivity.class);
				
				startActivity(i);
				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}
			
		});
		
		/**
		 * Option 2: User chooses to search for ingredients
		 */
		searchIngredients.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(PortalActivity.this, SearchActivity.class);
				
				startActivity(i);
				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}
			
		});
		
		/**
		 * Option 3: User chooses to edit his list of ingredients
		 */
		editList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(PortalActivity.this, EditListActivity.class);
				
				startActivity(i);
				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			}
		});
	}
	
	/**
	 * Disables onBackPressd
	 */
	public void onBackPressed() {
		// Do nothing
	}
}
