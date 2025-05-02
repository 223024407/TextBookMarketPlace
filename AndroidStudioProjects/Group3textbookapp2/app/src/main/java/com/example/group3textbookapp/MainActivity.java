package com.example.group3textbookapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextSeller, editTextPrice, editTextCopies, editTextBankInfo;
    private Button buttonSubmit;
    private TextbookDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing); // Ensure correct layout

        dbHelper = new TextbookDatabaseHelper(this);

        // Initialize views
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextSeller = findViewById(R.id.editTextSeller);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextCopies = findViewById(R.id.editTextCopies);
        editTextBankInfo = findViewById(R.id.editTextBankInfo);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Button click listener for submitting a listing
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitListing();
            }
        });

        // Navigate to BrowseActivity
        Button buttonBrowse = findViewById(R.id.buttonBrowse);
        buttonBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BrowseActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to SearchActivity
        Button buttonGoToSearch = findViewById(R.id.buttonGoToSearch);
        buttonGoToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void submitListing() {
        String title = editTextTitle.getText().toString().trim();
        String seller = editTextSeller.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        String copiesStr = editTextCopies.getText().toString().trim();
        String bankInfo = editTextBankInfo.getText().toString().trim();

        // Validate input
        if (title.isEmpty() || seller.isEmpty() || priceStr.isEmpty() || copiesStr.isEmpty() || bankInfo.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int copies = Integer.parseInt(copiesStr);

            // Create Book object from user input
            Book book = new Book(title, seller, copies, price, bankInfo);

            // Check for duplicates in the database
            boolean exists = dbHelper.isDuplicate(book);
            if (exists) {
                Toast.makeText(this, "This book is already listed", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert new textbook listing into database
            boolean inserted = dbHelper.insertTextbook(book);
            if (inserted) {
                Toast.makeText(this, "Listing added successfully!", Toast.LENGTH_SHORT).show();
                clearFields();
                editTextTitle.requestFocus();
            } else {
                Toast.makeText(this, "Failed to add listing", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        editTextTitle.setText("");
        editTextSeller.setText("");
        editTextPrice.setText("");
        editTextCopies.setText("");
        editTextBankInfo.setText("");
    }
}
