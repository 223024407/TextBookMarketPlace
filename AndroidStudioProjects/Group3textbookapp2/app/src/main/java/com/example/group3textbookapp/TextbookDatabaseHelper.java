package com.example.group3textbookapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TextbookDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "textbooks.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "textbooks";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_SELLER = "seller";
    public static final String COL_COPIES = "copies";
    public static final String COL_PRICE = "price";
    public static final String COL_BANK = "bank_info";  // fixed

    public TextbookDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT NOT NULL, " +
                COL_SELLER + " TEXT NOT NULL, " +
                COL_PRICE + " REAL NOT NULL, " +
                COL_COPIES + " INTEGER NOT NULL, " +
                COL_BANK + " TEXT NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertTextbook(Book book) {
        return insertTextbook(book.getTitle(), book.getSeller(), book.getPrice(), book.getCopies(), book.getBankInfo());
    }

    public boolean insertTextbook(String title, String seller, double price, int copies, String bankInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if a textbook with the same title and seller already exists
        String checkQuery = "SELECT * FROM textbooks WHERE title=? AND seller=?";
        Cursor cursor = db.rawQuery(checkQuery, new String[]{title, seller});

        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return false; // Duplicate entry detected
        }

        cursor.close();

        ContentValues values = new ContentValues();
        values.put("title", title.trim());
        values.put("seller", seller.trim());
        values.put("price", Math.max(price, 0));  // Prevent negative price values
        values.put("copies", Math.max(copies, 1)); // Ensure at least 1 copy is listed
        values.put("bank_info", bankInfo.trim());

        long result = db.insert("textbooks", null, values);
        db.close();

        return result != -1; // Returns true if insert succeeded
    }


    public boolean isDuplicate(Book book) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COL_ID},
                COL_TITLE + " = ? AND " + COL_SELLER + " = ?",
                new String[]{book.getTitle(), book.getSeller()},
                null, null, null
        );
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public ArrayList<Book> searchTextbooks(String keyword) {
        ArrayList<Book> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COL_TITLE + " LIKE ? OR " + COL_SELLER + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + keyword + "%", "%" + keyword + "%"});
        if (cursor.moveToFirst()) {
            do {
                list.add(new Book(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SELLER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_COPIES)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BANK))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<String> getAllTextbooks() {
        ArrayList<String> textbooks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT title, seller, price, copies FROM textbooks", null);

        if (cursor.moveToFirst()) {
            do {
                String entry = cursor.getString(0) + " by " + cursor.getString(1) +
                        " - R" + cursor.getDouble(2) + " (" + cursor.getInt(3) + " copies)";
                textbooks.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return textbooks;
    }



    public ArrayList<String> searchByTitle(String title) {
        ArrayList<String> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_TITLE + " LIKE ?", new String[]{"%" + title + "%"});

        if (cursor.moveToFirst()) {
            do {
                results.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)) +
                        " by " + cursor.getString(cursor.getColumnIndexOrThrow(COL_SELLER)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return results;
    }

    public String getBankInfo(String title, String seller) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT bank_info FROM " + TABLE_NAME + " WHERE title=? AND seller=?", new String[]{title, seller});

        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        cursor.close();
        db.close();
        return "Bank info not found";
    }


    public ArrayList<String> searchBySeller(String seller) {
        ArrayList<String> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_SELLER + " LIKE ?", new String[]{"%" + seller + "%"});

        if (cursor.moveToFirst()) {
            do {
                results.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)) +
                        " by " + cursor.getString(cursor.getColumnIndexOrThrow(COL_SELLER)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return results;
    }
}
