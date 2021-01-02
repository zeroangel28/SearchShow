package com.android.searchshow.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.Exception


private const val TAG = "ShowDatabaseHelper"
private const val DatabaseName = "Show.db"
private const val DatabaseVersion: Int = 1


class ShowDatabaseHelper(context: Context) : SQLiteOpenHelper(
        context,
        DatabaseName,
        null,
        DatabaseVersion
) {
    private val ShowTable = "ShowTable"
    private val SearchTable = "SearchTable"
    private val context = context

    override fun onCreate(db: SQLiteDatabase?) {
        val SQLShowTable = "CREATE TABLE IF NOT EXISTS " + ShowTable + "( " +
                "Drama_id INTEGER PRIMARY KEY NOT NULL, " +
                "Name TEXT NOT NULL, " +
                "Total_views TEXT," +
                "Created_at TEXT," +
                "Thumb TEXT," +
                "Rating TEXT" +
                ");"
        val SQLSearchTable = "CREATE TABLE IF NOT EXISTS " + SearchTable + "( " +
                "Id INTEGER PRIMARY KEY NOT NULL, " +
                "Search TEXT" +
                ");"
        if (db != null) {
            db.execSQL(SQLShowTable)
            db.execSQL(SQLSearchTable)
        }else{
            Log.e(TAG, "The database is not exit.")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.e(TAG, "Now Version is 1. If update, please upgrade table.")
    }

    fun addShowData(
            drama_id: Int,
            name: String,
            total_views: String,
            created_at: String,
            thumb: String,
            rating: String
    ) {
        try {
            val db = writableDatabase
            val values = ContentValues()
            values.put("Drama_id", drama_id)
            values.put("Name", name)
            values.put("Total_views", total_views)
            values.put("Created_at", created_at)
            values.put("Thumb", thumb)
            values.put("Rating", rating)
            db.insert(ShowTable, null, values)
        }catch (e: Exception){
            Log.e(TAG, "Already has this Data.")
        }
    }

    fun showAll() : ArrayList<Show> {
        val db: SQLiteDatabase = getReadableDatabase ()
        val cursor: Cursor = db.rawQuery("SELECT * FROM " + ShowTable, null)
        val showList: ArrayList<Show> = ArrayList()
        var show: Show? = null
        while (cursor.moveToNext()) {
            val hashMap: HashMap<String, String> = HashMap<String, String>()

            var drama_id: Int = cursor.getInt(cursor.getColumnIndex("Drama_id"))
            var name: String = cursor.getString(cursor.getColumnIndex("Name"))
            var total_views: String = cursor.getString(cursor.getColumnIndex("Total_views"))
            var created_at: String = cursor.getString(cursor.getColumnIndex("Created_at"))
            var thumb: String = cursor.getString(cursor.getColumnIndex("Thumb"))
            var rating: String = cursor.getString(cursor.getColumnIndex("Rating"))

            show = Show(drama_id, name, thumb, total_views, created_at, rating)
            showList.add(show)
        }
        return showList
    }

    fun searchString():String{
        var search: String = ""
        val db = readableDatabase
        try {
            val cursor: Cursor = db.rawQuery(" SELECT * FROM " + SearchTable + " WHERE Id ='1'", null)
            while (cursor.moveToNext()) {
                search = cursor.getString(cursor.getColumnIndex("Search"))
                Log.i(TAG, "Search : " + search)
            }
        }catch(e: Exception){
            initSearchString()
        }
        return search
    }

    fun initSearchString(){
        val db = writableDatabase
        val values = ContentValues()
        values.put("Id", 1)
        values.put("Search", "")
        db.insert(SearchTable, null, values)
    }

    fun modify(id: Int, search: String) {
        val db = writableDatabase
        db.execSQL(" UPDATE " + SearchTable +
                " SET Search=" + "'" + search +
                "' WHERE Id=" + "'" + id + "'")
    }
}