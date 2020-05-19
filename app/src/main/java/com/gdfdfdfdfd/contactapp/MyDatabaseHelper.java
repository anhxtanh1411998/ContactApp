package com.gdfdfdfdfd.contactapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "HELLO_WORLD ";
    private static String TABLE_CONTACT = "phone_contact ";
    private static String COLUMN_GENDER = "gender ";
    private static String COLUMN_NAME = "name ";
    private static String COLUMN_PHONE = "phone_number ";

    private Context context;


    public MyDatabaseHelper(Context context){
        super(context, DATABASE_NAME,null,1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String Script = "CREATE TABLE " + TABLE_CONTACT + "(" + COLUMN_GENDER + "Boolean," + COLUMN_NAME + "TEXT," + COLUMN_PHONE +"TEXT"+")";
        sqLiteDatabase.execSQL(Script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);

    }

    public  void addContact(ListItems listItems){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, listItems.getName());
        values.put(COLUMN_GENDER, listItems.getAvatar());
        values.put(COLUMN_PHONE, listItems.getPhone());
        sqLiteDatabase.insert(TABLE_CONTACT,null,values);
        sqLiteDatabase.close();
    }

    public ArrayList<ListItems> getAllContact() {
        ArrayList<ListItems> listItems = new ArrayList<>();
        String Select = "Select * from " + TABLE_CONTACT;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Select, null);
        if (cursor.moveToFirst()) {
            do {
                ListItems contact = new ListItems();
                contact.setAvatar(Boolean.parseBoolean(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhone(cursor.getString(2));
                listItems.add(contact);

            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return listItems;
    }
    public void deleteContact(ListItems listItems) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_CONTACT, COLUMN_PHONE + " = ?",
                new String[] { String.valueOf(listItems.getPhone()) });
        sqLiteDatabase.close();
    }
    public void Upgrate(ListItems listItems, String PhoneNumber){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE,listItems.getPhone());
        sqLiteDatabase.update(TABLE_CONTACT,values,COLUMN_PHONE + " = ?", new String[]{String.valueOf(listItems.getPhone())});
        sqLiteDatabase.close();
    }

    public void DeleteAll(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

    }

    public int getContactCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
