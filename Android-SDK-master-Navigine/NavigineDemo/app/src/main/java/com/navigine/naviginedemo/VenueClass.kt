package com.navigine.naviginedemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.Arrays
import java.util.Collections
import java.util.List
import java.util.Locale
import ContextMenu.ContextMenuInfo
import AdapterView.AdapterContextMenuInfo

class VenueClass : Activity() {
    var Empty: TextView? = null
    var text1: String? = null
    var list: String? = null
    var Venue_list: List<String> = ArrayList()
    var file_list: List<String> = ArrayList()
    var simpleList: ListView? = null

    @Override
    protected fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        text1 = getIntent().getStringExtra("List")
        file_list = Arrays.asList(getApplicationContext().fileList())
        for (i in 0 until file_list.size()) {
            if (file_list[i].equals(text1)) {
                val fis: FileInputStream
                try {
                    fis = openFileInput(text1)
                    val input = ByteArray(fis.available())
                    while (fis.read(input) !== -1) {
                    }
                    list = String(input)
                    Venue_list = Arrays.asList(list.split(" , "))
                    if (Venue_list.isEmpty()) {
                        setContentView(R.layout.empty_list)
                        Empty = findViewById(R.id.AddList)
                        registerForContextMenu(Empty)
                    } else {
                        setContentView(R.layout.venue)
                        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.search_listview, R.id.textView, Venue_list)
                        simpleList = findViewById(R.id.listVenue)
                        simpleList.setAdapter(arrayAdapter)
                        registerForContextMenu(simpleList)
                    }
                    //Toast.makeText(search_history.this, text1, Toast.LENGTH_SHORT).show();
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                break
            } else {
                setContentView(R.layout.empty_list)
                Empty = findViewById(R.id.AddList)
                registerForContextMenu(Empty)
            }
        }
    }

    @Override
    fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.listvenuemenu, menu)
    }

    @Override
    fun onContextItemSelected(item: MenuItem): Boolean {
        val info: AdapterContextMenuInfo = item.getMenuInfo() as AdapterContextMenuInfo
        val file: String? = null
        return when (item.getItemId()) {
            R.id.delete -> {
                var ven: String? = null
                var text2: String? = null
                ven = Venue_list[info.position]
                var i = 0
                while (i < Venue_list.size()) {
                    text2 = if (text2 == null) {
                        if (Venue_list[i].equals(ven)) {
                            ++i
                            continue
                        } else {
                            Venue_list[i]
                        }
                    } else {
                        if (Venue_list[i].equals(ven)) {
                            ++i
                            continue
                        } else {
                            text2.toString() + " , " + Venue_list[i]
                        }
                    }
                    ++i
                }
                if (text2 == null) {
                    setContentView(R.layout.empty_list)
                    Empty = findViewById(R.id.AddList)
                    registerForContextMenu(Empty)
                    deleteFile(text1)
                } else {
                    val fos: FileOutputStream
                    try {
                        fos = openFileOutput(text1, Context.MODE_PRIVATE)
                        fos.write(text2.getBytes())
                        fos.close()
                    } catch (e: FileNotFoundException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    } catch (e: IOException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                    val fis: FileInputStream
                    try {
                        fis = openFileInput(text1)
                        val input = ByteArray(fis.available())
                        while (fis.read(input) !== -1) {
                        }
                        list = String(input)
                        Venue_list = Arrays.asList(list.split(" , "))
                        if (Venue_list.isEmpty()) {
                            setContentView(R.layout.empty_list)
                            Empty = findViewById(R.id.AddList)
                            registerForContextMenu(Empty)
                        } else {
                            setContentView(R.layout.venue)
                            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.search_listview, R.id.textView, Venue_list)
                            simpleList = findViewById(R.id.listVenue)
                            simpleList.setAdapter(arrayAdapter)
                            registerForContextMenu(simpleList)
                        }


                        //Toast.makeText(search_history.this, text1, Toast.LENGTH_SHORT).show();
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun add(v: View?) {
        val intent = Intent(this, formVenue::class.java)
        intent.putExtra("List", text1)
        startActivity(intent)
    }

    fun back(v: View?) {
        val intent = Intent(this, VenuePage::class.java)
        startActivity(intent)
    }
}