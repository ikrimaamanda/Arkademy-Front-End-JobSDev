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

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

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

        val image = sharedPref.getValueString(ConstantProjectCompany.projectImage)
        var imgLink = "http://54.236.22.91:4000/image/$image"

        Glide.with(binding.ivProjectImage)
            .load(imgLink)
            .placeholder(R.drawable.img_add_new_project)
            .error(R.drawable.img_add_new_project)
            .into(binding.ivProjectImage)

        binding.etProjectName.setText(sharedPref.getValueString(ConstantProjectCompany.projectName))
        val deadline = sharedPref.getValueString(ConstantProjectCompany.projectDeadline)!!.split("T")[0]
        binding.etDeadline.setText(deadline)
        binding.etDesc.setText(sharedPref.getValueString(ConstantProjectCompany.projectDesc))

        binding.ivProjectImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    pickImageFromGalery()
                }
            } else {
                pickImageFromGalery()
            }
        }

        binding.btnDelete.setOnClickListener {
            callDeleteProjectApi(projectId)
        }

    }

    private fun pickImageFromGalery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            binding.ivProjectImage.setImageURI(data?.data)

            val filePath = data?.data?.let { getPath(this, it) }
            val file = File(filePath)

            var img : MultipartBody.Part? = null
            val mediaTypeImg = "image/jpeg".toMediaType()
            val inputStream = data?.data?.let { contentResolver.openInputStream(it) }
            val reqFile : RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            img = reqFile?.let { it1 ->
                MultipartBody.Part.createFormData("image", file.name, it1)
            }

            binding.btnUpdate.setOnClickListener {
                if(binding.etProjectName.text.isNullOrEmpty() || binding.etDeadline.text.isNullOrEmpty() || binding.etDesc.text.isNullOrEmpty()) {
                    showMessage("Please filled all fields")
                    binding.etProjectName.requestFocus()
                }
                if (img != null) {
                    Glide.with(binding.ivProjectImage)
                        .load(img)
                        .placeholder(R.drawable.img_add_new_project)
                        .error(R.drawable.img_add_new_project)
                        .into(binding.ivProjectImage)
                    callUpdateProjectApi(sharedPref.getValueString(ConstantProjectCompany.projectId)!!.toInt(), img)
                }
            }

        }
    }

    private fun getPath(context : Context, contentUri : Uri) : String? {
        var result : String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)

        val cursorLoader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor = cursorLoader.loadInBackground()

        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }
        return result
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

    private fun callUpdateProjectApi(projectId : Int, image : MultipartBody.Part) {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    val projectName = binding.etProjectName.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDesc = binding.etDesc.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDeadline = binding.etDeadline.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    service.updateProjectById(projectId, projectName, projectDesc, projectDeadline, image)
                } catch (e : Throwable) {
                    Log.e("errorM", e.message.toString())
                    e.printStackTrace()
                }
            }

            Log.d("updateProject", response.toString())

            if (response is GeneralResponse) {
                Log.d("updateProject", response.toString())
                showMessage(response.message)
                moveActivity()
            } else {
                showMessage("Something wrong...")
            }
        }
    }
}