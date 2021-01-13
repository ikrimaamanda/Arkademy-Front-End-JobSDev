package com.example.jobsdev.maincontent.editprofile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityEditAccountEngineerBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.DetailEngineerByAcIdResponse
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.*
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class EditAccountEngineerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditAccountEngineerBinding
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : JobSDevApiService
    val typeJob = arrayOf("freelance", "fulltime")
    val imageLink = "http://54.236.22.91:4000/image/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_account_engineer)
        service = ApiClient.getApiClient(this)!!.create(JobSDevApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val acId = sharedPref.getValueString(Constant.prefAccountId)

        getEngineerId(acId.toString())
        val oldPassword = sharedPref.getValueString(Constant.prefPassword)
        val newPassword = sharedPref.getValueString(Constant.prefPassword)
        binding.etOldPassword.setText(oldPassword)
        binding.etNewPassword.setText(newPassword)

        configSpinnerTypeJob()

        binding.btnSave.setOnClickListener {
            if (binding.etAcName.text.isNullOrEmpty() || binding.etEditPhoneNumber.text.isNullOrEmpty()
                || binding.etOldPassword.text.isNullOrEmpty() || binding.etNewPassword.text.isNullOrEmpty()
                || binding.etEditJobTitle.text.isNullOrEmpty() || binding.etEditLocation.text.isNullOrEmpty()
                || binding.etEditDescription.text.isNullOrEmpty()) {
                showMessage("Please filled all fields")
            }
                callUpdateAccount(sharedPref.getValueString(Constant.prefAccountId)!!.toInt(), binding.etAcName.text.toString(), binding.etEditPhoneNumber.text.toString(), binding.etNewPassword.text.toString())
                callUpdateEngineer()
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun configSpinnerTypeJob() {
        binding.spinnerJobType.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, typeJob)
        binding.spinnerJobType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                showMessage("None")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sharedPref.putValue(ConstantAccountEngineer.jobType, typeJob[position])
            }

        }
    }

    private fun getEngineerId(acId : String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getEngineerByAcId(acId)
                } catch (e:Throwable) {
                    Log.e("errorMessage : ", e.message.toString())
                    e.printStackTrace()
                }
            }

            if (result is DetailEngineerByAcIdResponse) {
                Log.d("engineerResponse", result.toString())
                binding.model = result.data
            }
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
            } else{
                showMessage("Something wrong...")
            }
        }
    }

    private fun callUpdateEngineer() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val enId = sharedPref.getValueString(ConstantAccountEngineer.engineerId)!!.toInt()
                    val jobTitle = binding.etEditJobTitle.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val jobType = sharedPref.getValueString(ConstantAccountEngineer.jobType)!!
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val location = binding.etEditLocation.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val description = binding.etEditDescription.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    service.updateEngineerById(enId, jobTitle, jobType, location, description)

                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            Log.d("editEng", result.toString())

            if(result is GeneralResponse) {
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

}