package com.example.group3textbookapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BrowseActivity extends AppCompatActivity {
    TextbookDatabaseHelper dbHelper;
    ArrayList<String> textbookList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        ListView listViewTextbooks = findViewById(R.id.listViewTextbooks); // Correct ID
        dbHelper = new TextbookDatabaseHelper(this);
        textbookList = dbHelper.getAllTextbooks();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, textbookList);
        listViewTextbooks.setAdapter(adapter);

        listViewTextbooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = textbookList.get(position);

                // Example format: "Intro to Java by John - R200.0 (3 copies)"
                // Split the line to extract parts
                try {
                    String[] firstSplit = selectedItem.split(" by ");
                    String title = firstSplit[0];
                    String[] secondSplit = firstSplit[1].split(" - R");
                    String seller = secondSplit[0];
                    String[] thirdSplit = secondSplit[1].split(" \\(");
                    String price = thirdSplit[0];
                    String copies = thirdSplit[1].replace(" copies)", "");

                    // Bank info isn't included in the string — you'd need to fetch full Book from DB
                    String bank = dbHelper.getBankInfo(title, seller); // Implement this

                    Intent intent = new Intent(BrowseActivity.this, TextbookDetailsActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("seller", seller);
                    intent.putExtra("price", price);
                    intent.putExtra("copies", copies);
                    intent.putExtra("bank", bank);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
