package com.example.mydailer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.mydailer.R
import com.example.mydailer.domain.objects.Phone

class DayAdapter(var phoneList: List<Phone>) : Adapter<DayView>() {
    val original = ArrayList<Phone>(phoneList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayView = DayView(
        LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false))

    override fun onBindViewHolder(holder: DayView, position: Int) {
        holder.name.text = phoneList[position].name
        holder.phone.text = phoneList[position].phone
        holder.description.text = phoneList[position].type
    }

    override fun getItemCount(): Int = phoneList.size
}