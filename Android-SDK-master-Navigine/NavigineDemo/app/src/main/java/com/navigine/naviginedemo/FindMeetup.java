package com.navigine.naviginedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindMeetup extends AppCompatActivity {
    TextView mShareLocation,mEmail,mPhoneNumber,mUserName,mTest;
    EditText mSearchUser;
    Button mBtnGo;
    FirebaseDatabase dB;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_meetup);
        mBtnGo=findViewById(R.id.btnGoFindMeetup);
        mSearchUser=findViewById(R.id.txtSearch);
mTest=findViewById(R.id.tvTest);
        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FindUser=mSearchUser.getText().toString().trim();
                dB= FirebaseDatabase.getInstance();
                reference=dB.getReference("users").child(FindUser).child("location");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        String Location;

                        if(snapshot.exists())
                        {              Location=snapshot.getValue().toString();
                            mTest.setText("MeetUp Location is At "+Location);


                        }
                        else
                        {
                            Toast.makeText(FindMeetup.this,"User does not exist",Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });






    }












}