package com.example.jobsdev.maincontent.hireengineer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.maincontent.portfolioengineer.ItemPortfolioDataClass

class RecyclerViewListPortfolioDetailEngineerAdapter(private val exampleListExperience : ArrayList<ItemPortfolioDataClass>?) : RecyclerView.Adapter<RecyclerViewListPortfolioDetailEngineerAdapter.DetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.example_item_portfolio, parent, false)
        return DetailViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val currentItem = exampleListExperience?.get(position)

        holder.imageView.setImageResource(currentItem!!.imageProfile)
    }

    override fun getItemCount(): Int = exampleListExperience!!.size

    class DetailViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView?.findViewById(R.id.iv_image_portofolio)
    }

}