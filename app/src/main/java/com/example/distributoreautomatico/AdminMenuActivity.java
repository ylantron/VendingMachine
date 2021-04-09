package com.example.distributoreautomatico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.distributoreautomatico.adapters.DrinkAdminAdapter;
import com.example.distributoreautomatico.business.Drink;
import com.example.distributoreautomatico.dao.DaoDrink;
import com.google.android.material.snackbar.Snackbar;

public class AdminMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        // adding the username from the intent to the welcome message
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText(getString(R.string.welcome_username, getIntent().getStringExtra("username")));

        // creating a dao for drinks
        DaoDrink daoDrink = DaoDrink.getInstance(getApplicationContext());

        // removing the "no results found" view if there are drinks in the database
        ConstraintLayout noResultsFound = findViewById(R.id.no_results_found);
        ListView drinksListView = findViewById(android.R.id.list);

        if (daoDrink.isNotEmpty()) {
            noResultsFound.setVisibility(View.GONE);
            drinksListView.setVisibility(View.VISIBLE);
        }

        // creating and setting the drink adapter
        DrinkAdminAdapter drinkAdminAdapter = new DrinkAdminAdapter(this, daoDrink.getAllDrinks());
        drinksListView.setPadding(10, 10, 10, 10);
        drinksListView.setAdapter(drinkAdminAdapter);

        // adding the click listener on the addDrink button
        Button addDrinkButton = findViewById(R.id.add_drink_button);
        addDrinkButton.setOnClickListener(v -> addDrink(drinkAdminAdapter));
    }

    private void addDrink(DrinkAdminAdapter drinkAdminAdapter) {
        // get all drink edit texts
        EditText addDrinkNameEditText = findViewById(R.id.add_drink_name_edit_text);
        EditText addDrinkCostEditText = findViewById(R.id.add_drink_cost_edit_text);
        EditText addDrinkQuantityEditText = findViewById(R.id.add_drink_quantity_edit_text);

        // check if the drink is valid (if it's not valid don't go further)
        if (!drinkAdminAdapter.isDrinkValid(addDrinkNameEditText, addDrinkCostEditText, addDrinkQuantityEditText))
            return;

        // extracting text from edit texts
        String drinkName = addDrinkNameEditText.getText().toString();
        String drinkCostStr = addDrinkCostEditText.getText().toString();
        String drinkQuantityStr = addDrinkQuantityEditText.getText().toString();

        // converting cost and quantity from string
        float drinkCost = Float.parseFloat(drinkCostStr);
        int drinkQuantity = Integer.parseInt(drinkQuantityStr);

        // instantiate a dao for drinks
        DaoDrink daoDrink = DaoDrink.getInstance(getApplicationContext());

        // build a new drink with the edit text information
        Drink drink = new Drink(drinkName, drinkQuantity, drinkCost);

        // try and add a drink to the database, show a message based on the result
        if (daoDrink.addDrink(drink)) {
            Snackbar.make(findViewById(android.R.id.content).getRootView(), getString(R.string.successful_drink_add_message), Snackbar.LENGTH_SHORT).show();

            // clean the edit texts
            addDrinkNameEditText.setText("");
            addDrinkCostEditText.setText("");
            addDrinkQuantityEditText.setText("");
        } else Snackbar.make(findViewById(android.R.id.content).getRootView(), getString(R.string.error_drink_add_message), Snackbar.LENGTH_SHORT).show();

        // removes the "no results found" view if it's still active
        ConstraintLayout noResultsFound = findViewById(R.id.no_results_found);
        noResultsFound.setVisibility(View.GONE);

        ListView drinksListView = findViewById(android.R.id.list);
        drinksListView.setVisibility(View.VISIBLE);

        // update the list
        drinkAdminAdapter.updateList();
    }
}