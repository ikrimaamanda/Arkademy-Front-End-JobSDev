package com.example.jobsdev.maincontent.portfolioengineer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R

class RecyclerViewListPortfolioAdapter(private val exampleList: List<ItemPortfolioDataClass>) : RecyclerView.Adapter<RecyclerViewListPortfolioAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.example_item_portfolio, parent, false)
        return viewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = exampleList[position]

        holder.imageView.setImageResource(currentItem.imageProfile)

    }

    override fun getItemCount(): Int = exampleList.size

    class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val imageView : ImageView = itemView.findViewById(R.id.iv_image_portofolio)

    }

}