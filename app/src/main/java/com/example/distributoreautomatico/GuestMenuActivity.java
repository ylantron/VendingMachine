package com.example.distributoreautomatico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.distributoreautomatico.adapters.DrinkGuestAdapter;
import com.example.distributoreautomatico.dao.DaoDrink;

public class GuestMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu);

        // adding the username from the intent to the welcome message
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText(getString(R.string.welcome_username, getIntent().getStringExtra("username")));

        // creating a dao for drinks
        DaoDrink daoDrink  = DaoDrink.getInstance(getApplicationContext());

        // removing the "no results found" view if there are drinks in the database
        ConstraintLayout noResultsFound = findViewById(R.id.no_results_found);
        ListView drinksListView = findViewById(android.R.id.list);

        if (daoDrink.isNotEmpty()) {
            noResultsFound.setVisibility(View.GONE);
            drinksListView.setVisibility(View.VISIBLE);
        }

        // creating and setting the drink adapter
        DrinkGuestAdapter drinkGuestAdapter = new DrinkGuestAdapter(this, daoDrink.getAllDrinks());
        drinksListView.setPadding(10, 10, 10, 10);
        drinksListView.setAdapter(drinkGuestAdapter);
    }
}