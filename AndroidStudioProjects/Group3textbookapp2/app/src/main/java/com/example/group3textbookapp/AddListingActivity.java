//AddListActivity
package com.example.group3textbookapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddListingActivity extends AppCompatActivity {

    private EditText titleInput, sellerInput, priceInput, copiesInput, bankInput;
    private TextbookDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);

        titleInput = findViewById(R.id.editTextTitle);
        sellerInput = findViewById(R.id.editTextSeller);
        priceInput = findViewById(R.id.editTextPrice);
        copiesInput = findViewById(R.id.editTextCopies);
        bankInput = findViewById(R.id.editTextBankInfo);
        Button submitBtn = findViewById(R.id.buttonSubmit);

        dbHelper = new TextbookDatabaseHelper(this);

        submitBtn.setOnClickListener(v -> addTextbook());
    }

    private void addTextbook() {
        String title = titleInput.getText().toString().trim();
        String seller = sellerInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();
        String copiesStr = copiesInput.getText().toString().trim();
        String bank = bankInput.getText().toString().trim();




        if (title.isEmpty() || seller.isEmpty() || priceStr.isEmpty() || copiesStr.isEmpty() || bank.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int copies = Integer.parseInt(copiesStr);

            Book book = new Book(title, seller, copies, price, bank);

            boolean success = dbHelper.insertTextbook(title, seller, price, copies, bank);

            if (success) {
                Toast.makeText(this, "Textbook listed successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close activity
            } else {
                Toast.makeText(this, "Duplicate listing exists", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
        }
    }
}
