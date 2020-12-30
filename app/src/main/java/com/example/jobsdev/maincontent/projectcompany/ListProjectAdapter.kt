package com.example.jobsdev.maincontent.projectcompany

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ItemProjectCompanyBinding

class ListProjectAdapter(private val listProjectCompany : ArrayList<ProjectCompanyModel>, private val onListProjectCompany : OnListProjectCompanyClickListener) : RecyclerView.Adapter<ListProjectAdapter.ProjectHolder>() {

    fun addListProjectCompany(list : List<ProjectCompanyModel>) {
        listProjectCompany.clear()
        listProjectCompany.addAll(list)
        notifyDataSetChanged()
    }

    class ProjectHolder(val binding : ItemProjectCompanyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectHolder {
        return ProjectHolder(DataBindingUtil.inflate((LayoutInflater.from(parent.context)), R.layout.item_project_company, parent, false))
    }

    override fun getItemCount(): Int = listProjectCompany.size

    override fun onBindViewHolder(holder: ProjectHolder, position: Int) {
        val item = listProjectCompany[position]
        var img = "http://54.236.22.91:4000/image/${item.projectImage}"

        holder.binding.tvProjectName.text = item.projectName
        holder.binding.tvDescriptionProject.text = item.projectDesc
        holder.binding.tvDeadline.text = item.projectDeadline
        holder.binding.tvDeadline.text = item.projectDeadline

        Glide.with(holder.itemView)
            .load(img)
            .placeholder(R.drawable.profile_pict_base)
            .error(R.drawable.profile_pict_base)
            .into(holder.binding.ivImageProject)

        holder.itemView.setOnClickListener {
            onListProjectCompany.onProjectCompanyItemClicked(position)
        }
    }

    interface OnListProjectCompanyClickListener {
        fun onProjectCompanyItemClicked(position : Int)
    }
}