package com.example.jobsdev.maincontent.projectcompany

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
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
import java.text.SimpleDateFormat
import java.util.*

class EditProjectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditProjectBinding
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var service : ProjectsCompanyApiService
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var viewModel : EditProjectViewModel
    private lateinit var deadlineProject: DatePickerDialog.OnDateSetListener
    private lateinit var c: Calendar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_project)
        sharedPref = PreferencesHelper(this)
        service = ApiClient.getApiClient(context = this)!!.create(ProjectsCompanyApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        viewModel = ViewModelProvider(this).get(EditProjectViewModel::class.java)

        viewModel.setSharedPref(sharedPref)
        viewModel.setUpdateImageService(service)

        c = Calendar.getInstance()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.etProjectName.setText(sharedPref.getValueString(ConstantProjectCompany.projectName))
        val deadline = sharedPref.getValueString(ConstantProjectCompany.projectDeadline)!!.split("T")[0]
        binding.etDeadlineUpdateProject.setText(deadline)
        binding.etDesc.setText(sharedPref.getValueString(ConstantProjectCompany.projectDesc))

        binding.etDeadlineUpdateProject.setOnClickListener {
            DatePickerDialog(
                this, deadlineProject, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        deadlineProject()

        binding.btnUpdate.setOnClickListener {
            if(binding.etProjectName.text.isNullOrEmpty() || binding.etDeadlineUpdateProject.text.isNullOrEmpty() || binding.etDesc.text.isNullOrEmpty()) {
                showMessage("Please filled all fields")
                binding.etProjectName.requestFocus()
            } else {
                viewModel.callUpdateProjectApi(binding.etProjectName.text.toString(), binding.etDesc.text.toString(), binding.etDeadlineUpdateProject.text.toString())
            }
        }

        binding.btnDelete.setOnClickListener {
            showDialogDelete()
        }

        subsribeLoadingLiveData()
        subscribeUpdateImageLiveData()
        subscribeDeleteLiveData()
    }

    private fun showDialogDelete() {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Project")
            builder.setMessage("Are you sure to delete this project?")
            builder.setPositiveButton("Yes") { _: DialogInterface, _: Int -> viewModel.callDeleteProjectApi()
            }
        builder.setNegativeButton("No") { _: DialogInterface, _: Int ->}
        builder.show()
    }

    private fun deadlineProject() {
        deadlineProject = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val day = findViewById<TextView>(R.id.et_deadline_update_project)
            val formatDate = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(formatDate, Locale.US)

            day.text = sdf.format(c.time)
        }
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