package com.example.jobsdev.maincontent.projectcompany

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityAddNewProjectBinding

class AddNewProjectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddNewProjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_project)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnAdd.setOnClickListener {
            if(binding.etProjectName.text.isEmpty() || binding.etProjectDesc.text.isEmpty() || binding.etDeadline.text.isEmpty() || binding.etImageProject.text.isEmpty()) {

            }

            Toast.makeText(this, "Success add new project", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }
    }
}