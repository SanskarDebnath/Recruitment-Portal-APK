package com.example.recruitment_portal

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BannerAdapter(private val banners: List<String>) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    class BannerViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner, parent, false) as TextView

        return BannerViewHolder(textView)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.textView.text = banners[position]
    }

    override fun getItemCount(): Int = banners.size
}