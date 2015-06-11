package com.firebase.androidchat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class MainActivity extends Activity{
    private static String room;
    private Firebase ref;
    private String receivingUser = "a675feec052ac570";
    private Firebase create;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        create = new Firebase("https://shining-inferno-1885.firebaseio.com/users/" + getPhoneId() + "/" + receivingUser + "/room");
        create = new Firebase("https://shining-inferno-1885.firebaseio.com/chat/" + getPhoneId() + "/" + receivingUser);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ref = new Firebase("https://shining-inferno-1885.firebaseio.com/users/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                room = dataSnapshot.child(getPhoneId() + "/" + receivingUser + "/" + "room").getValue().toString();
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    String getPhoneId() {
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }
    static String getRoom() {
        return room;
    }
}



