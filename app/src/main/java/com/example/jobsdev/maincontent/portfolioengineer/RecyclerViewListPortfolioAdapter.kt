package com.example.jobsdev.maincontent.portfolioengineer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ExampleItemPortfolioBinding

class RecyclerViewListPortfolioAdapter(private val listPortfolio: ArrayList<ItemPortfolioModel>, private val onPortfolioClickListener : OnPortfolioClickListener) : RecyclerView.Adapter<RecyclerViewListPortfolioAdapter.PortfolioViewHolder>() {

    fun addListPortfolio(list : List<ItemPortfolioModel>) {
        listPortfolio.clear()
        listPortfolio.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        return PortfolioViewHolder(
            DataBindingUtil.inflate((LayoutInflater.from(parent.context)),
            R.layout.example_item_portfolio, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        val currentItem = listPortfolio[position]
        var img = "http://54.236.22.91:4000/image/${currentItem.portfolioImage}"

        Glide.with(holder.itemView)
            .load(img)
            .placeholder(R.drawable.profile_pict_base)
            .error(R.drawable.profile_pict_base)
            .into(holder.binding.ivImagePortofolio)

        holder.itemView.setOnClickListener {
            onPortfolioClickListener.onPortfolioItemClicked(position)
        }

    }

    override fun getItemCount(): Int = listPortfolio.size

    class PortfolioViewHolder(val binding : ExampleItemPortfolioBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnPortfolioClickListener {
        fun onPortfolioItemClicked(position: Int)
    }

}