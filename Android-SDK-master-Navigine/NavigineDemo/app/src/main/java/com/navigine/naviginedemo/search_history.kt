package com.navigine.naviginedemo

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.ArrayList
import java.util.Arrays
import java.util.Collection
import java.util.Collections
import java.util.List
import ContextMenu.ContextMenuInfo
import AdapterView.AdapterContextMenuInfo

class search_history : Activity() {
    var filename = "History"
    var list: String? = null
    var History_list: List<String> = ArrayList()
    var simpleList: ListView? = null

    @Override
    protected fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_history)
        val fis: FileInputStream
        try {
            fis = openFileInput(filename)
            val input = ByteArray(fis.available())
            while (fis.read(input) !== -1) {
            }
            list = String(input)
            //Toast.makeText(search_history.this, text1, Toast.LENGTH_SHORT).show();
            History_list = Arrays.asList(list.split(" , "))
            Collections.reverse(History_list)
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.search_listview, R.id.textView, History_list)
            simpleList = findViewById(R.id.listVenue2)
            simpleList.setAdapter(arrayAdapter)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}