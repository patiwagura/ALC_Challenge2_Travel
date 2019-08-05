package com.pato.travelmantics;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {

    private final FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    ArrayList<TravelDeal> deals;
    private ChildEventListener mChildListener;

    //DealAdapter constructor.
    public DealAdapter(){
        FirebaseUtil.openFbReference("traveldeals");
        mfirebaseDatabase = FirebaseUtil.firebaseDb;
        mDatabaseReference = FirebaseUtil.dbReference;

        //get array of deals from FirebaseUtil class.
        deals = FirebaseUtil.mDeals;

        //child listener.
        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //method called when activity is first time loaded, all data-items in db will trigger event here.

                TravelDeal tDeal = dataSnapshot.getValue(TravelDeal.class);
                Log.d("Deal: ", tDeal.getTitle());

                //set deal-id to id of data pushed from firebase.
                tDeal.setId(dataSnapshot.getKey());

                //add deals to the arrayList
                deals.add(tDeal);

                //notify about data changes.
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
        mDatabaseReference.addChildEventListener(mChildListener );
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //called when creating a new viewItem.
        Context mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.recycler_row, parent, false);

        return new DealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
    //get the current deal.
        TravelDeal deal = deals.get(position);
        holder.bind(deal);
    }

    @Override
    public int getItemCount() {
        //get number of items from the arrayList.
        return deals.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvDescription;
        TextView tvPrice;

        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            //ui declared in recycler_row_xml.
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvDescription = (TextView)itemView.findViewById(R.id.tvDescription);
            tvPrice = (TextView)itemView.findViewById(R.id.tvPrice);
        }

        public void bind(TravelDeal deal){
            tvTitle.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice());
        }


    }
}
