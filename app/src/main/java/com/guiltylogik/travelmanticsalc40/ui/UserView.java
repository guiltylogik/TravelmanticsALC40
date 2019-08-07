package com.guiltylogik.travelmanticsalc40.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.guiltylogik.travelmanticsalc40.R;
import com.guiltylogik.travelmanticsalc40.utils.FireBaseInit;
import com.guiltylogik.travelmanticsalc40.utils.TravelDeal;
import com.squareup.picasso.Picasso;

public class UserView extends AppCompatActivity {

    TextView title, price, desc;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deal_view);
        setTitle(R.string.deal);

        img = findViewById(R.id.u_deal_image);
        title = findViewById(R.id.u_deal_title);
        price = findViewById(R.id.u_deal_price);
        desc = findViewById(R.id.u_deal_desc);

        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");

        title.setText(deal.getTitle());
        price.setText(String.format("GHc %s", deal.getPrice()));
        desc.setText(deal.getDescription());
        showImg(deal.getImageUrl());

    }

    private void showImg(String url){
        if(url != null && !url.isEmpty()){
            Picasso.get()
                    .load(url)
//                    .centerCrop()
                    .into(img);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                AuthUI.getInstance()
                        .signOut(UserView.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                FireBaseInit.authConnect();
                            }
                        });
                FireBaseInit.authDisConnect();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user, menu);

        return true;
    }
}
