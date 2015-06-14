package com.firebase.androidchat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.base.CharMatcher;

/**
 * Created by FRANK LAPTOP on 13-6-2015.
 */
public class AddContactActivity extends MainActivity {


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_addcontact);
            EditText getOwnAndroidID = (EditText) findViewById(R.id.OwnAndroidID);
            getOwnAndroidID.setText(getPhoneId());

    }
    public void addContact(View v){
        EditText androidID = (EditText) findViewById(R.id.androidID);
        EditText name = (EditText) findViewById(R.id.name);
        Contacts contacts = new Contacts();
        if(androidID.getText().length() !=  16) {
            Toast.makeText(AddContactActivity.this, "Foutieve Android ID. Geen 16 tekens.", Toast.LENGTH_SHORT).show();
        } else if(CharMatcher.WHITESPACE.matchesAnyOf(androidID.getText())){
            Toast.makeText(AddContactActivity.this, "Foutieve Android ID. Spaties niet toegestaan.", Toast.LENGTH_SHORT).show();
        } else if(name.getText().length() == 0) {
            Toast.makeText(AddContactActivity.this, "Vergeet niet een naam in te vullen.", Toast.LENGTH_SHORT).show();
        } else {
            contacts.addContact(getApplicationContext(), androidID.getText().toString(), name.getText().toString());
            finish();
        }
    }




}
