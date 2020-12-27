package com.example.jobsdev.maincontent.skillengineer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R

class RecyclerViewSkillEngineerAdapter(private val listSkill : ArrayList<ItemSkillEngineerDataClass>, private val onSkillClickListener : OnSkillClickListener) : RecyclerView.Adapter<RecyclerViewSkillEngineerAdapter.SkillViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.example_item_skill, parent, false)
        return SkillViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int = listSkill.size

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val currentItem = listSkill[position]

        holder.skillName.text = currentItem.SkillName

        holder.itemView.setOnClickListener {
            onSkillClickListener.onSkillItemClicked(position)
        }
    }

    class SkillViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val skillName : TextView = itemView.findViewById(R.id.tv_skill_name)
    }

    interface OnSkillClickListener {
        fun onSkillItemClicked(position: Int)
    }
}