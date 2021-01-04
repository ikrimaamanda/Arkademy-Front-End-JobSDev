package com.example.jobsdev.maincontent.listhireengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityDetailHireEngineerBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.maincontent.hireengineer.HireApiService
import com.example.jobsdev.maincontent.hireengineer.HireResponse
import com.example.jobsdev.remote.ApiClient
import kotlinx.coroutines.*

class DetailHireEngineerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailHireEngineerBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : HireApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_hire_engineer)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(HireApiService::class.java)

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
        var img = "http://54.236.22.91:4000/image/$image"
        Glide.with(binding.ivProjectImage)
            .load(img)
            .placeholder(R.drawable.profile_pict_base)
            .error(R.drawable.profile_pict_base)
            .into(binding.ivProjectImage)

        val hireId = intent.getIntExtra("hireId",0)

        binding.btnApprove.setOnClickListener {
            updateHireStatus(hireId, "approve")
        }

        binding.btnReject.setOnClickListener {
            updateHireStatus(hireId, "reject")
        }
    }

    private fun updateHireStatus(hireId : Int, status : String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.updateHireStatus(hireId, status)
                } catch (e : Throwable) {
                    e.printStackTrace()
                }
            }
            if (result is HireResponse) {
                if (result.success) {
                    showMessage(result.message)
                    moveActivity()
                } else {
                    showMessage(result.message)
                    moveActivity()
                }
            } else {
                showMessage("Error")
            }
        }
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