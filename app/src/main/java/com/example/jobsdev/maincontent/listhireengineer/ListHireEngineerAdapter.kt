package com.example.jobsdev.maincontent.listhireengineer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ExampleItemHireEngineerBinding

class ListHireEngineerAdapter(private val listHireEngineer : ArrayList<DetailHireEngineerModel>, private val onListHireEngineerClickListener: OnListHireEngineerClickListener) : RecyclerView.Adapter<ListHireEngineerAdapter.ListHireEngineerViewHolder>() {

    fun addListHireEngineer(list : List<DetailHireEngineerModel>) {
        listHireEngineer.clear()
        listHireEngineer.addAll(list)
        notifyDataSetChanged()
    }

    class ListHireEngineerViewHolder( val binding : ExampleItemHireEngineerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHireEngineerViewHolder {
        return ListHireEngineerViewHolder(DataBindingUtil.inflate((LayoutInflater.from(parent.context)), R.layout.example_item_hire_engineer, parent, false))
    }

    override fun getItemCount(): Int = listHireEngineer.size

    override fun onBindViewHolder(holder: ListHireEngineerViewHolder, position: Int) {
        val item = listHireEngineer[position]

        holder.binding.tvProjectName.text = item.projectName
        holder.binding.tvCompanyName.text = item.companyName
        holder.binding.tvLocation.text = item.companyCity
        holder.binding.tvPrice.text = item.price.toString()
        holder.binding.btnSeeDetail.setOnClickListener {
            onListHireEngineerClickListener.onHireEngineerItemClicked(position)
        }

        holder.itemView.setOnClickListener {
            onListHireEngineerClickListener.onHireEngineerItemClicked(position)
        }
    }

    interface OnListHireEngineerClickListener {
        fun onHireEngineerItemClicked(position : Int)
    }
}