package com.gdfdfdfdfd.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddContactActivity extends AppCompatActivity {
    private ImageButton img_Approve;
    private ImageButton img_Cancel;
    private EditText edt_Name;
    private EditText edt_Phone;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    public ListView listView;
    ArrayList<ListItems> listItems = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        ClickButtonAdd();
        ClickButtonCancel();

    }

    public void addContactDatabase() {

        edt_Name = findViewById(R.id.edt_name);
        edt_Phone = findViewById(R.id.edt_phone);
        listView = findViewById(R.id.lv_contact);

        Intent intent = getIntent();
        listItems = intent.getParcelableArrayListExtra(MainActivity.LISTCONTACT);
        MyDatabaseHelper DB = new MyDatabaseHelper(this);


        if (edt_Name.getText().toString().length() > 0 && edt_Phone.getText().toString().length() > 0) {

            for (ListItems items : listItems) {
                if (items.getPhone().equals(edt_Phone.getText().toString())) {
                    Toast.makeText(AddContactActivity.this, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            add();

        } else {
            Toast.makeText(this, "No Information!", Toast.LENGTH_SHORT).show();
        }
    }


    Random random = new Random();

    public void add() {
        DatabaseReference postsRef = database.child("contacts");
        DatabaseReference newPostRef = postsRef.push();
        newPostRef.setValue(new ListItems(random.nextBoolean(), edt_Name.getText().toString(), edt_Phone.getText().toString()));
        Intent intent = new Intent(AddContactActivity.this, MainActivity.class);
        startActivity(intent);
        finish();


    }

    public void ClickButtonAdd() {
        img_Approve = findViewById(R.id.ic_approve);

        // final int count = DB.getContactCount();
        img_Approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContactDatabase();
            }
        });
    }

    public void ClickButtonCancel() {
        img_Cancel = findViewById(R.id.ic_cancel);
        img_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddContactActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
