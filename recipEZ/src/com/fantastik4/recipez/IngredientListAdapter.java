package com.fantastik4.recipez;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class IngredientListAdapter extends ArrayAdapter<String> {

	private ArrayList<String> ingredients;
	private Context context;
	private int resource;
	
	public IngredientListAdapter(Context context, int resource, ArrayList<String> ingredients) {
		super(context, resource, ingredients);

		this.context = context;
		this.resource = resource;
		this.ingredients = ingredients;
	}
}
