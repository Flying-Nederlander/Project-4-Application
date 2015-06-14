package com.firebase.androidchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class MainActivity extends Activity{
    private static String room;
    private Firebase ref;
    static String SERVER_URL = "https://shining-inferno-1885.firebaseio.com";
    private String receivingUser;
    private Contacts contacts = new Contacts();
    private static String PhoneID;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ref = new Firebase(SERVER_URL + "/users/");


        ListView lv = (ListView) findViewById(R.id.contactList);
        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                contacts.getContactsName(getApplicationContext()));

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                receivingUser = contacts.getContactsID.get((int) id);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            room = dataSnapshot.child(getPhoneId() + "/" + receivingUser + "/" + "room").getValue().toString();
                            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                            startActivity(intent);
                        } catch(NullPointerException e) {
                            ref.child(getPhoneId()).child(receivingUser).child("room").setValue(MainActivity.getPhoneId() + "/" + receivingUser);
                            ref.child(receivingUser).child(getPhoneId()).child("room").setValue(MainActivity.getPhoneId() + "/" + receivingUser);
                            room = getPhoneId() + "/" + receivingUser;
                            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }
        });
        setPhoneId();
    }

    void setPhoneId() {
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        PhoneID = android_id;
    }

    static String getPhoneId() {
        return PhoneID;
    }

    static String getRoom() {
        return room;
    }

    static String getSERVER_URL() {
        return SERVER_URL;
    }

    public void showAddContacts(View v) {
        Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
        startActivity(intent);
    }

}



