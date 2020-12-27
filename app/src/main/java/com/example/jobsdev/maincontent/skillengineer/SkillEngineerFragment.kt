package com.example.jobsdev.maincontent.skillengineer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentSkillEngineerBinding

class SkillEngineerFragment : Fragment() {

    private lateinit var binding : FragmentSkillEngineerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_skill_engineer, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exampleListSkill = generateDummyList(10)
        binding.rvSkillEngineer.apply {
            adapter =
                RecyclerViewSkillEngineerAdapter(
                    exampleListSkill
                )
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun generateDummyList(size : Int) : List<ItemSkillEngineerDataClass> {
        val list = ArrayList<ItemSkillEngineerDataClass>()

        for (i in 0 until size) {
            val skillName = when(i%3) {
                0 -> "Kotlin"
                1 -> "Java"
                else -> "Laravel"
            }
            val item = ItemSkillEngineerDataClass("${i + 1}", skillName)
            list += item
        }
        return list
    }

}