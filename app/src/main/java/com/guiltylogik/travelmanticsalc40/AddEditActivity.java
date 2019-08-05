package com.guiltylogik.travelmanticsalc40;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.widget.Toast.LENGTH_LONG;

public class AddEditActivity extends AppCompatActivity {


    private FirebaseDatabase mFireDb;
    private DatabaseReference mDataRef;
    TravelDeal deal;
    EditText dealTitle;
    EditText dealPrice;
    EditText dealDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        setTitle(R.string.add_deal_txt);

        FireBaseInit.FirebaseRef("deals");
        mFireDb = FireBaseInit.mFireDb;
        mDataRef = FireBaseInit.mDbRef;

        dealTitle = findViewById(R.id.ev_dealTitle);
        dealPrice = findViewById(R.id.ev_dealPrice);
        dealDesc = findViewById(R.id.ev_dealDesc);


        Button save_btn = findViewById(R.id.save_btn);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDeal();
                Toast.makeText(AddEditActivity.this, getString(R.string.save_notice), Toast.LENGTH_SHORT).show();
                backToList();
            }
        });


        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");


        if(intent.hasExtra("Deal")){
            setTitle(R.string.edit_deal_txt);
        }

        if(deal == null){
            deal = new TravelDeal();
        }

        this.deal = deal;
        dealTitle.setText(deal.getTitle());
        dealPrice.setText(deal.getPrice());
        dealDesc.setText(deal.getDescription());


    }

    private void resetInput() {

        dealTitle.setText("");
        dealPrice.setText("");
        dealDesc.setText("");
        dealTitle.requestFocus();

    }

    private void backToList() {
        Intent intent = new Intent(this, DealsListActivity.class);
        startActivity(intent);
    }

    private void saveDeal() {

        deal.setImageUrl("");
        deal.setTitle(dealTitle.getText().toString());
        deal.setPrice(dealPrice.getText().toString());
        deal.setDescription(dealDesc.getText().toString());

        if(deal.getId() == null && deal != null){
            mDataRef.push().setValue(deal);
            resetInput();
        }else{
            mDataRef.child(deal.getId()).setValue(deal);
        }

    }

    private void destroyDeal(){
        if(deal.getId() != null){
            mDataRef.child(deal.getId()).removeValue();
            return;
        }
        Toast.makeText(this, "Deal does not exist.", LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_opt:
                destroyDeal();
                Toast.makeText(this, "Deal deleted successfully", LENGTH_LONG).show();
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opt_menu, menu);

        return true;
    }
}
