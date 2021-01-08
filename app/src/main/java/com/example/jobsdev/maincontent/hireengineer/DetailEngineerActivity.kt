package com.example.jobsdev.maincontent.hireengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityDetailEngineerBinding
import com.example.jobsdev.maincontent.skillengineer.ItemSkillEngineerModel
import com.example.jobsdev.maincontent.skillengineer.RecyclerViewSkillEngineerAdapter
import com.example.jobsdev.maincontent.skillengineer.UpdateSkillActivity
import com.example.jobsdev.maincontent.webview.GitHubWebViewActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.GetSkillByEnIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.ConstantDetailEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class DetailEngineerActivity : AppCompatActivity(), RecyclerViewSkillEngineerAdapter.OnSkillClickListener {

    private lateinit var binding : ActivityDetailEngineerBinding
    private lateinit var pagerAdapter: TabPagerDetailEngineerAdapter
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var coroutineScope : CoroutineScope
    var listSkill = ArrayList<ItemSkillEngineerModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_engineer)
        sharedPref = PreferencesHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        binding.tvFullName.text = intent.getStringExtra("name")
        binding.tvJobTitle.text = intent.getStringExtra("jobTitle")
        binding.tvJobType.text = intent.getStringExtra("jobType")
        binding.tvLocation.text = intent.getStringExtra("location")
        binding.tvDesc.text = intent.getStringExtra("description")
        binding.tvEmailProfile.text = intent.getStringExtra("email")
        binding.tvPhoneNumber.text = intent.getStringExtra("phoneNumber")

        val image = intent.getStringExtra("image")
        var img = "http://54.236.22.91:4000/image/$image"
        Log.d("image: ", img!!)

        Glide.with(binding.civProfilePict)
            .load(img)
            .placeholder(R.drawable.profile_pict_base)
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

        getListSkill()
        binding.rvSkillEngineer.adapter = RecyclerViewSkillEngineerAdapter(listSkill, this)
        binding.rvSkillEngineer.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        pagerAdapter = TabPagerDetailEngineerAdapter(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getListSkill() {
        val service = ApiClient.getApiClient(this)?.create(JobSDevApiService::class.java)

        coroutineScope.launch {
            Log.d("listSkill", "Start: ${Thread.currentThread().name}")

            val response = withContext(Dispatchers.IO) {
                Log.d("listSkill", "CallApi: ${Thread.currentThread().name}")

                try {
                    service?.getSkillByEnId(sharedPref.getValueString(ConstantDetailEngineer.engineerId)!!.toInt())
                } catch (e:Throwable) {
                    Log.e("errorM", e.message.toString())
                    e.printStackTrace()
                }
            }

            Log.d("listSkillResponse", response.toString())

            if(response is GetSkillByEnIdResponse) {
                Log.d("listSkillResponse", response.toString())

                val list = response.data.map {
                    ItemSkillEngineerModel(it?.skId, it?.enId, it?.skillName)
                }
                (binding.rvSkillEngineer.adapter as RecyclerViewSkillEngineerAdapter).addSkill(list)
            }
        }

    }

    private fun View.showOrGone(show: Boolean) {
        visibility = if(show) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onSkillItemClicked(position: Int) {
//        Toast.makeText(this, "Skill ${listSkill[position].skillName} clicked", Toast.LENGTH_SHORT).show()
    }

}