package com.example.jobsdev.maincontent.projectcompany

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityEditProjectBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.sharedpreference.ConstantProjectCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class EditProjectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditProjectBinding
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var service : ProjectsCompanyApiService
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var viewModel : EditProjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_project)
        sharedPref = PreferencesHelper(this)
        service = ApiClient.getApiClient(context = this)!!.create(ProjectsCompanyApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        viewModel = ViewModelProvider(this).get(EditProjectViewModel::class.java)

        viewModel.setSharedPref(sharedPref)
        viewModel.setUpdateImageService(service)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.etProjectName.setText(sharedPref.getValueString(ConstantProjectCompany.projectName))
        val deadline = sharedPref.getValueString(ConstantProjectCompany.projectDeadline)!!.split("T")[0]
        binding.etDeadline.setText(deadline)
        binding.etDesc.setText(sharedPref.getValueString(ConstantProjectCompany.projectDesc))

        binding.btnUpdate.setOnClickListener {
            if(binding.etProjectName.text.isNullOrEmpty() || binding.etDeadline.text.isNullOrEmpty() || binding.etDesc.text.isNullOrEmpty()) {
                showMessage("Please filled all fields")
                binding.etProjectName.requestFocus()
            } else {
                viewModel.callUpdateProjectApi(binding.etProjectName.text.toString(), binding.etDesc.text.toString(), binding.etDeadline.text.toString())
            }
        }

        binding.btnDelete.setOnClickListener {
            viewModel.callDeleteProjectApi()
        }

        subsribeLoadingLiveData()
        subscribeUpdateImageLiveData()
        subscribeDeleteLiveData()
    }

    private fun subscribeDeleteLiveData() {
        viewModel.isDeleteLiveData.observe(this, Observer {
            if (it) {
                viewModel.isMessage.observe(this, Observer { it1->
                    showMessage(it1)
                })
                moveActivity()
            } else {
                viewModel.isMessage.observe(this, Observer { it1->
                    if (it1 == "expired") {
                        showMessage("Please sign in again!")
                    } else {
                        showMessage(it1)
                    }
                })
            }
        })
    }

    private fun subscribeUpdateImageLiveData() {
        viewModel.isUpdateLiveData.observe(this, Observer {
            if (it) {
                viewModel.isMessage.observe(this, Observer { it1->
                    showMessage(it1)
                })
                moveActivity()
            } else {
                viewModel.isMessage.observe(this, Observer { it1->
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

    private fun moveActivity() {
        startActivity(Intent(this, MainContentActivity::class.java))
        finish()
    }

}