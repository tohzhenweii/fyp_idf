package com.navigine.naviginedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.navigine.naviginesdk.LocationInfo;
import com.navigine.naviginesdk.NavigationThread;
import com.navigine.naviginesdk.NavigineSDK;
import com.navigine.naviginesdk.SubLocation;
import com.navigine.naviginesdk.Venue;

public class FindLocation extends AppCompatActivity {
TextView mShareLocation,mEmail,mPhoneNumber,mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);
        mShareLocation=findViewById(R.id.tvSharingLocation);
        mEmail=findViewById(R.id.tvEmail);
        mPhoneNumber=findViewById(R.id.tvPhoneNumber);
        mUserName=findViewById(R.id.tvUserName);

    //    String CurrentLocation,Email,PhoneNumber,UserName;
SharedPreferences sp=getApplicationContext().getSharedPreferences("MyUserProfile", Context.MODE_PRIVATE);
String name=sp.getString("username","");
        String phoneNumber=sp.getString("phoneNumber","");
        String location=sp.getString("location","");
        String ShowLocation=sp.getString("showLocation","");
        String email=sp.getString("email","");

        mUserName.setText(name);
        mPhoneNumber.setText(phoneNumber);
       mShareLocation.setText(ShowLocation);
        mEmail.setText(email);











   /*     mLocation=findViewById(R.id.tvLocation);

        String Location= NavigineSDK.getLastKnownLocation().toString();
        String Location1=com.navigine.naviginesdk.Location.class.getName();
         String Location2=NavigineSDK.getNavigation().getLocationName();//closest
       String L3= com.navigine.naviginesdk.Venue.class.getName();

   //     mLocation.setText(L3); */

    }
}