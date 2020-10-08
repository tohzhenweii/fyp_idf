package com.navigine.naviginedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.navigine.naviginesdk.LocationInfo;
import com.navigine.naviginesdk.NavigationThread;
import com.navigine.naviginesdk.NavigineSDK;
import com.navigine.naviginesdk.SubLocation;
import com.navigine.naviginesdk.Venue;

public class FindLocation extends AppCompatActivity {
TextView mLocation,mEmail,mPhoneNumber,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);
        String CurrentLocation,Email,PhoneNumber,UserName;


        mLocation=findViewById(R.id.tvLocation);

        String Location= NavigineSDK.getLastKnownLocation().toString();
        String Location1=com.navigine.naviginesdk.Location.class.getName();
         String Location2=NavigineSDK.getNavigation().getLocationName();//closest
       String L3= com.navigine.naviginesdk.Venue.class.getName();

        mLocation.setText(L3);

    }
}