package com.example.jobsdev.maincontent.skillengineer

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityUpdateSkillBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class UpdateSkillActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUpdateSkillBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : JobSDevApiService
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var viewModel : UpdateSkillViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_skill)
        sharedPref = PreferencesHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(JobSDevApiService::class.java)
        viewModel = ViewModelProvider(this).get(UpdateSkillViewModel::class.java)

        viewModel.setService(service)
        viewModel.setSharedPref(sharedPref)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val name = intent.getStringExtra("skillName")
        binding.tvCurrentSkillName.text = name
        val skillId = intent.getIntExtra("skillId",0)

        binding.btnUpdateSkill.setOnClickListener {
            if(binding.etUpdateSkillName.text.isNullOrEmpty()) {
                showMessage("Please input skill name")
                binding.etUpdateSkillName.requestFocus()
            } else {
                viewModel.callUpdateSkillApi(skillId, binding.etUpdateSkillName.text.toString())
            }
        }

        binding.btnDelete.setOnClickListener {
            showDialogDelete(skillId)
        }

        subscribeLoadingLiveData()
        subscribeUpdateSkillLiveData()
        subscribeDeleteSkillLiveData()
    }

    private fun showDialogDelete(skillId : Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Skill")
        builder.setMessage("Are you sure to delete this skill?")
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int -> viewModel.callDeleteSkillApi(skillId)
        }
        builder.setNegativeButton("No") { _: DialogInterface, _: Int ->}
        builder.show()
    }

    private fun subscribeLoadingLiveData() {
        viewModel.isLoadingLiveData.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun subscribeDeleteSkillLiveData() {
        viewModel.isDeleteSkillLiveData.observe(this, Observer {
            if (it) {
                viewModel.isMessageLiveData.observe(this, Observer { it1->
                    showMessage(it1)
                })
                moveActivity()
            } else {
                viewModel.isMessageLiveData.observe(this, Observer { it1->
                    if (it1 == "expired") {
                        showMessage("Please sign in again!")
                    } else {
                        showMessage(it1)
                    }
                })
            }
        })
    }

    private fun subscribeUpdateSkillLiveData() {
        viewModel.isUpdateSkillLiveData.observe(this, Observer {
            if (it) {
                viewModel.isMessageLiveData.observe(this, Observer { it1->
                    showMessage(it1)
                })
                moveActivity()
            } else {
                viewModel.isMessageLiveData.observe(this, Observer { it1->
                    if (it1 == "expired") {
                        showMessage("Please sign in again!")
                    } else {
                        showMessage(it1)
                    }
                })
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