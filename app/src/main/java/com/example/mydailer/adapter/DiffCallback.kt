package com.example.mydailer.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.mydailer.domain.objects.Phone

class DiffCallback : DiffUtil.ItemCallback<Phone>() {
    override fun areItemsTheSame(oldItem: Phone, newItem: Phone): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Phone, newItem: Phone): Boolean {
        return oldItem == newItem
    }
}