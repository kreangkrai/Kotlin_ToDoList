package com.example.todolist

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mSqliteHelper:SQLiteHelper
    private lateinit var mSqliteDB : SQLiteDatabase
    private lateinit var mRCV:RecyclerView
    private lateinit var toDoDate : Triple<Int?,Int?,Int?>
    private var toDoTitle:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSqliteHelper = SQLiteHelper.getInstance(this)
        mSqliteDB = mSqliteHelper.writableDatabase

        mRCV = findViewById(R.id.recycleView)
        mRCV.layoutManager = LinearLayoutManager(this)
        val itemDecor = DividerItemDecoration(this,DividerItemDecoration.VERTICAL)

        mRCV.addItemDecoration(itemDecor)
        readDatabaseToRecycleView()

        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton).apply {
            setOnClickListener {
                showDatePickerDialog()
            }
        }
    }
    fun readDatabaseToRecycleView(){
        val sql = "SELECT * FROM todo ORDER BY timestamp DESC"
        val cursor = mSqliteDB.rawQuery(sql,null)
        val adapter = Adapter(cursor)
        mRCV.adapter = adapter
    }
    private fun showDatePickerDialog(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        toDoDate = Triple(null,null,null)
        val listener = DatePickerDialog.OnDateSetListener{
            _,year,month,day ->
            toDoDate = Triple(day,month+1,year)
            showCustomInputDialog("")
        }
        val dialog = DatePickerDialog(this@MainActivity,listener,year,month,day)
        dialog.show()
    }
    private fun showCustomInputDialog(message:String){
        val view = layoutInflater.inflate(
            R.layout.activity_dialog_content,null
        )
        val edit = view.findViewById<EditText>(R.id.editTextCustom_Dialog)
        edit.hint = message
        AlertDialog.Builder(this@MainActivity).apply {
            setTitle("สิ่งที่ต้องทำ")
            setView(view)
            setPositiveButton("ตกลง") { _, _ ->
                var str = edit.text.toString()
                toDoTitle = if (!str.trim().isNullOrBlank()) {
                    edit.text.toString()
                } else {
                    null
                }
                if (toDoDate.first != null && toDoTitle != null) {
                    insertDatabase()
                }
            }
            setNegativeButton("ยกเลิก") { _, _ ->
                toDoTitle = null
            }
        }.show()
    }
    private fun insertDatabase(){
        val dateStr = "${toDoDate.first}/${toDoDate.second}/${toDoDate.third}"
        val date = SimpleDateFormat("dd/MM/yyyy").parse(dateStr)
        val timeMillis = date.time
        var sql ="""INSERT INTO todo (_id,title,timestamp)
             VALUES (?,?,?)"""
        var args = arrayOf(null,toDoTitle,timeMillis)
        mSqliteDB.execSQL(sql,args)
        readDatabaseToRecycleView()
    }
}