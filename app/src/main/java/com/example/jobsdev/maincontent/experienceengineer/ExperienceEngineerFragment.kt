package com.example.jobsdev.maincontent.experienceengineer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentExperienceEngineerBinding

class ExperienceEngineerFragment : Fragment() {
    private lateinit var binding : FragmentExperienceEngineerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_experience_engineer, container, false)
        binding.btnAddExperience.setOnClickListener {
            startActivity(Intent(activity, AddExperienceActivity::class.java))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exampleListExperience = generateDummyList(5)

        binding.recyclerViewExperience.apply {
            adapter =
                RecyclerViewListExperienceAdapter(
                    exampleListExperience
                )
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun generateDummyList(size : Int) : List<ItemExperienceDataClass> {
        val list = ArrayList<ItemExperienceDataClass>()

        for (i in 0 until size) {
            val drawable = when(i%2) {
                0 -> R.drawable.tokopedia
                else -> R.drawable.instagram_icon
            }

            val item =
                ItemExperienceDataClass(
                    drawable,
                    "Software Engineer",
                    "Tokopedia",
                    "July 2019 - January 2020",
                    "6 months",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
                )
            list += item
        }
        return list
    }
}