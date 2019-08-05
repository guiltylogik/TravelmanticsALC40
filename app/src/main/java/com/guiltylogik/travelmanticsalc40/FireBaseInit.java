package com.guiltylogik.travelmanticsalc40;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FireBaseInit {

    public static FirebaseDatabase mFireDb;
    public static DatabaseReference mDbRef;
    public static FirebaseStorage mFbStor;
    public static StorageReference mStorRef;
    public static ArrayList<TravelDeal> mTDs;
    private static FireBaseInit mFireBaseInit;


    private FireBaseInit(){}

    public static void FirebaseRef(String ref){

        if(mFireBaseInit == null){
            mFireBaseInit = new FireBaseInit();
            mFireDb = FirebaseDatabase.getInstance();
        }

        mTDs = new ArrayList<TravelDeal>();
        mDbRef = mFireDb.getReference().child(ref);

        storConnect();

    }

    public static void storConnect(){
        mFbStor = FirebaseStorage.getInstance();
        mStorRef = mFbStor.getReference().child("deal_pictures");
    }
}
