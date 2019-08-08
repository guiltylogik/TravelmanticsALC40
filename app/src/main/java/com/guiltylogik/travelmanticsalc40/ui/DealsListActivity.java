package com.guiltylogik.travelmanticsalc40.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.guiltylogik.travelmanticsalc40.R;
import com.guiltylogik.travelmanticsalc40.utils.DealsAdapter;
import com.guiltylogik.travelmanticsalc40.utils.FireBaseInit;
import com.guiltylogik.travelmanticsalc40.utils.TravelDeal;

import java.util.ArrayList;

public class DealsListActivity extends AppCompatActivity {

    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFireDb;
    private DatabaseReference mDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        FloatingActionButton fab = findViewById(R.id.fab);
        if(!FireBaseInit.isAdmin){
            fab.setVisibility(View.GONE);}
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DealsListActivity.this, AddEditActivity.class);
                intent.putExtra("addDeal", "new deal");
                startActivity(intent);
//                finish();
            }
        });

        FloatingActionButton logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(DealsListActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                FireBaseInit.authConnect();
                            }
                        });
                FireBaseInit.authDisConnect();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        FireBaseInit.authDisConnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FireBaseInit.FirebaseRef("deals", this);

        FireBaseInit.authConnect();

        RecyclerView rvDeals = findViewById(R.id.rv_deals);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvDeals.setLayoutManager(layoutManager);

        final DealsAdapter dealsAdapter = new DealsAdapter();
        rvDeals.setAdapter(dealsAdapter);
    }
}
