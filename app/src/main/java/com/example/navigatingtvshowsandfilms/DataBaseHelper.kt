package com.example.navigatingtvshowsandfilms

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DataBaseHelper : SQLiteOpenHelper {
    private val dbTable = "Shows"
    private val colId = "id"
    private val colName = "name"
    private val colType = "type"
    private val colInProgress = "inProgress"
    private val colWatched = "watched"

    private val CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + dbTable + " (" + colId + " INTEGER PRIMARY KEY," +
            colName + " TEXT, " + colType + " TEXT, " + colInProgress + " TEXT, " + colWatched + " TEXT);"
    private var db: SQLiteDatabase? = null

    constructor(context: Context?) : super(context, "DBShows", null, 1) {

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists Shows")
    }

    fun insertObject(show: Show):Boolean{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(colName, show.name)
        contentValues.put(colType, show.type)
        contentValues.put(colInProgress, show.inProgress)
        contentValues.put(colWatched, show.watched)
        val insert = db.insert(dbTable, null, contentValues)
        return insert != -1L
    }
    fun getObjects(condition: String): ArrayList<Show>{
        val db = this.readableDatabase
        var cursor: Cursor = db.rawQuery("Select * from $dbTable", null)
        if(condition != "")
            cursor = db.rawQuery("Select * from $dbTable where type = ?", arrayOf(condition))
        val list: ArrayList<Show> = ArrayList<Show>()
        if(cursor != null && cursor.moveToFirst()) {
            do{
                val show = Show()
                show.id = cursor.getInt(cursor.getColumnIndexOrThrow(colId))
                show.name = cursor.getString(cursor.getColumnIndexOrThrow(colName))
                show.type = cursor.getString(cursor.getColumnIndexOrThrow(colType))
                show.inProgress = cursor.getString(cursor.getColumnIndexOrThrow(colInProgress))
                show.watched = cursor.getString(cursor.getColumnIndexOrThrow(colWatched))
                list.add(show)
            }while(cursor.moveToNext())
        }
        return list
    }
    fun deleteObject(id: String): Int{
        val db = this.readableDatabase
        db.delete(dbTable, "id = ?", arrayOf(id))
        val cursor: Cursor = db.rawQuery("Select * from $dbTable where id = ?", arrayOf(id))
        return cursor.count
    }
    fun updateObject(id: Int, progress: Boolean, watched: Boolean){
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("Select * from $dbTable where id = ?", arrayOf(id.toString()))
        cursor.moveToFirst()
        val contentValues = ContentValues()
        contentValues.put(colName, cursor.getString(cursor.getColumnIndexOrThrow(colName)))
        contentValues.put(colType, cursor.getString(cursor.getColumnIndexOrThrow(colType)))
        if(progress) {
            if(cursor.getString(cursor.getColumnIndexOrThrow(colInProgress)) == "Завершен")
                contentValues.put(colInProgress, "В разработке")
            else
                contentValues.put(colInProgress, "Завершен")
            contentValues.put(colWatched, cursor.getString(cursor.getColumnIndexOrThrow(colWatched)))
        }
        if(watched) {
            if(cursor.getString(cursor.getColumnIndexOrThrow(colWatched)) == "Просмотрено")
                contentValues.put(colWatched, "Не просмотрено")
            else
                contentValues.put(colWatched, "Просмотрено")
            contentValues.put(colInProgress, cursor.getString(cursor.getColumnIndexOrThrow(colInProgress)))
        }
        db.update(dbTable, contentValues, "$colId=$id", null)
    }
    fun getType(id: Int): String{
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("Select * from $dbTable where id = ?", arrayOf(id.toString()))
        cursor.moveToFirst()
        return cursor.getString(cursor.getColumnIndexOrThrow(colType))
    }
}