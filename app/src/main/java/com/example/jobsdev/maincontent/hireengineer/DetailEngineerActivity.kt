package com.example.jobsdev.maincontent.hireengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityDetailEngineerBinding
import com.example.jobsdev.maincontent.skillengineer.ItemSkillEngineerModel
import com.example.jobsdev.maincontent.skillengineer.RecyclerViewSkillEngineerAdapter
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class DetailEngineerActivity : AppCompatActivity(), RecyclerViewSkillEngineerAdapter.OnSkillClickListener, DetailEngineerContract.ViewDetailEngineer {

    private lateinit var binding : ActivityDetailEngineerBinding
    private lateinit var pagerAdapter: TabPagerDetailEngineerAdapter
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var service : JobSDevApiService
    private var listSkill = ArrayList<ItemSkillEngineerModel>()
    private var imgLink = "http://54.236.22.91:4000/image/"
    private var presenter : DetailEngineerPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_engineer)
        sharedPref = PreferencesHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(JobSDevApiService::class.java)
        presenter = DetailEngineerPresenter(coroutineScope, service, sharedPref)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.tvFullName.text = intent.getStringExtra("name")
        binding.tvJobTitle.text = intent.getStringExtra("jobTitle")
        binding.tvJobType.text = intent.getStringExtra("jobType")
        binding.tvLocation.text = intent.getStringExtra("location")
        binding.tvDesc.text = intent.getStringExtra("description")
        binding.tvEmailProfile.text = intent.getStringExtra("email")
        binding.tvPhoneNumber.text = intent.getStringExtra("phoneNumber")

        val image = intent.getStringExtra("image")

        Glide.with(binding.civProfilePict)
            .load(imgLink + image)
            .placeholder(R.drawable.img_loading)
            .error(R.drawable.profile_pict_base)
            .into(binding.civProfilePict)


        if(sharedPref.getValueInt(Constant.prefLevel) == 0) {
            binding.btnHireEngineer.showOrGone(false)
        } else {
            binding.btnHireEngineer.showOrGone(true)
            binding.btnHireEngineer.setOnClickListener {
                startActivity(Intent(this, FormHireActivity::class.java))
            }
        }

        binding.rvSkillEngineer.adapter = RecyclerViewSkillEngineerAdapter(listSkill, this)
        binding.rvSkillEngineer.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        pagerAdapter = TabPagerDetailEngineerAdapter(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }

    private fun View.showOrGone(show: Boolean) {
        visibility = if(show) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onSkillItemClicked(position: Int) {
    }

    override fun addListSkill(list: List<ItemSkillEngineerModel>) {
        (binding.rvSkillEngineer.adapter as RecyclerViewSkillEngineerAdapter).addSkill(list)
        binding.rvSkillEngineer.showOrGone(true)
        binding.progressBarSkill.showOrGone(false)
        binding.tvEmptyListSkill.showOrGone(false)
    }

    override fun failedAddSkill() {
        binding.rvSkillEngineer.showOrGone(false)
        binding.tvEmptyListSkill.showOrGone(true)
        binding.progressBarSkill.showOrGone(false)
    }

    override fun showProgressBarSkill() {
        binding.progressBarSkill.showOrGone(true)
        binding.tvEmptyListSkill.showOrGone(false)
    }

    override fun hideProgressBarSkill() {
        binding.progressBarSkill.showOrGone(false)
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindView(this)
        presenter?.callListSkillApi()
    }

    override fun onStop() {
        super.onStop()
        presenter?.unbind()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        presenter = null
        super.onDestroy()
    }

}