package com.example.mydailer.adapter

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mydailer.R
import com.example.mydailer.domain.objects.Phone

class DayView(view: View) : ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.name)
    private val phone: TextView = view.findViewById(R.id.phone)
    private val description: TextView = view.findViewById(R.id.description)

    init {
        view.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${phone.text}")
            view.context.startActivity(intent)
        }
    }

    fun bind(item: Phone) = with(itemView) {
        name.text = item.name
        phone.text = item.phone
        description.text = item.type
    }
}