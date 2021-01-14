package com.example.jobsdev.maincontent.editprofile


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityEditAccountCompanyBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.DetailCompanyByAcIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class EditAccountCompanyActivity : AppCompatActivity(), EditAccountCompanyContract.ViewEditAcCompany {

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var binding : ActivityEditAccountCompanyBinding
    private lateinit var service : JobSDevApiService
    private lateinit var coroutineScope: CoroutineScope
    private var presenter : EditAccountCompanyContract.PresenterEditAcCompany? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_account_company)
        sharedPref = PreferencesHelper(this)
        service = ApiClient.getApiClient(this)!!.create(JobSDevApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        presenter = EditAccountCompanyPresenter(coroutineScope, service, sharedPref)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

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
            } else {
                presenter?.callUpdateAccountApi(binding.etAcName.text.toString(), binding.etEditPhoneNumber.text.toString(), binding.etNewPassword.text.toString())
                presenter?.callUpdateCompanyApi(binding.etEditPosition.text.toString(), binding.etEditFields.text.toString(),
                    binding.etEditLocation.text.toString(), binding.etEditDescription.text.toString(), binding.etInstagram.text.toString(),
                    binding.etEditLinkedin.text.toString(), binding.etCompanyName.text.toString())
            }
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun moveActivity() {
        startActivity(Intent(this, MainContentActivity::class.java))
        finish()
    }

    override fun setData(data: DetailCompanyByAcIdResponse.Company) {
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
        presenter?.callCompanyIdApi()
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