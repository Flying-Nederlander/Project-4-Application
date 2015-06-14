package com.firebase.androidchat;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.common.base.CharMatcher;

import java.util.ArrayList;

/**
 * Created by FRANK LAPTOP on 12-6-2015.
 */

public class Contacts {
    private Firebase ref = new Firebase(MainActivity.SERVER_URL);
    ArrayList<String> getContactsID = new ArrayList<String>();
    public ArrayList<String> getContactsName(Context context) {
        ArrayList<String> getContactsName = new ArrayList<String>();
        ContentResolver contentResolver = context.getContentResolver(); //Activity/Application android.content.Context

        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {

            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phoneCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = " + id;
                    String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE };
                    Cursor nameCur = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME);
                    while (phoneCur.moveToNext() && nameCur.moveToNext()) {
                        String contactID = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String contactName = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
                        if (!CharMatcher.WHITESPACE.matchesAnyOf(contactID) && contactID.length() == 16) {
                            getContactsName.add(contactName);
                            getContactsID.add(contactID);
                        }
                        break;
                    }
                    phoneCur.close();
                    nameCur.close();
                }

            } while (cursor.moveToNext());
        }
        return getContactsName;
    }
    public ArrayList<String> getContactsID(int id){
        return getContactsID;
    }


    public void addContact(Context context, String userID, String contactName){
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactName) // Name of the person\
                .build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, userID) // Number of the person
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); // Type of mobile number

        try {
            ContentProviderResult[] res = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            ref.child("users").child(MainActivity.getPhoneId()).child(userID).child("room").setValue(MainActivity.getPhoneId() + "/" + userID);
            ref.child("users").child(userID).child(MainActivity.getPhoneId()).child("room").setValue(MainActivity.getPhoneId() + "/" + userID);

        } catch (RemoteException e) {
            // error
        } catch (OperationApplicationException e) {
            // error
        }
    }

}