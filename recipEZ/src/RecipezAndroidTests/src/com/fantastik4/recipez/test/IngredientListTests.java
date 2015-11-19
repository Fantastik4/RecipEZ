package com.fantastik4.recipez.test;

import static org.junit.Assert.fail;


import android.R;
import android.content.Intent;
import android.test.*;
import android.widget.ListView;
import android.widget.SearchView;

import org.junit.Test;

import activities.IngredientSearchActivity;
import resources.IngredientProvider;

public class IngredientListTests extends ActivityUnitTestCase{
	
	public IngredientListTests() {
		super(IngredientSearchActivity.class);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		
		Intent mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), IngredientSearchActivity.class);
        startActivity(mLaunchIntent, null, null);
        
	}

	public void testNotNull(){
		SearchView sv = (SearchView) getActivity().findViewById(com.fantastik4.recipez.R.id.sv_searchIngredients);
		
		assertNotNull("Search list is empty",sv.getChildAt(0));
	}
}
