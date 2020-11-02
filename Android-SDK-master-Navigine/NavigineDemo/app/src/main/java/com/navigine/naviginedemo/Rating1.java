package com.navigine.naviginedemo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseLongArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navigine.naviginedemo.VenueClass;
import com.navigine.naviginesdk.Location;
import com.navigine.naviginesdk.NavigationThread;
import com.navigine.naviginesdk.NavigineSDK;
import com.navigine.naviginesdk.SubLocation;
import com.navigine.naviginesdk.Venue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.firebase.database.DatabaseReference;


public class Rating1 extends AppCompatActivity {
    EditText FeedMessage;
    Button SendFeed;
    String rating1=""; //String rating1="",rating2="", rating3="";
    RatingBar ratingbar1;  //RatingBar ratingbar1,ratingbar2,ratingbar3;
    DatabaseReference ref;
    Spinner spinner;
    Feedback feedback; // get,set class
    //DatabaseReference reff1;  //?
    //ListView listView;
    FirebaseDatabase database;
    ArrayAdapter<String> adapter;
    ArrayList<String> list;
    //ArrayList<String> ArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating1); // link java to xml

        //display list of venues from db on listview
        /*listView = (ListView) findViewById(R.id.ListViewLoc) ;
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Venues");
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2, list);
        listView.setAdapter(adapter);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren())
                {
                    list.add(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        //list = new ArrayList<>();
        //arrayAdapter = new ArrayAdapter<>(this,R.layout.)


        // display list of location from DB in spinner
        //reff1 = FirebaseDatabase.getInstance().getReference("Venues");
        //arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, ArrayList);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(arrayAdapter);

        //spinner = findViewById(R.id.spinnerLoc);
        //listView = (ListView) findViewById(R.id.ListViewLoc) ;

        //insert data to fb
        /*FeedMessage = (EditText) findViewById(R.id.FeedMessage);
        ratingbar1 = (RatingBar) findViewById(R.id.ratingBar1);
        SendFeed = (Button) findViewById(R.id.sendFeed);
        feedback = new Feedback();
        ref = FirebaseDatabase.getInstance().getReference().child("Feedback");
        SendFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating1 = String.valueOf(ratingbar1.getRating());
                //String loc = spinner.getSelectedItem().toString();
                feedback.setRreviews(FeedMessage.getText().toString().trim());
                feedback.setRrating(rating1);
                //feedback.setRlocation(loc);
                ref.push().setValue(feedback);
                Toast.makeText(Rating1.this,"Review submitted!",Toast.LENGTH_LONG).show();

            }
        });*/
    }


}