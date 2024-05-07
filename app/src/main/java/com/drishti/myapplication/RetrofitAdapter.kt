package com.drishti.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RetrofitAdapter(private val dataList: List<GetApiKeyItem>) :
RecyclerView.Adapter<RetrofitAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        val emailTextView: TextView = itemView.findViewById(R.id.tvEmail)
        val genderImageView: ImageView = itemView.findViewById(R.id.ivGender)
        val statusImageView: ImageView = itemView.findViewById(R.id.ivStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.retrofit_items, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        var gender = currentItem.gender
        var status = currentItem.status

        holder.nameTextView.text = "Name: "+currentItem.name
        holder.emailTextView.text ="Email: "+currentItem.email

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}