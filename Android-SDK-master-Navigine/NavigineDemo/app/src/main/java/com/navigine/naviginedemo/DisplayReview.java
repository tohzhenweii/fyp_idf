package com.navigine.naviginedemo;

import android.os.Bundle;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayReview extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter adapter;
    private List list;
    FirebaseDatabase database;
    HelperAdapter helperAdapter;
    DatabaseReference databaseReference;
    List<ReviewClass> reviewClass;
    //List<FetchData> fetch data;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        recyclerView = findViewById(R.id.recyclerFdbs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewClass = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Reviews");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    ReviewClass data = ds.getValue(ReviewClass.class);
                    reviewClass.add(data);
                }
                helperAdapter = new HelperAdapter(reviewClass);
                recyclerView.setAdapter(helperAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
