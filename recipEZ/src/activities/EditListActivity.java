package activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import objects.CheckedListViewAdapter;
import objects.Ingredient;
import objects.Model;

import java.util.ArrayList;

import com.fantastik4.recipez.R;
import resources.IngredientProvider;
import com.fantastik4.recipez.R.layout;

import android.app.Activity;

public class EditListActivity extends Activity {

	
	SearchView searchField;
	Button findRecipes, addToFridge;
	String userID;
	ArrayList<Ingredient> ingredients;
	IngredientProvider ingredientProvider;
	CheckedListViewAdapter ingredientListAdapter;
	ArrayList<Model> displayedIngredients, ingredientNames;
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_list);
		
		userID = (String) getIntent().getSerializableExtra("Currentusername");
		ingredientProvider = new IngredientProvider();
		PopulateIngredientList();
	}
	
	private void PopulateIngredientList() {
		listView = (ListView)findViewById(R.id.lv_userList);		

		ingredients = ingredientProvider.FetchIngredientsByUserID(userID);
		ingredientNames = ConvertIngredientsToModelArray(ingredients);
		displayedIngredients = (ArrayList<Model>) ingredientNames.clone();
		ingredientListAdapter = new CheckedListViewAdapter(this, displayedIngredients);

		listView.setAdapter(ingredientListAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	private ArrayList<Model> ConvertIngredientsToModelArray(ArrayList<Ingredient> ingredients) {
		ArrayList<Model> ingredientNames = new ArrayList<Model>();
		Model temp;
		for(Ingredient i: ingredients) {
			temp = new Model(i.getName());
			temp.setSelected(false);
			ingredientNames.add(temp);
		}

		return ingredientNames;
	}
}
