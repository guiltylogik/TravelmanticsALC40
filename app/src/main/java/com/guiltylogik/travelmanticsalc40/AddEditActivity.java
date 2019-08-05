package com.guiltylogik.travelmanticsalc40;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.widget.Toast.LENGTH_LONG;

public class AddEditActivity extends AppCompatActivity {


    public static final int PICTURE_RC = 2019;
    private FirebaseDatabase mFireDb;
    private DatabaseReference mDataRef;
    TravelDeal deal;
    EditText dealTitle;
    EditText dealPrice;
    EditText dealDesc;
    ImageView dealImage;


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
        dealImage = findViewById(R.id.deal_img);


        Button save_btn = findViewById(R.id.save_btn);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDeal();
                Toast.makeText(AddEditActivity.this, getString(R.string.save_notice), Toast.LENGTH_SHORT).show();
                backToList();
            }
        });

        Button upload_btn = findViewById(R.id.upload_img);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Upload Picture"), PICTURE_RC);
            }
        });


        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");


        if(intent.hasExtra("Deal")){
            setTitle(R.string.edit_deal_txt);
            upload_btn.setText(R.string.update_img);
            upload_btn.setBackgroundColor(getResources().getColor(R.color.colorUpload));
        }

        if(deal == null){
            deal = new TravelDeal();
        }


        this.deal = deal;
        dealTitle.setText(deal.getTitle());
        dealPrice.setText(deal.getPrice());
        dealDesc.setText(deal.getDescription());
        showImg(deal.getImageUrl());


    }

    private void showImg(String url){
        if(url != null && url.isEmpty() == false){
            Picasso.get()
                    .load(url)
                    .into(dealImage);
        }
    }

    private void resetInput() {

        dealTitle.setText("");
        dealPrice.setText("");
        dealDesc.setText("");
        dealTitle.requestFocus();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICTURE_RC && resultCode == RESULT_OK){
            final Uri imgUri = data.getData();
            final StorageReference  sRef = FireBaseInit.mStorRef.child(imgUri.getLastPathSegment());
            sRef.putFile(imgUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        String imageName = taskSnapshot.getStorage().getName();
                        deal.setDealImageName(imageName);
                        Log.d("ImageName", imageName);
                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("image", "onSuccess: uri= "+ uri.toString());
                                deal.setImageUrl(uri.toString());
                                showImg(uri.toString());
                            }
                        });
                }
            });
        }
    }

    private void backToList() {
        Intent intent = new Intent(this, DealsListActivity.class);
        startActivity(intent);
    }

    private void saveDeal() {

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
            if(deal.getDealImageName() != null && !deal.getDealImageName().isEmpty()){
                StorageReference imgRef = FireBaseInit.mStorRef.child(deal.getDealImageName());
                imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Deleted", "Deleted Successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Not Deleted", e.getMessage());
                    }
                });
            mDataRef.child(deal.getId()).removeValue();
            }
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
        if(deal.getId() == null){
            menu.findItem(R.id.delete_opt).setVisible(false);
        }

        return true;
    }
}
