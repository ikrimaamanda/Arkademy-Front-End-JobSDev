package com.example.jobsdev.maincontent.projectcompany

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityAddNewProjectBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.sharedpreference.ConstantAccountCompany
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.ConstantPortfolio
import com.example.jobsdev.sharedpreference.PreferencesHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class AddNewProjectActivity : AppCompatActivity(), UploadRequestBody.UploadCallBack {

    private lateinit var binding : ActivityAddNewProjectBinding
    private lateinit var service : ProjectsCompanyApiService
    private lateinit var coroutineScope : CoroutineScope
    private var selectedImage : Uri? = null
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var imageName: MultipartBody.Part

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_project)
        service = ApiClient.getApiClient(context = this)!!.create(ProjectsCompanyApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.ivAddProjectImage.setOnClickListener {
        }

        binding.btnAdd.setOnClickListener {
            if(binding.etProjectName.text.isEmpty() || binding.etProjectDesc.text.isEmpty() || binding.etDeadline.text.isEmpty()) {
                Toast.makeText(this, "Please filled all field", Toast.LENGTH_SHORT).show()
                binding.etProjectName.requestFocus()
            } else {
                callAddProjectApi()
            }
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun callAddProjectApi() {
        coroutineScope.launch {
            val results = withContext(Dispatchers.IO){
                try {
                    val cnId = sharedPref.getValueString(ConstantAccountCompany.companyId)!!.toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectName = binding.etProjectName.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDesc = binding.etProjectDesc.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDeadline = binding.etDeadline.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    service.addNewProject(projectName, projectDesc, projectDeadline, cnId)
                } catch (e:Throwable) {
                    Log.e("error?", e.message.toString())
                    e.printStackTrace()
                }
            }
            Log.d("addPortfolioReq", results.toString())

            if(results is AddProjectResponse) {
                Log.d("addPortfolioReq", results.toString())

                if(results.success) {
                    showMessage(results.message)
                    moveActivity()
                } else {
                    showMessage(results.message)
                }
            }
            showMessage("Something wrong...")
        }
    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun moveActivity() {
        val intent = Intent(this, MainContentActivity::class.java)
        startActivity(intent)
    }

    private fun View.snackbar(message : String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).also {
            snackbar ->  snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
        }.show()
    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressBar.progress = percentage
    }
}