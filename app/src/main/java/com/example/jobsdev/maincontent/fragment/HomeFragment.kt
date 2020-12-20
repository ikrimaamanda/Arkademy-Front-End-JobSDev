package com.example.jobsdev.maincontent.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.adapter.RecyclerViewAdapter
import com.example.jobsdev.maincontent.dataclass.SearchItemDataClass

class HomeFragment : Fragment() {

    private lateinit var rootView : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exampleList = generateDummyList(100)

        val recyclerView : RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.apply {
            adapter =
                RecyclerViewAdapter(exampleList)
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun generateDummyList(size : Int) : List<SearchItemDataClass> {
        val list = ArrayList<SearchItemDataClass>()

        for (i in 0 until size) {
            val drawable = when(1%3) {
                0 -> R.drawable.profile_pict
                1 -> R.drawable.profile_pict
                else -> R.drawable.profile_pict
            }

            val item = SearchItemDataClass(drawable, "Marinda Yunella", "Web Developer", "Kotlin", "Java", "Laravel", "3+")
            list += item
        }
        return list
    }
}