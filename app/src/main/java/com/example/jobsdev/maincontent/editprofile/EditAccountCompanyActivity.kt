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
    val imageLink = "http://54.236.22.91:4000/image/"

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

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

        val image = sharedPref.getValueString(Constant.prefProfilePict)

        Glide.with(binding.civEditProfilePict)
            .load(imageLink+image)
            .placeholder(R.drawable.profile_pict_base)
            .error(R.drawable.profile_pict_base)
            .into(binding.civEditProfilePict)

        binding.civEditProfilePict.setOnClickListener {
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

        binding.btnCancel.setOnClickListener {
            onBackPressed()
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
            binding.civEditProfilePict.setImageURI(data?.data)

            val filePath = data?.data?.let { getPath(this, it) }
            val file = File(filePath)

            var img : MultipartBody.Part?
            val mediaTypeImg = "image/jpeg".toMediaType()
            val inputStream = data?.data?.let { contentResolver.openInputStream(it) }
            val reqFile : RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            img = reqFile?.let { it1 ->
                MultipartBody.Part.createFormData("image", file.name, it1)
            }

            binding.btnSave.setOnClickListener {
                if (binding.etCompanyName.text.isNullOrEmpty() || binding.etAcName.text.isNullOrEmpty() || binding.etEditPhoneNumber.text.isNullOrEmpty()
                    || binding.etOldPassword.text.isNullOrEmpty() || binding.etNewPassword.text.isNullOrEmpty()
                    || binding.etEditPosition.text.isNullOrEmpty() || binding.etEditFields.text.isNullOrEmpty()
                    || binding.etEditLocation.text.isNullOrEmpty() || binding.etEditDescription.text.isNullOrEmpty()
                    || binding.etInstagram.text.isNullOrEmpty() || binding.etEditLinkedin.text.isNullOrEmpty()) {
                    showMessage("Please filled all fields")
                }
                if (img != null) {
                    Glide.with(binding.civEditProfilePict)
                        .load(img)
                        .placeholder(R.drawable.profile_pict_base)
                        .error(R.drawable.profile_pict_base)
                        .into(binding.civEditProfilePict)
                    callUpdateAccount(sharedPref.getValueString(Constant.prefAccountId)!!.toInt(), binding.etAcName.text.toString(), binding.etEditPhoneNumber.text.toString(), binding.etNewPassword.text.toString())
                    callUpdateCompany(img)
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

    private fun callUpdateCompany(image : MultipartBody.Part) {
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

                    service.updateCompany(cnId, position, fields, location, description, instagram, linkedin, image, companyName)

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
                Glide.with(this@EditAccountCompanyActivity)
                    .load(imageLink + response.data.cnProfilePict)
                    .placeholder(R.drawable.profile_pict_base)
                    .into(binding.civEditProfilePict)
                Log.d("cnIdReqByCom", response.toString())
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