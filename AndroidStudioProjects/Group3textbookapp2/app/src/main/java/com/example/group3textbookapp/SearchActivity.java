//SearchActivity
package com.example.group3textbookapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.content.Intent;

public class SearchActivity extends AppCompatActivity {

    EditText editTextSearchQuery;
    Spinner spinnerSearchType;
    Button buttonSearch;
    ListView listViewResults;

    TextbookDatabaseHelper dbHelper;
    ArrayAdapter<String> adapter;
    ArrayList<String> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editTextSearchQuery = findViewById(R.id.editTextSearchQuery);
        spinnerSearchType = findViewById(R.id.spinnerSearchType);
        buttonSearch = findViewById(R.id.buttonSearch);
        listViewResults = findViewById(R.id.listViewResults);

        dbHelper = new TextbookDatabaseHelper(this);
        resultList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultList);
        listViewResults.setAdapter(adapter);

        // Setup spinner options
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.search_types, android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSearchType.setAdapter(spinnerAdapter);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editTextSearchQuery.getText().toString().trim();
                String type = spinnerSearchType.getSelectedItem().toString();

                if (query.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Enter a search query", Toast.LENGTH_SHORT).show();
                    return;
                }

                resultList.clear();
                if (type.equals("Title")) {
                    resultList.addAll(dbHelper.searchByTitle(query));
                } else {
                    resultList.addAll(dbHelper.searchBySeller(query));
                }

                adapter.notifyDataSetChanged();
            }
        });

        listViewResults.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = resultList.get(position);

            Intent intent = new Intent(SearchActivity.this, TextbookDetailsActivity.class);
            intent.putExtra("textbookDetails", selectedItem); // Pass the whole string
            startActivity(intent);
        });

    }
}