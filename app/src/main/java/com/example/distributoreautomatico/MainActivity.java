package com.example.distributoreautomatico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adding a click listener on the login button
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> login());

        // Adding a click listener on the guest checkbox
        CheckBox continueAsGuestCheckbox = findViewById(R.id.continueAsGuestCheckbox);
        continueAsGuestCheckbox.setOnClickListener(v -> checkboxBehaviour());
    }

    private void login() {
        // get username edit text and guest checkbox
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        CheckBox continueAsGuestCheckbox = findViewById(R.id.continueAsGuestCheckbox);

        // extract username string from edit text
        String username = usernameEditText.getText().toString();

        // if username edit text is empty (only when the "continue as guest" checkbox is unchecked) display an error message
        if (username.isEmpty() && !continueAsGuestCheckbox.isChecked()) {
            usernameEditText.setError(getString(R.string.empty_username_error));
            return;
        }

        // Creating a new intent based if the "Continue as Guest" checkbox is checked
        Intent intent = new Intent(getApplicationContext(), !continueAsGuestCheckbox.isChecked() ? AdminMenuActivity.class : GuestMenuActivity.class);
        intent.putExtra("username", !continueAsGuestCheckbox.isChecked() ? username : getString(R.string.guest_name));

        // Clear the username EditText text and error messages
        usernameEditText.setError(null);

        // start the second activity
        startActivity(intent);
    }

    private void checkboxBehaviour() {
        // get username edit text and guest checkbox
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        CheckBox continueAsGuestCheckbox = findViewById(R.id.continueAsGuestCheckbox);

        // enables or disables the username EditText based on the checkbox
        usernameEditText.setEnabled(!continueAsGuestCheckbox.isChecked());
        usernameEditText.setInputType(!continueAsGuestCheckbox.isChecked() ? InputType.TYPE_TEXT_VARIATION_PERSON_NAME : InputType.TYPE_NULL);
    }
}