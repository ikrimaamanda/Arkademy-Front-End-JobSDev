package com.example.jobsdev.maincontent.editprofile

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
import com.example.jobsdev.databinding.ActivityEditAccountCompanyBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.DetailCompanyByAcIdResponse
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.ConstantAccountCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditAccountCompanyActivity : AppCompatActivity() {

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var binding : ActivityEditAccountCompanyBinding
    private lateinit var service : JobSDevApiService
    private lateinit var coroutineScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_account_company)
        sharedPref = PreferencesHelper(this)
        service = ApiClient.getApiClient(this)!!.create(JobSDevApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val acId = sharedPref.getValueString(Constant.prefAccountId)

        getCompanyId(acId.toString())
        val oldPassword = sharedPref.getValueString(Constant.prefPassword)
        val newPassword = sharedPref.getValueString(Constant.prefPassword)
        binding.etOldPassword.setText(oldPassword)
        binding.etNewPassword.setText(newPassword)

        binding.btnSave.setOnClickListener {
            if (binding.etCompanyName.text.isNullOrEmpty() || binding.etAcName.text.isNullOrEmpty() || binding.etEditPhoneNumber.text.isNullOrEmpty()
                || binding.etOldPassword.text.isNullOrEmpty() || binding.etNewPassword.text.isNullOrEmpty()
                || binding.etEditPosition.text.isNullOrEmpty() || binding.etEditFields.text.isNullOrEmpty()
                || binding.etEditLocation.text.isNullOrEmpty() || binding.etEditDescription.text.isNullOrEmpty()
                || binding.etInstagram.text.isNullOrEmpty() || binding.etEditLinkedin.text.isNullOrEmpty()) {
                showMessage("Please filled all fields")
            }
                callUpdateAccount(sharedPref.getValueString(Constant.prefAccountId)!!.toInt(), binding.etAcName.text.toString(), binding.etEditPhoneNumber.text.toString(), binding.etNewPassword.text.toString())
                callUpdateCompany()

        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun callUpdateAccount(acId : Int, acName : String, acPhoneNumber : String, acPassword : String) {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.updateAccountByAcId(acId, acName, acPhoneNumber, acPassword)
                } catch (e : Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is GeneralResponse) {
                showMessage(response.message)
            } else {
                showMessage("Something wrong...")
            }
        }
    }

    private fun callUpdateCompany() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val cnId = sharedPref.getValueString(ConstantAccountCompany.companyId)!!.toInt()
                    val position = binding.etEditPosition.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val fields = binding.etEditFields.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val location = binding.etEditLocation.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val description = binding.etEditDescription.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val instagram = binding.etInstagram.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val linkedin = binding.etEditLinkedin.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val companyName = binding.etCompanyName.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    service.updateCompany(cnId, position, fields, location, description, instagram, linkedin, companyName)

                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if(result is GeneralResponse) {
                showMessage(result.message)
                moveActivity()
            } else {
                showMessage("Something wrong...")
            }
        }
    }

    private fun getCompanyId(acId : String) {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getCompanyByAcId(acId)
                } catch (e: Throwable) {
                    Log.e("errorMessage : ", e.message.toString())
                    e.printStackTrace()
                }
            }

            if (response is DetailCompanyByAcIdResponse) {
                binding.model = response.data
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
}