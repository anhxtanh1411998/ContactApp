package com.gdfdfdfdfd.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class DetailContact extends AppCompatActivity {
    public EditText editTextDetailName;
    public EditText editTextDetailPhone;
    public ImageView imageViewDetailAva;

    private ImageButton imageButtonDelete;
    private ImageButton buttonEdit;
    private Button buttonSave;
    private Button buttonCancel;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<String> listKeys = new ArrayList<>();
    ArrayList<ListItems> listItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        InitBtn();
        Show();
        ClickButtonDelete();
        ClickButtonEdit();
//
//        DB.deleteContact(items);
    }

    public void InitBtn() {
        editTextDetailName = findViewById(R.id.edt_showname);
        editTextDetailPhone = findViewById(R.id.edt_showphone);
        imageViewDetailAva = findViewById(R.id.imgbtn_avatar);
        imageButtonDelete = findViewById(R.id.imgbtn_delete);
        buttonEdit = findViewById(R.id.bt_edit);
        buttonSave = findViewById(R.id.bt_save);
        buttonCancel = findViewById(R.id.bt_canceldetail);

    }


    public void Show() {
        Intent intent = getIntent();
        if (intent.getBooleanExtra(MainActivity.AVATAR, true) == true) {
            imageViewDetailAva.setImageResource(R.drawable.man);
        } else {
            imageViewDetailAva.setImageResource(R.drawable.woman);
        }

        editTextDetailName.setText(intent.getStringExtra(MainActivity.NAME));
        editTextDetailPhone.setText(intent.getStringExtra(MainActivity.PHONE));

    }

    public void DeleteContact() {
        final Intent intent = getIntent();
        final int pos = intent.getIntExtra(MainActivity.POSITION, 0);
        listKeys = intent.getStringArrayListExtra(MainActivity.LISTKEY);
        final DatabaseReference reference = database.child("contacts");
        reference.child(listKeys.get(pos)).removeValue();
        Intent intent1 = new Intent(DetailContact.this, MainActivity.class);
        startActivity(intent1);
        finish();
    }

    public void ClickButtonDelete() {
        final MyDatabaseHelper DB = new MyDatabaseHelper(this);
        // Toast.makeText(DetailContact.this,listKeys.get(listPos[0]),Toast.LENGTH_LONG).show();
        imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteContact();
            }
        });
    }

    public void ClickButtonEdit() {

        final Intent intent = getIntent();
        final int pos = intent.getIntExtra(MainActivity.POSITION, 0);
        listKeys = intent.getStringArrayListExtra(MainActivity.LISTKEY);
        final DatabaseReference reference = database.child("contacts/" + listKeys.get(pos));
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextDetailName.setEnabled(true);
                editTextDetailPhone.setEnabled(true);
                buttonSave.setEnabled(true);


            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartActivity();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(editTextDetailName.getText()) && !TextUtils.isEmpty(editTextDetailPhone.getText())) {
                    reference.child("name").setValue(editTextDetailName.getText().toString());
                    reference.child("phone").setValue(editTextDetailPhone.getText().toString());
                    StartActivity();
                } else {
                    Toast.makeText(DetailContact.this, "lack of information", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public void StartActivity() {
        Intent intent = new Intent(DetailContact.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
