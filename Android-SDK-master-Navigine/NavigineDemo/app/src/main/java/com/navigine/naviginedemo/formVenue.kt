package com.navigine.naviginedemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.List
import ContextMenu.ContextMenuInfo
import AdapterView.AdapterContextMenuInfo

class formVenue : Activity() {
    var text1: String? = null
    var venues: List<String> = ArrayList<String>()

    @Override
    protected fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        text1 = getIntent().getStringExtra("List")
        setContentView(R.layout.venue_form)
    }

    fun onCheckboxClicked(view: View) {
        // Is the view now checked?
        val checked: Boolean = (view as CheckBox).isChecked()
        when (view.getId()) {
            R.id.L308 -> if (checked) venues.add("L308") else venues.remove("L308")
            R.id.L311 -> if (checked) venues.add("L311") else venues.remove("L311")
            R.id.L312 -> if (checked) venues.add("L312") else if (!checked) venues.remove("L312")
            R.id.L313 -> if (checked) venues.add("L313") else venues.remove("L313")
            R.id.L314 -> if (checked) venues.add("L314") else venues.remove("L314")
            R.id.L306 -> if (checked) venues.add("L306") else venues.remove("L306")
            R.id.L310 -> if (checked) venues.add("L310") else venues.remove("L310")
            R.id.L309 -> if (checked) venues.add("L309") else venues.remove("L309")
            R.id.L335 -> if (checked) venues.add("L335") else venues.remove("L335")
            R.id.L348 -> if (checked) venues.add("L348") else venues.remove("L348")
            R.id.L330 -> if (checked) venues.add("L330") else venues.remove("L330")
            R.id.FYP -> if (checked) venues.add("FYP Entrance/Exit") else venues.remove("FYP Entrance/Exit")
            R.id.BlkL -> if (checked) venues.add("Blk L - Level 3 Entrance/Exit") else venues.remove("Blk L - Level 3 Entrance/Exit")
        }
    }

    fun add(v: View?) {
        var text2: String? = null
        if (venues.isEmpty()) {
            Toast.makeText(this@formVenue, "Please choose at least 1 venue", Toast.LENGTH_SHORT).show()
        } else {
            for (i in 0 until venues.size()) {
                text2 = if (text2 == null) {
                    venues[i]
                } else {
                    text2.toString() + " , " + venues[i]
                }
            }
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
            val intent = Intent(this, VenuePage::class.java)
            startActivity(intent)
        }
    }

    fun back(v: View?) {
        val intent = Intent(this, VenuePage::class.java)
        startActivity(intent)
    }
}