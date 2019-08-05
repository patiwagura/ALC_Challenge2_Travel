package com.pato.travelmantics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealsActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    private EditText txtTitle, txtPrice, txtDesc;
    TravelDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        //used before FirebaseUtil was created.
        //create  an instance of FirebaseDatabase.
        //mFirebaseDb = FirebaseDatabase.getInstance();

        //a firebaseDbReference creates a path where data is stored.
        //mDbReference = mFirebaseDb.getReference().child("traveldeals");

        //Using FirebaseUtil class get a reference to FirebaseDatabase and DatabaseReference
        FirebaseUtil.openFbReference("traveldeals");
        mFirebaseDb = FirebaseUtil.firebaseDb;
        mDbReference = FirebaseUtil.dbReference;

        //get reference to ui-widgets declared in layout_xml
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtPrice = (EditText) findViewById(R.id.txtPrice);
        txtDesc = (EditText) findViewById(R.id.txtDescription);

        //get intent passed when starting Activity.
        Intent sIntent = getIntent();
        TravelDeal myDeal = (TravelDeal) sIntent.getSerializableExtra("Deal");
        if (myDeal == null) {
            myDeal = new TravelDeal();
        }
        this.deal = myDeal;

        //set text of the ui to match selected deal.
        txtTitle.setText(deal.getTitle());
        txtDesc.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this, "Deal Saved", Toast.LENGTH_LONG).show();
                cleanTxt(); //Reset the EditText.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * method to save a Deal.
     */
    private void saveDeal() {
        //String title = txtTitle.getText().toString();
        //String desc = txtDesc.getText().toString();
        //String price = txtPrice.getText().toString();

        //instance of TravelDeal to hold data.
        //TravelDeal deal = new TravelDeal(title, desc, price, "");

        deal.setTitle(txtTitle.getText().toString());
        deal.setDescription(txtDesc.getText().toString());
        deal.setPrice(txtPrice.getText().toString());

        //check if deal exists or its a new one.
        if (deal.getId() == null) {
            //save the new deal.
            mDbReference.push().setValue(deal);
        } else {
            //deal exists. retrieve deal specified by id.
            mDbReference.child(deal.getId()).setValue(deal);
        }


        Log.d("SAVE_DEAL", " TITLE " + txtTitle.getText().toString() + ":  Price " + txtPrice.getText().toString() + " DESC : " + txtDesc.getText().toString());
    }

    //method to delete existing deal.
    private void deleteDeal(){
        //check if deal exists if not prompt user to save a deal first.
        if(deal == null){
            Toast.makeText(this,"Please save a deal before deleting", Toast.LENGTH_LONG).show();
            return;
        }
        //get current deal and remove it.
        mDbReference.child(deal.getId()).removeValue();

    }

    //goback to listActivity after saving deal.
    private void backToList(){
        Intent mIntent = new Intent(this,ListActivity.class);
        startActivity(mIntent);
    }

    //method to clear the EditText fields
    private void cleanTxt() {
        txtTitle.setText("");
        txtPrice.setText("");
        txtDesc.setText("");
        //set focus to Title_textField.
        txtTitle.requestFocus();

    }
}
