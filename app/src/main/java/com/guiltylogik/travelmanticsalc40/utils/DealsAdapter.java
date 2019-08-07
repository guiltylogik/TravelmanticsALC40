package com.guiltylogik.travelmanticsalc40.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.guiltylogik.travelmanticsalc40.ui.AddEditActivity;
import com.guiltylogik.travelmanticsalc40.R;
import com.guiltylogik.travelmanticsalc40.ui.UserView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealsViewHolder> {
    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFireDb;
    private DatabaseReference mDataRef;

    public DealsAdapter(){
        mFireDb = FireBaseInit.mFireDb;
        mDataRef = FireBaseInit.mDbRef;

        this.deals = FireBaseInit.mTDs;

        ChildEventListener mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal tDs = dataSnapshot.getValue(TravelDeal.class);
                tDs.setId(dataSnapshot.getKey());
                deals.add(tDs);
                notifyItemInserted(deals.size()-1);
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
        mDataRef.addChildEventListener(mChildListener);
    }


    @Override
    public DealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.rv_deals_list, parent, false);
        return new DealsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealsViewHolder holder, int position) {

        TravelDeal deal = deals.get(position);
        holder.bind(deal);

    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public class DealsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView dealTitle;
        TextView dealPrice;
        TextView dealDesc;
        ImageView dealImg;

        public DealsViewHolder(@NonNull View itemView) {
            super(itemView);

            dealTitle = itemView.findViewById(R.id.tw_deal_title);
            dealPrice = itemView.findViewById(R.id.tw_deal_price);
            dealDesc = itemView.findViewById(R.id.tw_deal_desc);
            dealImg = itemView.findViewById(R.id.iv_deal_image);
            itemView.setOnClickListener(this);
        }

        public  void bind(TravelDeal deal){
            dealTitle.setText(deal.getTitle());
            dealPrice.setText(String.format("GHc %s", deal.getPrice()));
            dealDesc.setText(deal.getDescription());
            showImg(deal.getImageUrl());
        }

        private void showImg(String url){
            if(url != null && !url.isEmpty()){
                Picasso.get()
                        .load(url)
                        .into(dealImg);
            }
        }
        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            TravelDeal selectedDeal = deals.get(pos);
            Intent intent = new Intent(view.getContext(), (FireBaseInit.isAdmin)
                    ? AddEditActivity.class: UserView.class);
            intent.putExtra("Deal", selectedDeal);
            view.getContext().startActivity(intent);
        }
    }
}
