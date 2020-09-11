package com.navigine.naviginedemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.navigine.naviginesdk.Location
import com.navigine.naviginesdk.NavigationThread
import com.navigine.naviginesdk.NavigineSDK
import com.navigine.naviginesdk.SubLocation
import com.navigine.naviginesdk.Venue
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.HashSet
import java.util.List
import java.util.Set
import ContextMenu.ContextMenuInfo
import AdapterView.AdapterContextMenuInfo

class shortcut : Activity() {
    private var mLocation: Location? = null
    private var mNavigation: NavigationThread? = null
    private val mCurrentSubLocationIndex = 0
    private var LVenue1: Spinner? = null
    private var shortcut1: EditText? = null
    var name: String? = null
    var filename = "Shortcut"
    var destination: String? = null

    // public String text1 = null;
    private var mydb: MyDbAdapter? = null
    var VenueList: List<String> = ArrayList<String>()
    protected fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavigation = NavigineSDK.getNavigation()
        VenueLists()
        setContentView(R.layout.shortcut)
        shortcut1 = findViewById(R.id.shortcut1) as EditText?
        LVenue1 = findViewById(R.id.spinner1) as Spinner?
        mydb = MyDbAdapter(this)
        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter1: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, VenueList)
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        LVenue1.setAdapter(adapter1)
        LVenue1.setOnItemSelectedListener(object : OnItemSelectedListener() {
            @Override
            fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                destination = VenueList[position]
            }

            @Override
            fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        })
    }

    fun VenueLists() {
        mLocation = mNavigation.getLocation()
        val subLoc: SubLocation = mLocation.getSubLocations().get(mCurrentSubLocationIndex)
        for (i in 0 until subLoc.getVenues().size()) {
            val ven: Venue = subLoc.getVenues().get(i)
            VenueList.add(ven.getName())
            Log.d(TAG, "venues: " + ven.getName())
        }
        val NewVenue: Set<String> = HashSet(VenueList)
        VenueList.clear()
        VenueList.addAll(NewVenue)
        VenueList.add(0, "---Select---")
    }

    /*
    public void Search(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Position1", text1);
        intent.putExtra("Position2", text2);
        startActivity(intent);

    }*/
    fun Back(v: View?) {
        val intent = Intent(this, SearchPage::class.java)
        startActivity(intent)
    }

    fun Add(v: View?) {
        name = shortcut1.getText().toString()
        if (name === "" || destination === "---Select---") {
            Toast.makeText(this@shortcut, "Please enter the right informantion", Toast.LENGTH_SHORT).show()
        } else {


            //Internal Storage
            val fos: FileOutputStream
            try {
                fos = openFileOutput(name, Context.MODE_PRIVATE)
                fos.write(destination.getBytes())
                fos.close()
                Toast.makeText(
                        this@shortcut, name.toString() + " saved",
                        Toast.LENGTH_LONG).show()
            } catch (e: FileNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            //mydb.insertShortcut(name,destination);
            val intent = Intent(this, SearchPage::class.java)
            startActivity(intent)
        }

        /*
        intent.putExtra("name", name);
        intent.putExtra("Position", destination);

         */
    }

    companion object {
        private const val TAG = "NAVIGINE.Demo"
    }
}