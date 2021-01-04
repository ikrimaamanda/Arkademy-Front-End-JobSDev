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
import com.example.jobsdev.sharedpreference.ConstantAccountCompany
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

    companion object{
//        private const val REQUEST_CODE_IMAGE_PICKER = 100
        const val FIELD_REQUIRED = "Field tidak boleh kosong"
        const val PROJECT_ADD_AUTH_KEY = "project_add_auth_key"
        const val REQUEST_CODE = 100
    }

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
            chooseImage()
//            openImageChooser()
        }

        binding.btnAdd.setOnClickListener {
            if(binding.etProjectName.text.isEmpty() || binding.etProjectDesc.text.isEmpty() || binding.etDeadline.text.isEmpty()) {
                Toast.makeText(this, "Please filled all field", Toast.LENGTH_SHORT).show()
                binding.etProjectName.requestFocus()
            } else {

                // disiniiii
                val cnId = sharedPref.getValueString(ConstantAccountCompany.companyId)
                addProject(binding.etProjectName.text.toString(), binding.etProjectDesc.text.toString(), binding.etDeadline.text.toString())
//                addPlease()
            }
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun addProject(projectName : String, projectDesc : String, projectDeadline : String) {
        if (selectedImage == null) {
            binding.addProjectLayout.snackbar("Select an image first")
            return
        }

        Log.d("select", selectedImage!!.path.toString())

        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImage!!, "r", null) ?: return

        val file = File(cacheDir, contentResolver.getFileName(selectedImage!!))
        Log.d("fileImage", file.path.toString())
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        Log.d("out", outputStream.toString())
        inputStream.copyTo(outputStream)

        binding.progressBar.progress = 0
        val body = UploadRequestBody(file, "image", this)

        coroutineScope.launch {
            Log.d("upload", "Start: ${Thread.currentThread().name}")

            val response = withContext(Dispatchers.IO) {
                Log.d("upload", "CallApi: ${Thread.currentThread().name}")

                try {
                    val imageProject = MultipartBody.Part.createFormData("image", file.name, body)
                    val name = projectName.toRequestBody("text/plain".toMediaTypeOrNull())
                    val desc = projectDesc.toRequestBody("text/plain".toMediaTypeOrNull())
                    val deadline = projectDeadline.toRequestBody("text/plain".toMediaTypeOrNull())
                    val cpID = sharedPref.getValueString(ConstantAccountCompany.companyId).toString()
                    val compID = cpID.toRequestBody("text/plain".toMediaTypeOrNull())

                    service.addNewProject(imageProject, name, desc, deadline, compID)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }
            Log.d("addProjectReq : ", response.toString())

            if(response is AddProjectResponse) {
                if (response.success) {
                    Log.d("addProjectReq : ", response.toString())

                    binding.progressBar.progress = 100
                    binding.addProjectLayout.snackbar(response.message)
//                Toast.makeText(this, "Success add new project", Toast.LENGTH_SHORT).show()
                    moveActivity()
                }

            } else {
                binding.addProjectLayout.snackbar("Somethin wrong..")
            }
        }
    }

    private fun moveActivity() {
        val intent = Intent(this, MainContentActivity::class.java)
        startActivity(intent)
    }

//    private fun openImageChooser() {
//        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
//            it.type = "image/*"
//            val mimeTypes = arrayOf("image/jpeg", "image/png")
//            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//            startActivityForResult(it, REQUEST_CODE_IMAGE_PICKER)
//        }
//    }

    private fun chooseImage() {
        if (EasyPermissions.hasPermissions(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        } else {
            EasyPermissions.requestPermissions(this,"This application need your permission to access image gallery.",991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                REQUEST_CODE -> {
                    selectedImage = data?.data
                    binding.ivAddProjectImage.setImageURI(selectedImage)
                    Log.d("Eimage", selectedImage!!.path.toString())
                }
            }
        }
//        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
//            val dataResponse = data?.data?.path?.replace("/raw/".toRegex(), "")
//            val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), File(dataResponse!!))
//
//            Log.d("Eimage", data.toString())
//
//            imageName = MultipartBody.Part.createFormData("image", File(dataResponse).name, requestBody)
//            Glide.with(this).load(dataResponse).into(binding.ivAddProjectImage)
//        }
    }

    private fun View.snackbar(message : String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).also {
            snackbar ->  snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
        }.show()
    }

    private fun ContentResolver.getFileName(uri : Uri) : String? {
        var name = uri.path
        val cursor = query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
//            name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            name = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA).toString()
        }
        return name
    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressBar.progress = percentage
    }
}