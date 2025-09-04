package com.example.bottomnavigationbar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomnavigationbar.databinding.ItemMessageAryanBinding
import com.example.bottomnavigationbar.databinding.ItemMessageUserBinding
import com.example.bottomnavigationbar.sampledata.AryanChat

class AryanChatAdapter(private val messages: MutableList<AryanChat>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_USER = 1
    private val VIEW_TYPE_ARYAN = 2

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) VIEW_TYPE_USER else VIEW_TYPE_ARYAN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val binding = ItemMessageUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            UserViewHolder(binding)
        } else {
            val binding = ItemMessageAryanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AryanViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messages[position]
        if (holder is UserViewHolder) {
            holder.binding.userText.text = msg.text
        } else if (holder is AryanViewHolder) {
            holder.binding.aryanText.text = msg.text
        }
    }

    override fun getItemCount() = messages.size

    fun addMessage(message: AryanChat) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    inner class UserViewHolder(val binding: ItemMessageUserBinding) : RecyclerView.ViewHolder(binding.root)
    inner class AryanViewHolder(val binding: ItemMessageAryanBinding) : RecyclerView.ViewHolder(binding.root)
}


