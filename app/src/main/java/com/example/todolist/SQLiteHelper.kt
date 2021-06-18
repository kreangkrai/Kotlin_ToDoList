package com.example.todolist

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper private constructor(context:Context):SQLiteOpenHelper(context,DB_NAME,null,DB_VERSION){
    companion object{
        private val DB_NAME = "mydb"
        private  val DB_VERSION = 1
        private var mSqliteHelper:SQLiteHelper? = null
        @Synchronized
        fun getInstance(c:Context):SQLiteHelper{
            return if (mSqliteHelper == null){
                SQLiteHelper(c.applicationContext)
            }else{
                mSqliteHelper!!
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var sql = """CREATE TABLE todo (
            _id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT,
            timestamp REAL)"""
        db!!.execSQL(sql)

        val now = System.currentTimeMillis()
        sql = """INSERT INTO todo(_id,title,timestamp) VALUES
            (null,'JOB1',$now)"""
        db!!.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}