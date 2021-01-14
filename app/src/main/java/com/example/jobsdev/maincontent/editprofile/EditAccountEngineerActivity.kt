package com.example.jobsdev.maincontent.editprofile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.*
import kotlinx.coroutines.*

class EditAccountEngineerActivity : AppCompatActivity(), EditAccountEngineerContract.ViewEditAcEngineer {

    private lateinit var binding : ActivityEditAccountEngineerBinding
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : JobSDevApiService
    val typeJob = arrayOf("freelance", "fulltime")
    private var presenter : EditAccountEngineerPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_account_engineer)
        service = ApiClient.getApiClient(this)!!.create(JobSDevApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(this)
        presenter = EditAccountEngineerPresenter(coroutineScope, service, sharedPref)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

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
            } else {
                presenter?.callUpdateAccountApi(binding.etAcName.text.toString(), binding.etEditPhoneNumber.text.toString(), binding.etNewPassword.text.toString())
                presenter?.callUpdateEngineerApi(binding.etEditJobTitle.text.toString(), binding.etEditLocation.text.toString(), binding.etEditDescription.text.toString())
            }
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

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun moveActivity() {
        startActivity(Intent(this, MainContentActivity::class.java))
        finish()
    }

    override fun setData(data: DetailEngineerByAcIdResponse.Engineer) {
        binding.model = data
        binding.progressBar.visibility = View.GONE
    }

    override fun successUpdate(msg: String) {
        showMessage(msg)
        moveActivity()
    }

    override fun failed(msg: String) {
        if (msg == "expired") {
            Toast.makeText(this, "Please sign in!", Toast.LENGTH_LONG).show()
        } else {
            showMessage(msg)
        }
    }

    override fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindView(this)
        presenter?.callEngineerIdApi()
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