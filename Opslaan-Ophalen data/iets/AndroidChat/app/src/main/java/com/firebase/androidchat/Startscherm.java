package com.firebase.androidchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import java.util.ArrayList;


public class Startscherm extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscherm);


        Button chatbutton = (Button) findViewById(R.id.chatbutton);
        Button contactbutton = (Button) findViewById(R.id.contactbutton);
        Button locationbutton = (Button) findViewById(R.id.locationbutton);
        Button questionsbutton = (Button) findViewById(R.id.questionsbutton);
        Button answersbutton = (Button) findViewById(R.id.answersbutton);
        Button settingsbutton = (Button) findViewById(R.id.settingsbutton);

        chatbutton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Startscherm.this, MainActivity.class);
                startActivity(intent);
            }

        });

        contactbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Startscherm.this, Contacts.class);
                startActivity(intent2);
            }
        });

        locationbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(Startscherm.this, Location.class);
                startActivity(intent3);
            }
        });

        questionsbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(Startscherm.this, Questions.class);
                startActivity(intent4);
            }
        });

        answersbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(Startscherm.this, Answers.class);
                startActivity(intent5);
            }
        });

        settingsbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent6 =  new Intent(Startscherm.this, Settings.class);
                startActivity(intent6);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_startscherm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
