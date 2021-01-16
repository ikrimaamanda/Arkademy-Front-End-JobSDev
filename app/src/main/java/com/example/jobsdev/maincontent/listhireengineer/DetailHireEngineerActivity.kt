package com.example.jobsdev.maincontent.listhireengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityDetailHireEngineerBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.maincontent.hireengineer.HireApiService
import com.example.jobsdev.remote.ApiClient
import kotlinx.coroutines.*

class DetailHireEngineerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailHireEngineerBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : HireApiService
    private var imgLink = "http://54.236.22.91:4000/image/"
    private lateinit var viewModel : DetailHireEngineerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_hire_engineer)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(HireApiService::class.java)

        viewModel = ViewModelProvider(this).get(DetailHireEngineerViewModel::class.java)
        viewModel.setUpdateStatusHireService(service)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.tvProjectName.text = intent.getStringExtra("projectName")
        binding.tvCompanyName.text = intent.getStringExtra("companyName")
        binding.tvCompanyLocation.text = intent.getStringExtra("location")
        binding.tvHirePrice.text = intent.getIntExtra("hirePrice", 0).toString()
        binding.tvDeadline.text = intent.getStringExtra("deadline")!!.split("T")[0]
        binding.tvMessage.text = intent.getStringExtra("message")
        binding.tvDesc.text = intent.getStringExtra("description")
        var status = intent.getStringExtra("status")
        if (status.isNullOrEmpty()) {
            status = "wait"
            binding.tvStatusHire.text = status
            binding.btnApprove.showOrGone(true)
            binding.btnReject.showOrGone(true)
        } else if (status.equals("approve")) {
            binding.tvStatusHire.text = status
            binding.tvStatusHire.setBackgroundResource(R.drawable.bg_orange)
            binding.btnApprove.showOrGone(false)
            binding.btnReject.showOrGone(false)
        } else if (status.equals("reject")) {
            binding.tvStatusHire.text = status
            binding.tvStatusHire.setBackgroundResource(R.drawable.red_button)
            binding.btnApprove.showOrGone(false)
            binding.btnReject.showOrGone(false)
        }

        val image = intent.getStringExtra("projectImage")
        Glide.with(binding.ivProjectImage)
            .load(imgLink + image)
            .placeholder(R.drawable.profile_pict_base)
            .error(R.drawable.profile_pict_base)
            .into(binding.ivProjectImage)

        val hireId = intent.getIntExtra("hireId",0)

        binding.btnApprove.setOnClickListener {
            viewModel.callUpdateHireStatusApi(hireId, "approve")
        }

        binding.btnReject.setOnClickListener {
            viewModel.callUpdateHireStatusApi(hireId, "reject")
        }

        subsribeLoadingLiveData()
        subscribeUpdateImageLiveData()
    }

    private fun subscribeUpdateImageLiveData() {
        viewModel.isUpdateStatusHireLivedata.observe(this, Observer {
            if (it) {
                viewModel.isMessage.observe(this, Observer {it1->
                    showMessage(it1)
                })
                moveActivity()
            } else {
                viewModel.isMessage.observe(this, Observer {it1->
                    if (it1 == "expired") {
                        showMessage("Please sign in again!")
                    } else {
                        showMessage(it1)
                    }
                })
            }
        })
    }

    private fun subsribeLoadingLiveData() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun View.showOrGone(show: Boolean) {
        visibility = if(show) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun moveActivity() {
            startActivity(Intent(this, MainContentActivity::class.java))
            finish()

    }
}