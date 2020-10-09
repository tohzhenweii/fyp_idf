package com.navigine.naviginedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.navigine.naviginesdk.Location;
import com.navigine.naviginesdk.LocationInfo;
import com.navigine.naviginesdk.NavigationThread;
import com.navigine.naviginesdk.NavigineSDK;
import com.navigine.naviginesdk.SubLocation;
import com.navigine.naviginesdk.Venue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FindLocation extends AppCompatActivity {
TextView mShareLocation,mEmail,mPhoneNumber,mUserName;
    //search variable
    NavigationThread mNavigation = null;
    Spinner LVenue1 = null;
    List<String> VenueList = new ArrayList<String>();
    Location mLocation = null;
    int mCurrentSubLocationIndex = 0;
    static final String TAG = "NAVIGINE.Demo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);
        //user variable
        mShareLocation=findViewById(R.id.tvSharingLocation);
        mEmail=findViewById(R.id.tvEmail);
        mPhoneNumber=findViewById(R.id.tvPhoneNumber);
        mUserName=findViewById(R.id.tvUserName);

    //    User Profile
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



















        //Search




        mNavigation     = NavigineSDK.getNavigation();
        VenueLists();

        setContentView(R.layout.search);
        LVenue1 = (Spinner) findViewById(R.id.spinner1);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, VenueList);
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        LVenue1.setAdapter(adapter1);

        //venue method











   /*     mLocation=findViewById(R.id.tvLocation);

        String Location= NavigineSDK.getLastKnownLocation().toString();
        String Location1=com.navigine.naviginesdk.Location.class.getName();
         String Location2=NavigineSDK.getNavigation().getLocationName();//closest
       String L3= com.navigine.naviginesdk.Venue.class.getName();

   //     mLocation.setText(L3); */

    }
    public void VenueLists() {
        mLocation = mNavigation.getLocation();
        SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);


        for ( int i = 0; i < subLoc.getVenues().size(); i++) {

            Venue ven = subLoc.getVenues().get(i);
            VenueList.add(ven.getName());

            Log.d(TAG, "venues: " + ven.getName());

        }

        Set<String> NewVenue = new HashSet<>(VenueList);
        VenueList.clear();
        VenueList.addAll(NewVenue);
        VenueList.add(0,"---Select---");


    }
}