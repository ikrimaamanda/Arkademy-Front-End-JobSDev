package com.example.jobsdev.maincontent.portfolioengineer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R

class RecyclerViewListPortfolioAdapter(private val exampleList: ArrayList<ItemPortfolioDataClass>, private val onPortfolioClickListener : OnPortfolioClickListener) : RecyclerView.Adapter<RecyclerViewListPortfolioAdapter.PortfolioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.example_item_portfolio, parent, false)
        return PortfolioViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        val currentItem = exampleList[position]

        holder.imageView.setImageResource(currentItem.imageProfile)

        holder.itemView.setOnClickListener {
            onPortfolioClickListener.onPortfolioItemClicked(position)
        }

    }

    override fun getItemCount(): Int = exampleList.size

    class PortfolioViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val imageView : ImageView = itemView.findViewById(R.id.iv_image_portofolio)

    }

    interface OnPortfolioClickListener {
        fun onPortfolioItemClicked(position: Int)
    }

}