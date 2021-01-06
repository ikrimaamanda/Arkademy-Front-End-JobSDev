package com.example.jobsdev.maincontent.experienceengineer

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ExampleItemExperienceBinding
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class RecyclerViewListExperienceAdapter(private val listExperience : ArrayList<ItemExperienceModel>, private val onListExperienceClickListener : OnListExperienceClickListener) : RecyclerView.Adapter<RecyclerViewListExperienceAdapter.ExperienceViewHolder>() {

    fun addListExperience(list : List<ItemExperienceModel>) {
        listExperience.clear()
        listExperience.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        return ExperienceViewHolder(
            DataBindingUtil.inflate((LayoutInflater.from(parent.context)), R.layout.example_item_experience, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
        val currentItem = listExperience[position]
        val dateStart = currentItem.startDate!!.split("T")[0]
        val dateEnd = currentItem.endDate!!.split("T")[0]
        val sum = ChronoUnit.MONTHS.between(LocalDate.parse(dateStart).withDayOfMonth(1), LocalDate.parse(dateEnd).withDayOfMonth(1))

        holder.binding.tvExperiencePosition.text = currentItem.position
        holder.binding.tvExperienceCompany.text = currentItem.company
        holder.binding.tvStartDate.text = dateStart
        holder.binding.tvEndDate.text = dateEnd
        holder.binding.tvExperienceMonths.text = "$sum Month"
        holder.binding.tvExperienceDesc.text = currentItem.description


        holder.itemView.setOnClickListener {
            onListExperienceClickListener.onExperienceItemClicked(position)
        }
    }

    override fun getItemCount(): Int = listExperience.size

    class ExperienceViewHolder(val binding : ExampleItemExperienceBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnListExperienceClickListener {
        fun onExperienceItemClicked(position : Int)
    }

}