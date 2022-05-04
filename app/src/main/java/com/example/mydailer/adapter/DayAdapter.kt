package com.example.mydailer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.mydailer.R
import com.example.mydailer.domain.objects.Phone

class DayAdapter(var phoneList: List<Phone>) : ListAdapter<Phone, DayView>(DiffCallback()) {
    val original = ArrayList<Phone>(phoneList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView = DayView(
        LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false))

    override fun onBindViewHolder(holder: DayView, position: Int) {
        holder.bind(phoneList[position])
    }

    override fun getItemCount(): Int = phoneList.size
}