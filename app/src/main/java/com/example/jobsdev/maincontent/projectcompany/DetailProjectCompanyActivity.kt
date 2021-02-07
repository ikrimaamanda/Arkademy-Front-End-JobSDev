package com.example.jobsdev.maincontent.projectcompany

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityDetailProjectCompanyBinding
import com.example.jobsdev.maincontent.hireengineer.HireApiService
import com.example.jobsdev.maincontent.projectcompany.listhirebyprojectid.*
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class DetailProjectCompanyActivity : AppCompatActivity(),
    ListHireByProjectIdAdapter.OnHireByProjectIdClickListener, DetailProjectCompanyContract.ViewDetailProjectCompany {

    private lateinit var binding : ActivityDetailProjectCompanyBinding
    private var listHireProject = ArrayList<HireByProjectIdModel>()
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var sharedPref : PreferencesHelper
    private var imgLink = "http://54.236.22.91:4000/image/"
    private lateinit var service : HireApiService
    private var presenter : DetailProjectCompanyPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_project_company)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(this)
        service = ApiClient.getApiClient(this)!!.create(HireApiService::class.java)
        presenter = DetailProjectCompanyPresenter(coroutineScope, service, sharedPref)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val image = intent.getStringExtra("image")
        Glide.with(binding.ivProjectImage)
            .load(imgLink + image)
            .placeholder(R.drawable.img_loading)
            .error(R.drawable.profile_pict_base)
            .into(binding.ivProjectImage)

        binding.fabUpdateImage.setOnClickListener {
            val intent = Intent(this, UpdateProjectImageActivity::class.java)
            intent.putExtra("projectImage", image)
            startActivity(intent)
        }

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

        val listHireProjectAdapter = ListHireByProjectIdAdapter(listHireProject, this)
        binding.rvHireByProjectId.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHireByProjectId.adapter = listHireProjectAdapter
    }

    override fun onHireByProjectIdItemClicked(position: Int) {
    }

    override fun addListHireByPjId(list: List<HireByProjectIdModel>) {
        (binding.rvHireByProjectId.adapter as ListHireByProjectIdAdapter).addListHireByProject(list)
        binding.rvHireByProjectId.visibility = View.VISIBLE
        binding.progressBarList.visibility = View.GONE
        binding.progressBarBtn.visibility = View.GONE
        binding.ivEmptyIllustration.visibility = View.GONE
        binding.btnEditProject.visibility = View.GONE
        binding.fabUpdateImage.visibility = View.GONE
    }

    override fun failedAdd(msg: String) {
        if (msg == "expired") {
            Toast.makeText(this, "Please sign in!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
        binding.progressBarList.visibility = View.GONE
        binding.btnEditProject.visibility = View.VISIBLE
        binding.fabUpdateImage.visibility = View.VISIBLE
    }

    override fun failedAdd() {
        binding.progressBarList.visibility = View.GONE
        binding.btnEditProject.visibility = View.VISIBLE
        binding.fabUpdateImage.visibility = View.VISIBLE
    }

    override fun showProgressBar() {
        binding.progressBarList.visibility = View.VISIBLE
        binding.progressBarBtn.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressBarList.visibility = View.GONE
        binding.progressBarBtn.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindView(this)
        presenter?.callListHireByPjIdApi()
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