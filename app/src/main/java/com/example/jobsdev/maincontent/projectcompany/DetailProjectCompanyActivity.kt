package com.example.jobsdev.maincontent.projectcompany

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityDetailProjectCompanyBinding
import com.example.jobsdev.maincontent.hireengineer.HireApiService
import com.example.jobsdev.maincontent.projectcompany.listhirebyprojectid.HireByProjectIdModel
import com.example.jobsdev.maincontent.projectcompany.listhirebyprojectid.HireByProjectResponse
import com.example.jobsdev.maincontent.projectcompany.listhirebyprojectid.ListHireByProjectIdAdapter
import com.example.jobsdev.remote.ApiClient
import kotlinx.coroutines.*

class DetailProjectCompanyActivity : AppCompatActivity(),
    ListHireByProjectIdAdapter.OnHireByProjectIdClickListener {

    private lateinit var binding : ActivityDetailProjectCompanyBinding
    var listHireProject = ArrayList<HireByProjectIdModel>()
    private lateinit var coroutineScope : CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_project_company)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

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
                    service?.getListHireByProjectId()
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