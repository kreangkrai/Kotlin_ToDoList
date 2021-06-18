package com.example.todolist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VHolder(v:View):RecyclerView.ViewHolder(v) {
    var textTitle = v.findViewById<TextView>(R.id.textView_title)
    var textDate = v.findViewById<TextView>(R.id.textView_date)
    var textMonthYear = v.findViewById<TextView>(R.id.textView_month_year)
    var imgTrash = v.findViewById<ImageView>(R.id.imageView_trash)
}