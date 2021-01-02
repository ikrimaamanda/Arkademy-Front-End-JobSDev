package com.example.jobsdev.maincontent.listengineer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ExampleItemEngineerBinding
import com.example.jobsdev.maincontent.dataclass.ItemEngineerDataClass
import com.example.jobsdev.maincontent.recyclerview.OnListEngineerClickListener

class ListEngineerAdapter(private val listEngineer : ArrayList<DetailEngineerModel>, private val onListEngineerClickListener: OnListEngineerClickListener) : RecyclerView.Adapter<ListEngineerAdapter.ListEngineerViewHolder>() {

    fun addListEngineer(list : List<DetailEngineerModel>) {
        listEngineer.clear()
        listEngineer.addAll(list)
        notifyDataSetChanged()
    }

    class ListEngineerViewHolder( val binding : ExampleItemEngineerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListEngineerViewHolder {
        return ListEngineerViewHolder(DataBindingUtil.inflate((LayoutInflater.from(parent.context)), R.layout.example_item_engineer, parent, false))
    }

    override fun getItemCount(): Int = listEngineer.size

    override fun onBindViewHolder(holder: ListEngineerViewHolder, position: Int) {
        val item = listEngineer[position]
        var img = "http://54.236.22.91:4000/image/${item.engineerProfilePict}"

        holder.binding.tvName.text = item.engineerName
        holder.binding.tvJobTitle.text = item.engineerJobTitle
        holder.binding.tvJobType.text = item.engineerJobType

        Glide.with(holder.itemView)
            .load(img)
            .placeholder(R.drawable.profile_pict_base)
            .error(R.drawable.profile_pict_base)
            .into(holder.binding.civProfilePict)

        holder.itemView.setOnClickListener {
            onListEngineerClickListener.onEngineerItemClicked(position)
        }
    }


}