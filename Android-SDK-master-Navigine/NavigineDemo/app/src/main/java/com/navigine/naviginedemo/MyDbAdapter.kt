package com.navigine.naviginedemo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import java.util.ArrayList
import java.util.HashMap
import ContextMenu.ContextMenuInfo
import AdapterView.AdapterContextMenuInfo
import com.navigine.naviginedemo.MyDbAdapter.Companion.CONTACTS_TABLE_NAME

class MyDbAdapter(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    private val hp: HashMap? = null
    fun onCreate(db: SQLiteDatabase) {
        // TODO Auto-generated method stub
        db.execSQL("create table Shortcut" + "(id integer primary key, name text, place text)"
        )
    }

    @Override
    fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts")
        onCreate(db)
    }

    fun insertShortcut(name: String?, place: String?): Boolean {
        val db: SQLiteDatabase = this.getWritableDatabase()
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("place", place)
        db.insert("Shortcut", null, contentValues)
        return true
    }

    fun getData(id: Int): Cursor {
        val db: SQLiteDatabase = this.getReadableDatabase()
        return db.rawQuery("select * from contacts where id=$id", null)
    }

    fun numberOfRows(): Int {
        val db: SQLiteDatabase = this.getReadableDatabase()
        return DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME)
    }

    fun updateShortcut(id: Integer?, name: String?, place: String?): Boolean {
        val db: SQLiteDatabase = this.getWritableDatabase()
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("place", place)
        db.update("Shortcut", contentValues, "id = ? ", arrayOf<String>(Integer.toString(id)))
        return true
    }

    fun deleteContact(id: Integer?): Integer {
        val db: SQLiteDatabase = this.getWritableDatabase()
        return db.delete("Shortcut",
                "id = ? ", arrayOf<String>(Integer.toString(id)))
    }

    //hp = new HashMap();
    val allCotacts: ArrayList<String>
        get() {
            val array_list: ArrayList<String> = ArrayList<String>()

            //hp = new HashMap();
            val db: SQLiteDatabase = this.getReadableDatabase()
            val res: Cursor = db.rawQuery("select * from Shortcut", null)
            res.moveToFirst()
            while (res.isAfterLast() === false) {
                array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)).toString() + " : " + res.getString(res.getColumnIndex(CONTACTS_COLUMN_PLACE)))
                res.moveToNext()
            }
            return array_list
        }

    companion object {
        const val DATABASE_NAME = "MyDB.db"
        const val CONTACTS_TABLE_NAME = "Shortcut"
        const val CONTACTS_COLUMN_ID = "id"
        const val CONTACTS_COLUMN_NAME = "name"
        const val CONTACTS_COLUMN_PLACE = "place"
    }
}