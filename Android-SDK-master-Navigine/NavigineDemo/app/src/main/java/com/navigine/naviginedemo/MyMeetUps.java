package com.navigine.naviginedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyMeetUps extends AppCompatActivity {
    TextView mTest;
    EditText mSearchUser;
    Button mBtnGo,mHeadToMeetUp;
    FirebaseDatabase dB;
    DatabaseReference reference;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meet_ups);
    }

}