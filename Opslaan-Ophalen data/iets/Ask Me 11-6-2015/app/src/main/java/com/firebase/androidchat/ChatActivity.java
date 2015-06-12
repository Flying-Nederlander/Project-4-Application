package com.firebase.androidchat;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Random;

/**
 * Created by FRANK LAPTOP on 11-6-2015.
 */
public class ChatActivity extends ListActivity {


    private static final String FIREBASE_URL = "https://shining-inferno-1885.firebaseio.com/";

    private String mUsername;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;
    String receivingUser = "d89e17cd9347da3d";
    private Firebase ref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        setupUsername();
        setTitle("Chatting as " + mUsername);
        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat/" + MainActivity.getRoom());



    }



    public void toggleSubjects(View v) {
        setContentView(R.layout.subjects);
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupItemListener());
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.ask_menu, popup.getMenu());
        popup.show();

    }

    public void showContacts(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new ChooseContact());
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.contacts_menu, popup.getMenu());
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
                    Toast.makeText(ChatActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChatActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
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

    String getPhoneId() {
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }

    class ChooseContact implements PopupMenu.OnMenuItemClickListener {
        public boolean onMenuItemClick(MenuItem item) {
            if (!item.hasSubMenu()) {
                receivingUser = String.valueOf(item.getItemId());
            }
            return false;
        }
    }
}