package com.navigine.naviginedemo;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayReview extends AppCompatActivity {

    //RecyclerView recyclerView;
    //private Adapter adapter;
    //private List list;
    FirebaseDatabase database;
    //HelperAdapter helperAdapter;
    DatabaseReference ref;
    //List<ReviewClass> reviewClass;
    //List<FetchData> fetchData;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    ReviewClass reviewClass;
    static final String TAG = "NAVIGINE.Demo";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        reviewClass = new ReviewClass();
        listView = (ListView) findViewById(R.id.listViewFb);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Reviews");
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    reviewClass = ds.getValue(ReviewClass.class);
                    //list.add(reviewClass.getRating() + "Stars" + "  " +reviewClass.getFeedback().toString());
                    list.add(reviewClass.getRating() + " Stars" + "    " +reviewClass.getFeedback());
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        /*recyclerView = findViewById(R.id.recyclerFdbs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewClass = new ArrayList<>();

        databaseReference = database.getInstance().getReference("Reviews");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    ReviewClass data = ds.getValue(ReviewClass.class);
                    reviewClass.add(data);
                }
                helperAdapter = new HelperAdapter(reviewClass);
                recyclerView.setAdapter(helperAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }
}
