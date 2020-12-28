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
import com.example.jobsdev.databinding.FragmentSearchBinding
import com.example.jobsdev.maincontent.dataclass.ItemEngineerDataClass
import com.example.jobsdev.maincontent.hireengineer.DetailEngineerActivity
import com.example.jobsdev.maincontent.recyclerview.OnListEngineerClickListener
import com.example.jobsdev.maincontent.recyclerview.RecyclerViewListEngineerAdapter

class SearchFragment : Fragment(), OnListEngineerClickListener {

    private lateinit var binding : FragmentSearchBinding
    var listEngineer = ArrayList<ItemEngineerDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listEngineer = generateDummyList(100)

        var engineerAdapter = RecyclerViewListEngineerAdapter(listEngineer, this)
        binding.recyclerViewSearchEngineer.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewSearchEngineer.adapter = engineerAdapter

    }

    private fun generateDummyList(size : Int) : ArrayList<ItemEngineerDataClass> {
        val list = ArrayList<ItemEngineerDataClass>()

        for (i in 0 until size) {
            val drawable = when(i%3) {
                0 -> R.drawable.profile_pict
                1 -> R.drawable.profile_pict_2
                else -> R.drawable.profile_pict_3
            }

            val name = when(i%3) {
                0 -> "Marinda Yunella"
                1 -> "Alvita Limantara"
                else -> "Amala Audina"
            }

            val jobTitle = when(i%3) {
                0 -> "Web Developer"
                1 -> "Android Developer"
                else -> "DevOps"
            }

            val item = ItemEngineerDataClass(drawable, name, jobTitle, "Kotlin", "Java", "Laravel", "3+")
            list += item
        }
        return list
    }

    override fun onEngineerItemClicked(position: Int) {
        Toast.makeText(requireContext(), "Engineer $position clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), DetailEngineerActivity::class.java)
        intent.putExtra("name", listEngineer[position].name)
        intent.putExtra("jobTitle", listEngineer[position].jobTitle)
        intent.putExtra("image", listEngineer[position].imageProfile)
        startActivity(intent)
    }
}