package com.example.jobsdev.maincontent.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.maincontent.dataclass.ItemEngineerDataClass

class RecyclerViewListEngineerAdapter(private val exampleList : List<ItemEngineerDataClass>) : RecyclerView.Adapter<RecyclerViewListEngineerAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.example_item_engineer, parent, false)
        return viewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = exampleList[position]

        holder.imageView.setImageResource(currentItem.imageProfile)
        holder.name.text = currentItem.name
        holder.jobTitle.text = currentItem.jobTitle
        holder.skillOne.text = currentItem.skillOne
        holder.skillTwo.text = currentItem.skillTwo
        holder.skillThree.text = currentItem.skillThree
        holder.countOfAnotherSkill.text = currentItem.countOfAnotherSkill
    }

    override fun getItemCount(): Int = exampleList.size

    class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView.findViewById(R.id.civ_profile_pict)
        val name : TextView = itemView.findViewById(R.id.tv_name)
        val jobTitle : TextView = itemView.findViewById(R.id.tv_job_title)
        val skillOne : TextView = itemView.findViewById(R.id.tv_skill_kotlin)
        val skillTwo : TextView = itemView.findViewById(R.id.tv_skill_java)
        val skillThree : TextView = itemView.findViewById(R.id.tv_skill_laravel)
        val countOfAnotherSkill : TextView = itemView.findViewById(R.id.tv_count_skills)
    }

}