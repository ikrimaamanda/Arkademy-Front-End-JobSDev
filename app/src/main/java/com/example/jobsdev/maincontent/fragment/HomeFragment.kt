package com.example.jobsdev.maincontent.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.maincontent.recyclerview.RecyclerViewListEngineerAdapter
import com.example.jobsdev.databinding.FragmentHomeBinding
import com.example.jobsdev.maincontent.dataclass.ItemEngineerDataClass

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

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
        val exampleList = generateDummyList(100)

        val recyclerView : RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.apply {
            adapter =
                RecyclerViewListEngineerAdapter(
                    exampleList
                )
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun generateDummyList(size : Int) : List<ItemEngineerDataClass> {
        val list = ArrayList<ItemEngineerDataClass>()

        for (i in 0 until size) {
            val drawable = when(i%3) {
                0 -> R.drawable.profile_pict
                1 -> R.drawable.profile_pict_2
                else -> R.drawable.profile_pict_3
            }

            val item = ItemEngineerDataClass(drawable, "Marinda Yunella", "Web Developer", "Kotlin", "Java", "Laravel", "3+")
            list += item
        }
        return list
    }
}