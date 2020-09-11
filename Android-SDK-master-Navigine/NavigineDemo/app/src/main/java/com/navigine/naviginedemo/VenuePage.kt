package com.navigine.naviginedemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.navigine.naviginedemo.VenueClass
import com.navigine.naviginesdk.Location
import com.navigine.naviginesdk.NavigationThread
import com.navigine.naviginesdk.NavigineSDK
import com.navigine.naviginesdk.SubLocation
import com.navigine.naviginesdk.Venue
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.ArrayList
import java.util.HashSet
import java.util.List
import java.util.Set
import ContextMenu.ContextMenuInfo
import AdapterView.AdapterContextMenuInfo

class VenuePage : Activity() {
    private var mLocation: Location? = null
    private var mNavigation: NavigationThread? = null
    private val mCurrentSubLocationIndex = 0

    //String[] VenueList  = new String[] { "L306","L307","L308","L309","L310","L311","L312","L313","L314","L335","L339","L340","FYPJ Enterance","Blk L Floor 3 Enterance"};
    var VenueList: List<String> = ArrayList<String>()
    var VenueList1: ArrayList<VenueClass> = ArrayList<VenueClass>()
    var simpleList: ListView? = null

    @Override
    protected fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavigation = NavigineSDK.getNavigation()
        Categories()
        //VenueItem();
        setContentView(R.layout.customized_list)
        simpleList = findViewById(R.id.listVenue)
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.search_listview, R.id.textView, VenueList)
        simpleList.setAdapter(arrayAdapter)
        simpleList.setOnItemClickListener(object : OnItemClickListener() {
            fun onItemClick(parent: AdapterView<*>, view: View?, position: Int,
                            id: Long) {
                // TODO Auto-generated method stub
                val cat = parent.getItemAtPosition(position) as String
                //Toast.makeText(VenuePage.this, clickedFile, Toast.LENGTH_SHORT).show();
                val intent = Intent(getApplicationContext(), MainActivity::class.java)
                intent.putExtra("cat", cat)
                startActivity(intent)
            }
        })
    }

    fun Form_Fav(v: View?) {
        val intent = Intent(this, VenueClass::class.java)
        intent.putExtra("List", "Favourite")
        startActivity(intent)
    }

    fun Form_Star(v: View?) {
        val intent = Intent(this, VenueClass::class.java)
        intent.putExtra("List", "Starred")
        startActivity(intent)
    }

    fun Form_Wanttogo(v: View?) {
        val intent = Intent(this, VenueClass::class.java)
        intent.putExtra("List", "Want_To_Go")
        startActivity(intent)
    }

    fun Categories() {
        mLocation = mNavigation.getLocation()
        val subLoc: SubLocation = mLocation.getSubLocations().get(mCurrentSubLocationIndex)
        for (i in 0 until subLoc.getVenues().size()) {
            val ven: Venue = subLoc.getVenues().get(i)
            if (ven.getCategory().getName().equals("Office")) {
                continue
            } else {
                VenueList.add(ven.getCategory().getName())
            }
        }
        val NewVenue: Set<String> = HashSet(VenueList)
        VenueList.clear()
        VenueList.addAll(NewVenue)
    }

    fun back(v: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    } /*
    public void VenueItem(){
        String VenueLists = null;
        Boolean fav = false;



        VenueList1.add(
                new VenueClass("L306", false)
        );


        VenueList1.add(
                new VenueClass("L307", false)
        );


        VenueList1.add(
                new VenueClass("L308", false)
        );


        VenueList1.add(
                new VenueClass("L309", false)
        );


        VenueList1.add(
                new VenueClass("L310", false)
        );


        VenueList1.add(
                new VenueClass("L311", false)
        );


        VenueList1.add(
                new VenueClass("L312", false)
        );


        VenueList1.add(
                new VenueClass("L313", false)
        );


        VenueList1.add(
                new VenueClass("L314", false)
        );


        VenueList1.add(
                new VenueClass("L335", false)
        );


        VenueList1.add(
                new VenueClass("L339", false)
        );


        VenueList1.add(
                new VenueClass("L340", false)
        );

        Log.d(TAG, "venues List: " + VenueList1);
    }


    public void onDefaultToggleClick(View View) {
        Toast.makeText(this, "DefaultToggle", Toast.LENGTH_SHORT).show();
    }

    public void onCustomToggleClick(View view){
        Toast.makeText(this, "CustomToggle", Toast.LENGTH_SHORT).show();
    }

*/

    companion object {
        private const val TAG = "NAVIGINE.Demo"
    }
}