package com.example.mydailer.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mydailer.R

class DayView(view: View) : ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.name)
    val phone: TextView = view.findViewById(R.id.phone)
    val description: TextView = view.findViewById(R.id.description)
}