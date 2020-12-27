package com.example.jobsdev.maincontent.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.maincontent.recyclerview.RecyclerViewListEngineerAdapter
import com.example.jobsdev.databinding.FragmentHomeBinding
import com.example.jobsdev.maincontent.dataclass.ItemEngineerDataClass
import com.example.jobsdev.maincontent.hireengineer.DetailEngineerActivity
import com.example.jobsdev.maincontent.recyclerview.OnListEngineerClickListener

class HomeFragment : Fragment(), OnListEngineerClickListener {

    private lateinit var binding : FragmentHomeBinding
    var listEngineer = ArrayList<ItemEngineerDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listEngineer = generateDummyList(100)

        var engineerAdapter = RecyclerViewListEngineerAdapter(listEngineer, this)
        binding.recyclerViewListEngineer.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewListEngineer.adapter = engineerAdapter

    }

    private fun generateDummyList(size : Int) : ArrayList<ItemEngineerDataClass> {

        val list = ArrayList<ItemEngineerDataClass>()

        for (i in 0 until size) {
            val drawable = when(i%3) {
                0 -> R.drawable.profile_pict
                1 -> R.drawable.profile_pict_2
                else -> R.drawable.profile_pict_3
            }

            val item = ItemEngineerDataClass(drawable, "Marinda Yunella", "Web Developer", "Kotlin", "Java", "Laravel", "3+")
            list += item
            val item2 = ItemEngineerDataClass(drawable, "Alvita Limantara", "Android Developer", "Kotlin", "Java", "Laravel", "3+")
            list += item2
        }
        return list
    }

    override fun onEngineerItemClicked(position: Int) {
        Toast.makeText(requireContext(), "${listEngineer[position].name} clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), DetailEngineerActivity::class.java)
        intent.putExtra("name", listEngineer[position].name)
        intent.putExtra("jobTitle", listEngineer[position].jobTitle)
        startActivity(intent)
    }
}