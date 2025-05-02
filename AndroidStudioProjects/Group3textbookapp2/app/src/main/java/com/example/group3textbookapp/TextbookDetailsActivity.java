package com.example.group3textbookapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TextbookDetailsActivity extends AppCompatActivity {

    TextView textViewTitle, textViewSeller, textViewPrice, textViewCopies, textViewBank;
    Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textbook_details);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewSeller = findViewById(R.id.textViewSeller);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewCopies = findViewById(R.id.textViewCopies);
        textViewBank = findViewById(R.id.textViewBank);
        buttonBack = findViewById(R.id.buttonBack);

        String details = getIntent().getStringExtra("textbookDetails");

        if (details != null) {
            String[] parts = details.split("\n");
            for (String part : parts) {
                if (part.startsWith("Title:")) {
                    textViewTitle.setText(part);
                } else if (part.startsWith("Seller:")) {
                    textViewSeller.setText(part);
                } else if (part.startsWith("Price:")) {
                    textViewPrice.setText(part);
                } else if (part.startsWith("Copies:")) {
                    textViewCopies.setText(part);
                } else if (part.startsWith("Bank Info:")) {
                    textViewBank.setText(part);
                }
            }
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the details activity
            }
        });
    }
}
