package com.example.jobsdev.maincontent.projectcompany

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityDetailProjectCompanyBinding
import com.example.jobsdev.maincontent.hireengineer.HireApiService
import com.example.jobsdev.maincontent.projectcompany.listhirebyprojectid.HireByProjectIdModel
import com.example.jobsdev.maincontent.projectcompany.listhirebyprojectid.HireByProjectResponse
import com.example.jobsdev.maincontent.projectcompany.listhirebyprojectid.ListHireByProjectIdAdapter
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.ConstantProjectCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class DetailProjectCompanyActivity : AppCompatActivity(),
    ListHireByProjectIdAdapter.OnHireByProjectIdClickListener {

    private lateinit var binding : ActivityDetailProjectCompanyBinding
    var listHireProject = ArrayList<HireByProjectIdModel>()
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var sharedPref : PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_project_company)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val image = intent.getStringExtra("image")
        var img = "http://54.236.22.91:4000/image/$image"

        Glide.with(binding.ivProjectImage)
            .load(img)
            .placeholder(R.drawable.profile_pict_base)
            .error(R.drawable.profile_pict_base)
            .into(binding.ivProjectImage)

        binding.tvProjectName.text = intent.getStringExtra("projectName")
        val deadline = intent.getStringExtra("deadline")
        binding.tvDeadline.text = deadline!!.split("T")[0]
        val createdAt = intent.getStringExtra("createdAt")
        binding.tvCreatedAt.text = createdAt!!.split("T")[0]
        val updatedAt = intent.getStringExtra("updatedAt")
        binding.tvUpdatedAt.text = updatedAt!!.split("T")[0]
        binding.tvDesc.text = intent.getStringExtra("description")

        binding.btnEditProject.setOnClickListener {
            val intent = Intent(this, EditProjectActivity::class.java)
            startActivity(intent)
        }

        getList()

        var listHireProjectAdapter = ListHireByProjectIdAdapter(listHireProject, this)
        binding.rvHireByProjectId.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHireByProjectId.adapter = listHireProjectAdapter
    }

    private fun getList() {
        val service = ApiClient.getApiClient(this)?.create(HireApiService::class.java)

        coroutineScope.launch {
            Log.d("listHireProject", "Start: ${Thread.currentThread().name}")

            val response = withContext(Dispatchers.IO) {
                Log.d("listHireProject", "CallApi: ${Thread.currentThread().name}")

                try {
                    service?.getListHireByProjectId(sharedPref.getValueString(ConstantProjectCompany.projectId))
                } catch (e:Throwable) {
                    Log.e("errorM", e.message.toString())
                    e.printStackTrace()
                }
            }

            Log.d("listHireProjectResponse", response.toString())

            if(response is HireByProjectResponse) {
                Log.d("listHireProjectResponse", response.toString())

                val list = response.data.map {
                    HireByProjectIdModel(it.hrId, it.cnId, it.enId, it.enJobTitle, it.enJobType, it.enProfilePict, it.projectId, it.projectName,
                    it.projectDesc, it.projectDeadline, it.projectImage, it.hirePrice, it.hireMessage, it.hireStatus, it.hireDateConfirm, it.hireCreatedAt, it.projectCreateAt, it.projectUpdateAt, it.name, it.email, it.pHoneNumber)
                }
                (binding.rvHireByProjectId.adapter as ListHireByProjectIdAdapter).addListHireByProject(list)
            }
        }

    }

    override fun onHireByProjectIdItemClicked(position: Int) {
        Toast.makeText(this, "${listHireProject[position].acName} clicked", Toast.LENGTH_SHORT).show()
    }
}