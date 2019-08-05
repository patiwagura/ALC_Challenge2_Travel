package com.pato.travelmantics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtil {

    // avoid code repetition.
    //this class creates an instance of firebase database and gets a databasereference

    public static FirebaseDatabase firebaseDb;
    public static DatabaseReference dbReference;
    private static FirebaseUtil firebaseUtil;
    public static ArrayList<TravelDeal> mDeals;

    //private constructor, prevent creating an instance of this class from outside this class.
    private FirebaseUtil(){}

    //this method opens a firebase_DB_reference of the child passed as a parameter.
    public static void openFbReference(String childRef){
        if(firebaseUtil == null){
            firebaseUtil = new FirebaseUtil(); //instance of firebaseUtil.
            firebaseDb = FirebaseDatabase.getInstance();
            mDeals = new ArrayList<TravelDeal>();
        }

        dbReference = firebaseDb.getReference().child(childRef);

    }



}
