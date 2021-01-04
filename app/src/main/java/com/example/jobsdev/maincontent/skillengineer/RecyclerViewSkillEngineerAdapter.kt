package com.example.jobsdev.maincontent.skillengineer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ExampleItemSkillBinding

class RecyclerViewSkillEngineerAdapter(private val listSkill : ArrayList<ItemSkillEngineerModel>, private val onSkillClickListener : OnSkillClickListener) : RecyclerView.Adapter<RecyclerViewSkillEngineerAdapter.SkillViewHolder>() {

    fun addSkill(list : List<ItemSkillEngineerModel>) {
        listSkill.clear()
        listSkill.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        return SkillViewHolder(
            DataBindingUtil.inflate(
                (LayoutInflater.from(parent.context)),
                R.layout.example_item_skill,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listSkill.size

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val currentItem = listSkill[position]

        holder.binding.tvSkillName.text = currentItem.SkillName

        holder.itemView.setOnClickListener {
            onSkillClickListener.onSkillItemClicked(position)
        }
    }

    class SkillViewHolder(val binding : ExampleItemSkillBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnSkillClickListener {
        fun onSkillItemClicked(position: Int)
    }
}