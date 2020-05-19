package com.gdfdfdfdfd.contactapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ListItems> listContact = new ArrayList<>() ;
    public static ListView listView;
    SearchView searchViewSearch;
    private ImageButton imageButtonAdd;
    public static String AVATAR = "avatar";
    public static String NAME = "name";
    public static String PHONE = "phone";
    public static String POSITION = "position";
    public static String LISTKEY = "listKey";
    public static String LISTCONTACT = "listContact";
    MyDatabaseHelper DB = new MyDatabaseHelper(this);
    CustomAdapter adapter ;
    //public static String TAG = "FIREBASE";
    ArrayList<String> listKey = new ArrayList<>();
    public static String TAG = MainActivity.class.getSimpleName();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitKey();
        checkAndRequestPermissions();
        SearchContact();
        ShowListView();
        CallAddContactActivity();
        ShowDetailContact();
        if(isOnline()){
            Toast toast = Toast.makeText(MainActivity.this, "Internet access",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
        else {
            Toast toast = Toast.makeText(MainActivity.this, "No Internet",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }


    }

    public void InitKey() {
        listView = findViewById(R.id.lv_contact);
        imageButtonAdd = findViewById(R.id.img_add);
        searchViewSearch = findViewById(R.id.edt_search);
       // listContact.add(new ListItems(true,"Anhd","12345"));


    }

    public void CallContact(View v) {
        View parentRow = (View) v.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + listContact.get(position).getPhone()));
        startActivity(intent);

    }

    public void ShowListView() {
        final DatabaseReference myRef = database.child("contacts");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(adapter!=null)adapter.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    ListItems items = data.getValue(ListItems.class);
                    listContact.add(items);
                    listKey.add(data.getKey());
                    Log.e(TAG,data.getValue().toString());
                }
                adapter = new CustomAdapter(MainActivity.this, R.layout.list_items_contact, listContact);
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
//        database.addChildEventListener((new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                adapter.clear();
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    ListItems items = data.getValue(ListItems.class);
//                    listContact.add(items);
//                    listKey.add(data.getKey());
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        }));
    }

    public void CallAddContactActivity() {

        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                intent.putParcelableArrayListExtra(LISTCONTACT, listContact);
                startActivity(intent);
                finish();
            }
        });
    }

    public void ShowDetailContact() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItems item = (ListItems) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, DetailContact.class);
                intent.putExtra(AVATAR, item.getAvatar());
                intent.putExtra(NAME, item.getName());
                intent.putExtra(PHONE, item.getPhone());
                intent.putExtra(POSITION, position);
                intent.putExtra(LISTKEY, listKey);
                intent.putParcelableArrayListExtra(LISTCONTACT, listContact);
                startActivity(intent);
                finish();

            }
        });
    }

    public void SearchContact() {
        searchViewSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText.trim());
                adapter.notifyDataSetChanged();
                Log.d("SearchView", newText);
                return false;
            }
        });


//        {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.filter(newText);
//                adapter.notifyDataSetChanged();
//                return false;
//            }
//        });
    }

    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }
    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }
}
