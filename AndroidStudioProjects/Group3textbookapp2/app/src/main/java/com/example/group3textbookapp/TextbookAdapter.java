package com.example.group3textbookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class TextbookAdapter extends ArrayAdapter<Book> {

    public TextbookAdapter(Context context, ArrayList<Book> textbooks) {
        super(context, 0, textbooks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book textbook = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_textbook, parent, false);
        }

        TextView titleText = convertView.findViewById(R.id.titleText);
        TextView sellerText = convertView.findViewById(R.id.sellerText);
        TextView priceText = convertView.findViewById(R.id.priceText);
        TextView copiesText = convertView.findViewById(R.id.copiesText);

        titleText.setText(textbook.getTitle());
        sellerText.setText("Seller: " + textbook.getSellerName());
        priceText.setText("Price: $" + textbook.getPrice());
        copiesText.setText("Copies: " + textbook.getCopies());

        return convertView;
    }
}