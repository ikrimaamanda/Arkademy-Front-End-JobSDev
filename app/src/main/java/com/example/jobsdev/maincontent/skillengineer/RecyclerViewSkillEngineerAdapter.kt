package com.example.jobsdev.maincontent.skillengineer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R

class RecyclerViewSkillEngineerAdapter(private val listSkill : List<ItemSkillEngineerDataClass>) : RecyclerView.Adapter<RecyclerViewSkillEngineerAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.example_item_skill, parent, false)
        return viewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int = listSkill.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = listSkill[position]

        holder.skillName.text = currentItem.SkillName
    }

    class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val skillName : TextView = itemView.findViewById(R.id.tv_skill_name)
    }


}