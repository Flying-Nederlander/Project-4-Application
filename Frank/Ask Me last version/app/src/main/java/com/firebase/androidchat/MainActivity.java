package com.firebase.androidchat;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import android.provider.Settings.Secure;

import java.util.Random;



public class MainActivity extends ListActivity {


    private static final String FIREBASE_URL = "https://shining-inferno-1885.firebaseio.com/";

    private String mUsername;
    private Firebase mFirebaseRef;
    private Firebase contactInfo;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;
    String receivingUser = "d89e17cd9347da3d";
    String EmailAdress;
    String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat");

        setContentView(R.layout.activity_main);
        setupUsername();
        setTitle("Chatting as " + mUsername);
            }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void toggleSubjects(View v) {
        setContentView(R.layout.subjects);
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupItemListener());
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.activity_main, popup.getMenu());
        popup.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), this, R.layout.chat_message, mUsername, receivingUser, getPhoneId());
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    private void setupUsername() {
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        mUsername = prefs.getString("username", null);
        if (mUsername == null) {
            Random r = new Random();
            // Assign a random user name if we don't have one saved.
            mUsername = "JavaUser" + r.nextInt(100000);
            prefs.edit().putString("username", mUsername).commit();
        }
    }

    private void sendMessage(String input, String sendingUser, int id) {
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, sendingUser, id);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);


        }
    }

    class PopupItemListener implements PopupMenu.OnMenuItemClickListener {


        public boolean onMenuItemClick(MenuItem item) {
            if (!item.hasSubMenu()) {
                sendMessage(item.getTitle().toString(), getPhoneId(), item.getItemId());
            }
            return false;
        }
    }

    private String getPhoneId() {
        String android_id = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);
        return android_id;
    }

}



