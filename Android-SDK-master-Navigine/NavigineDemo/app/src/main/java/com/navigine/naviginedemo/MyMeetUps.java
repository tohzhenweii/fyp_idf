package com.navigine.naviginedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.HttpCookie;
import java.util.ArrayList;

public class MyMeetUps extends AppCompatActivity {

    private static final String TAG ="" ;
    private FirebaseDatabase dB;
    private DatabaseReference reference;
    private Button btnAddFriend;
    private ListView lvFriends;
    private TextView tvResult;
    private EditText txtFindFriend;
    String userName;
private Boolean UserExist=false;
private Boolean areFriends=false;
    long maxid;
    //search variables
    private ArrayList<String> stringArrayList=new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private int amountOfFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meet_ups);



        txtFindFriend=findViewById(R.id.txtAddFriend);
        btnAddFriend=findViewById(R.id.btnAddFriend);
        lvFriends=findViewById(R.id.lvFriends);
        tvResult=findViewById(R.id.tvNotification);
//Load Friendlist
        LoadFriendList();

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName=txtFindFriend.getText().toString().trim();
                Log.d(TAG, "Input User Name "+userName);
                CheckifUserExist(userName);
                CheckIfIsFriend(userName);
                GetFriendCountFromDB();
           /*     if(UserExist==true&&areFriends==false)
                {
                    AddFriend(userName);
                }*/

            }
        });




    }
    private String GetUserName()
    {
        SharedPreferences sp=getApplicationContext().getSharedPreferences("MyUserProfile", Context.MODE_PRIVATE);
        final String username=sp.getString("username","");
        return username;
    }
    private void AddFriend(final String inputUserName)
    {
        final int NoOfFriend=GetFriendCount();
        dB = FirebaseDatabase.getInstance();
String acUserName=GetUserName();

        reference = dB.getReference("users").child(acUserName).child("showLocation").child(inputUserName);
        Log.d(TAG, "AddFriend: "+reference);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Toast.makeText(getApplicationContext(),"Already Friends ",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    reference.setValue(inputUserName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       // reference=dB.getReference("users").child(myName).child("showLocation");



    }

private void CheckifUserExist(final String userName)
{

    dB= FirebaseDatabase.getInstance();
    reference=dB.getReference("users").child(userName);
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists())
            {

         UserExist=true;
            }
            else
            {
                Toast.makeText(getApplicationContext(),"User Does Not exist ",Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}
private void CheckIfIsFriend(final String inputUserName)
{String acUserName=GetUserName();

    dB=FirebaseDatabase.getInstance();
    reference=dB.getReference("users").child(acUserName).child("showLocation").child(inputUserName);
    reference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists())
            {
                areFriends=true;
                Toast.makeText(getApplicationContext(),"Your Already Friends with "+inputUserName,Toast.LENGTH_SHORT).show();
            }
            else
            {
                AddFriend(inputUserName);
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}

private long CountFriends() {

    {
        String myName = GetUserName();
        dB = FirebaseDatabase.getInstance();
        reference = dB.getReference("users").child(myName).child("showLocation");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxid = snapshot.getChildrenCount();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return maxid;
    }


}
private int GetFriendCount()
{  SharedPreferences sp=getApplicationContext().getSharedPreferences("MyUserProfile", Context.MODE_PRIVATE);
    int noOfFriend=sp.getInt("amountOfFriends",0);
    return noOfFriend;
}
private void GetFriendCountFromDB()
{
    dB = FirebaseDatabase.getInstance();
    reference = dB.getReference("users").child(userName).child("showLocation");
    reference.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            maxid=snapshot.getChildrenCount();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });


}
private void LoadFriendList()
{   String acUserName=GetUserName();
    dB=FirebaseDatabase.getInstance();
    reference=dB.getReference("users").child(acUserName).child("showLocation");
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot Dsp :snapshot.getChildren())
            {

                stringArrayList.add(Dsp.getValue().toString());
            }
            adapter.notifyDataSetChanged();
        }


        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,stringArrayList);
    lvFriends.setAdapter(adapter);



}






}