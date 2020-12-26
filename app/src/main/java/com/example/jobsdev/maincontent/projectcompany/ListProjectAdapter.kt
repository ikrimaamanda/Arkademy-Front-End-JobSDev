package com.example.jobsdev.maincontent.projectcompany

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ItemProjectCompanyBinding

class ListProjectAdapter : RecyclerView.Adapter<ListProjectAdapter.ProjectHolder>() {

    private var items = mutableListOf<ProjectCompanyModel>()

    fun addList(list : List<ProjectCompanyModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class ProjectHolder(val binding : ItemProjectCompanyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectHolder {
        return ProjectHolder(DataBindingUtil.inflate((LayoutInflater.from(parent.context)), R.layout.item_project_company, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ProjectHolder, position: Int) {
        val item = items[position]
        holder.binding.tvName.text = item.projectName
        holder.binding.tvDecription.text = item.projectDesc
        holder.binding.tvDeadline.text = item.projectDeadline
        holder.binding.tvImageProject.text = item.projectImage
        holder.binding.tvCreateProject.text = item.projectCreateAt
        holder.binding.tvUpdateProject.text = item.projectUpdateAt
    }
}