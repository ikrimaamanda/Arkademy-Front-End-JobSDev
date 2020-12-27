package com.example.jobsdev.maincontent.hireengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityDetailEngineerBinding
import com.example.jobsdev.maincontent.webview.GitHubWebViewActivity

class DetailEngineerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailEngineerBinding
    private lateinit var pagerAdapter: TabPagerDetailEngineerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_engineer)

        val name = intent.getStringExtra("name")
        binding.tvFullName.text = name
        val jobTitle = intent.getStringExtra("jobTitle")
        binding.tvJobTitle.text = jobTitle

        binding.btnHireEngineer.setOnClickListener {
            startActivity(Intent(this, FormHireActivity::class.java))
        }

        val exampleListSkill = generateDummyList(10)
        binding.rvSkillEngineer.apply {
            adapter =
                RecyclerViewSkillHireEngineerAdapter(
                    exampleListSkill
                )
            layoutManager = LinearLayoutManager(this@DetailEngineerActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        pagerAdapter = TabPagerDetailEngineerAdapter(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun generateDummyList(size : Int) : List<ItemSkillHireEngineerDataClass> {
        val list = ArrayList<ItemSkillHireEngineerDataClass>()

        for (i in 0 until size) {
            val skillName = when(i%3) {
                0 -> "Kotlin"
                1 -> "Java"
                else -> "Laravel"
            }
            val item = ItemSkillHireEngineerDataClass("${i + 1}", skillName)
            list += item
        }
        return list
    }

}