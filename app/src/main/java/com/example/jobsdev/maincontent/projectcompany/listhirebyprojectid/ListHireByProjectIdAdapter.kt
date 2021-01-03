package com.example.jobsdev.maincontent.projectcompany.listhirebyprojectid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ExampleItemHireByProjectIdBinding

class ListHireByProjectIdAdapter(private val listHireByProjectId: ArrayList<HireByProjectIdModel>, private val onHireByProjectIdClickListener : OnHireByProjectIdClickListener) : RecyclerView.Adapter<ListHireByProjectIdAdapter.HireByProjectViewHolder>() {

    fun addListHireByProject(list : List<HireByProjectIdModel>) {
        listHireByProjectId.clear()
        listHireByProjectId.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HireByProjectViewHolder {
        return HireByProjectViewHolder(
            DataBindingUtil.inflate(
                (LayoutInflater.from(parent.context)),
                R.layout.example_item_hire_by_project_id,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HireByProjectViewHolder, position: Int) {
        val item = listHireByProjectId[position]
        var img = "http://54.236.22.91:4000/image/${item.enProfilePict}"

        holder.binding.tvEngineerName.text = item.acName
        holder.binding.tvJobTitle.text = item.enJobTitle
        holder.binding.tvJobType.text = item.enJobType
        holder.binding.tvDateConfirm.text = item.dateConfirm
        holder.binding.tvPrice.text = item.hirePrice
        holder.binding.tvStatus.text = item.hireStatus

        Glide.with(holder.itemView)
            .load(img)
            .placeholder(R.drawable.profile_pict_base)
            .error(R.drawable.profile_pict_base)
            .into(holder.binding.civProfilePict)

        holder.itemView.setOnClickListener {
            onHireByProjectIdClickListener.onHireByProjectIdItemClicked(position)
        }

    }

    override fun getItemCount(): Int = listHireByProjectId.size

    class HireByProjectViewHolder(val binding : ExampleItemHireByProjectIdBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnHireByProjectIdClickListener {
        fun onHireByProjectIdItemClicked(position: Int)
    }

}