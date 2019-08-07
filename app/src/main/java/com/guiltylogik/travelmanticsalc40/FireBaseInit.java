package com.guiltylogik.travelmanticsalc40;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FireBaseInit {

    public static final int RC_SIGN_IN = 12;
    public static FirebaseDatabase mFireDb;
    public static DatabaseReference mDbRef;
    public static FirebaseStorage mFbStor;
    public static StorageReference mStorRef;
    public static FirebaseAuth mFbAuth;
    public static  FirebaseAuth.AuthStateListener mAuthListener;

    public static ArrayList<TravelDeal> mTDs;
    private static FireBaseInit mFireBaseInit;
    private static DealsListActivity callerFb;
    public static boolean isAdmin;

    private FireBaseInit(){}

    public static void FirebaseRef(String ref, final DealsListActivity callerActivity){

        if(mFireBaseInit == null){
            mFireBaseInit = new FireBaseInit();
            mFireDb = FirebaseDatabase.getInstance();
            mFbAuth = FirebaseAuth.getInstance();

            callerFb = callerActivity;
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(firebaseAuth.getCurrentUser() == null){
                        if (FireBaseInit.signIn()){
                        Toast.makeText(callerFb.getBaseContext(), "Signed in.", Toast.LENGTH_LONG)
                            .show();
                        }
                    }else{
                        String userId = firebaseAuth.getUid();
                        checkLevel(userId);
                    }
                }
            };
        }

        mTDs = new ArrayList<TravelDeal>();
        mDbRef = mFireDb.getReference().child(ref);

        storConnect();

    }

    private static void checkLevel(String userId) {
        FireBaseInit.isAdmin = false;
        DatabaseReference mRef = mFireDb.getReference().child("admins").child(userId);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FireBaseInit.isAdmin = true;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mRef.addChildEventListener(childEventListener);
    }

    private static boolean signIn() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


        // Create and launch sign-in intent
        callerFb.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(true)
                        .build(),
                RC_SIGN_IN);
        return true;
    }

    public static void storConnect(){
        mFbStor = FirebaseStorage.getInstance();
        mStorRef = mFbStor.getReference().child("deal_pictures");
    }

    public static void authConnect(){
        mFbAuth.addAuthStateListener(mAuthListener);
    }
    public static void authDisConnect(){
        mFbAuth.removeAuthStateListener(mAuthListener);
    }
}
