package com.navigine.naviginedemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import com.navigine.naviginesdk.Location
import com.navigine.naviginesdk.NavigationThread
import com.navigine.naviginesdk.NavigineSDK
import com.navigine.naviginesdk.SubLocation
import com.navigine.naviginesdk.Venue
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.Arrays
import java.util.HashSet
import java.util.List
import java.util.Set
import ContextMenu.ContextMenuInfo
import AdapterView.AdapterContextMenuInfo

class SearchPage : Activity() {
    var mydb: MyDbAdapter? = null
    private var mLocation: Location? = null
    private var mNavigation: NavigationThread? = null
    private val mCurrentSubLocationIndex = 0
    private var LVenue1: Spinner? = null
    private var LVenue2: Spinner? = null
    var text1: String? = null
    var text2: String? = null
    var filename = "History"
    var name: String? = null
    var destination: String? = null
    var name2: String? = null
    var destination2: String? = null
    var VenueList: List<String> = ArrayList<String>()
    var Shortcut_list: List<String> = ArrayList()
    var Trying: Array<String>
    var Shortcut_Venue: List<String> = ArrayList<String>()
    var testing: List<String> = ArrayList<String>()
    var simpleList: ListView? = null
    protected fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavigation = NavigineSDK.getNavigation()
        VenueLists()
        setContentView(R.layout.search)
        LVenue1 = findViewById(R.id.spinner1) as Spinner?

// Create an ArrayAdapter using the string array and a default spinner layout
        val adapter1: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, VenueList)
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        LVenue1.setAdapter(adapter1)
        LVenue2 = findViewById(R.id.spinner2) as Spinner?
        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter2: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, VenueList)
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        LVenue2.setAdapter(adapter2)


        //mydb = new MyDbAdapter(this);
        ShortcutLists()
        registerForContextMenu(simpleList)


        /*
        if (name !=null && destination != null){
            Shortcut_list.add(name + " : " + destination);
            name = null;
            destination = null;
        }*/LVenue1.setOnItemSelectedListener(object : OnItemSelectedListener() {
            @Override
            fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                text1 = VenueList[position]
            }

            @Override
            fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        })
        LVenue2.setOnItemSelectedListener(object : OnItemSelectedListener() {
            @Override
            fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                text2 = VenueList[position]
            }

            @Override
            fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        })
        simpleList.setOnItemClickListener(object : OnItemClickListener() {
            fun onItemClick(parent: AdapterView<*>, view: View?, position: Int,
                            id: Long) {
                // TODO Auto-generated method stub
                val clickedFile = parent.getItemAtPosition(position) as String
                //Toast.makeText(SearchPage.this, clickedFile, Toast.LENGTH_SHORT).show();
                val fis: FileInputStream
                try {
                    fis = openFileInput(clickedFile)
                    val input = ByteArray(fis.available())
                    while (fis.read(input) !== -1) {
                    }
                    text1 = String(input)
                    Toast.makeText(this@SearchPage, text1, Toast.LENGTH_SHORT).show()
                    text2 = "---Select---"
                    val intent = Intent(getApplicationContext(), MainActivity::class.java)
                    intent.putExtra("Position1", text1)
                    intent.putExtra("Position2", text2)
                    startActivity(intent)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
    }

    @Override
    fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.searchmenu, menu)
    }

    @Override
    fun onContextItemSelected(item: MenuItem): Boolean {
        val info: AdapterContextMenuInfo = item.getMenuInfo() as AdapterContextMenuInfo
        var file: String? = null
        return when (item.getItemId()) {
            R.id.delete -> {
                file = Shortcut_Venue[info.position]
                deleteFile(file)
                Shortcut_Venue.remove(file)
                val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.search_listview, R.id.textView, Shortcut_Venue)
                simpleList = findViewById(R.id.listVenue)
                simpleList.setAdapter(arrayAdapter)
                arrayAdapter.notifyDataSetChanged()
                true
            }
            R.id.edit -> {
                file = Shortcut_Venue[info.position]
                val fis: FileInputStream
                try {
                    fis = openFileInput(file)
                    val input = ByteArray(fis.available())
                    while (fis.read(input) !== -1) {
                    }
                    text1 = String(input)
                    //Toast.makeText(SearchPage.this, text1, Toast.LENGTH_SHORT).show();

                    //text2 = "---Select---";
                    val intent = Intent(getApplicationContext(), shortcut_edit::class.java)
                    intent.putExtra("Position1", text1)
                    intent.putExtra("name", file)
                    startActivity(intent)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                super.onContextItemSelected(item)
            }
            else -> super.onContextItemSelected(item)
        }
    }

    /*
    public void Listview(){


        Log.d(TAG, "Lists: " + Shortcut_list);
    }*/
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

    fun ShortcutLists() {
        Trying = getApplicationContext().fileList()
        Shortcut_list = Arrays.asList(getApplicationContext().fileList())
        for (i in 0 until Shortcut_list.size()) {
            Log.d(TAG, "Files: " + Shortcut_list[i])
            if (Shortcut_list[i].equals("Navigine") || Shortcut_list[i].equals("libnavigate-jni.so") || Shortcut_list[i].equals("History") || Shortcut_list[i].equals("Favourite") || Shortcut_list[i].equals("Starred") || Shortcut_list[i].equals("Want_To_Go")) {
                continue
            } else {
                Shortcut_Venue.add(Shortcut_list[i])
            }
        }
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.search_listview, R.id.textView, Shortcut_Venue)
        simpleList = findViewById(R.id.listVenue)
        simpleList.setAdapter(arrayAdapter)
        arrayAdapter.notifyDataSetChanged()
    }

    fun simpleListView() {
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.search_listview, R.id.textView, Shortcut_Venue)
        simpleList = findViewById(R.id.listVenue)
        simpleList.setAdapter(arrayAdapter)
        arrayAdapter.notifyDataSetChanged()
    }

    fun Search(v: View?) {
        if (text1 === "---Select---" && text2 !== "---Select---") {
            Toast.makeText(this@SearchPage, "Please choose your first destination", Toast.LENGTH_SHORT).show()
        } else if (text1 === "---Select---" && text2 === "---Select---") {
            Toast.makeText(this@SearchPage, "Please choose your destinations", Toast.LENGTH_SHORT).show()
        } else if (text1 !== "---Select---" && text2 === "---Select---") {
            //Internal Storage
            val fos: FileOutputStream
            try {
                val input = text1.toString() + " , "
                fos = openFileOutput(filename, Context.MODE_APPEND)
                fos.write(input.getBytes())
                fos.close()
            } catch (e: FileNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Position1", text1)
            intent.putExtra("Position2", text2)
            startActivity(intent)
        } else {
            //Internal Storage
            val fos: FileOutputStream
            try {
                val input = text1.toString() + " , " + text2 + " , "
                fos = openFileOutput(filename, Context.MODE_APPEND)
                fos.write(input.getBytes())
                fos.close()
            } catch (e: FileNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Position1", text1)
            intent.putExtra("Position2", text2)
            startActivity(intent)
        }
    }

    fun Back(v: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun History(v: View?) {
        val intent = Intent(this, search_history::class.java)
        startActivity(intent)
    }

    fun Shortcut(v: View?) {
        val intent = Intent(this, shortcut::class.java)
        startActivity(intent)
    }

    companion object {
        //ArrayList array_list = mydb.getAllCotacts();
        private const val TAG = "NAVIGINE.Demo"
    }
}