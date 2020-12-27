package com.example.jobsdev.maincontent.experienceengineer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R

class RecyclerViewListExperienceAdapter(private val exampleListExperience : List<ItemExperienceDataClass>) : RecyclerView.Adapter<RecyclerViewListExperienceAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.example_item_experience, parent, false)
        return viewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = exampleListExperience[position]

        holder.imageCompany.setImageResource(currentItem.imageCompany)
        holder.position.text = currentItem.position
        holder.company.text = currentItem.company
        holder.date.text = currentItem.date
        holder.months.text = currentItem.months
        holder.description.text = currentItem.description
    }

    override fun getItemCount(): Int = exampleListExperience.size

    class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imageCompany : ImageView = itemView.findViewById(R.id.iv_company)
        val position : TextView = itemView.findViewById(R.id.tv_experience_position)
        val company : TextView = itemView.findViewById(R.id.tv_experience_company)
        val date : TextView = itemView.findViewById(R.id.tv_experience_date)
        val months : TextView = itemView.findViewById(R.id.tv_experience_months)
        val description : TextView = itemView.findViewById(R.id.tv_experience_desc)
    }

}