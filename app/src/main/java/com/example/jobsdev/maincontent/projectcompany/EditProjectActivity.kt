package com.example.jobsdev.maincontent.projectcompany

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityEditProjectBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.sharedpreference.ConstantProjectCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProjectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditProjectBinding
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var service : ProjectsCompanyApiService
    private lateinit var coroutineScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_project)
        sharedPref = PreferencesHelper(this)
        service = ApiClient.getApiClient(context = this)!!.create(ProjectsCompanyApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val projectId = sharedPref.getValueString(ConstantProjectCompany.projectId)!!.toInt()

        binding.etProjectName.setText(sharedPref.getValueString(ConstantProjectCompany.projectName))
        val deadline = sharedPref.getValueString(ConstantProjectCompany.projectDeadline)!!.split("T")[0]
        binding.etDeadline.setText(deadline)
        binding.etDesc.setText(sharedPref.getValueString(ConstantProjectCompany.projectDesc))

        binding.btnUpdate.setOnClickListener {
            if(binding.etProjectName.text.isNullOrEmpty() || binding.etDeadline.text.isNullOrEmpty() || binding.etDesc.text.isNullOrEmpty()) {
                showMessage("Please filled all fields")
                binding.etProjectName.requestFocus()
            }
            callUpdateProjectApi(projectId)
        }

        binding.btnDelete.setOnClickListener {
            callDeleteProjectApi(projectId)
        }

    }

    private fun callDeleteProjectApi(projectId: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.deleteProjectById(projectId)
                } catch (e : Throwable) {
                    Log.e("error?", e.message.toString())
                    e.printStackTrace()
                }
            }

            if (result is GeneralResponse) {
                showMessage(result.message)
                moveActivity()
            } else {
                showMessage("Something wrong...")
            }
        }
    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun moveActivity() {
        startActivity(Intent(this, MainContentActivity::class.java))
        finish()
    }

    private fun callUpdateProjectApi(projectId : Int) {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    val projectName = binding.etProjectName.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDesc = binding.etDesc.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDeadline = binding.etDeadline.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    service.updateProjectById(projectId, projectName, projectDesc, projectDeadline)
                } catch (e : Throwable) {
                    e.printStackTrace()
                }
            }


            if (response is GeneralResponse) {
                showMessage(response.message)
                moveActivity()
            } else {
                showMessage("Something wrong...")
            }
        }
    }
}