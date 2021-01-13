package com.example.jobsdev.maincontent.portfolioengineer

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
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityUpdatePortfolioTwoBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.maincontent.projectcompany.EditProjectActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.ConstantPortfolio
import com.example.jobsdev.sharedpreference.ConstantProjectCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UpdatePortfolioActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUpdatePortfolioTwoBinding
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : JobSDevApiService
    val typeApp = arrayOf("mobile app", "web app")

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_portfolio_two)
        sharedPref = PreferencesHelper(this)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(JobSDevApiService::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val portfolioId = intent.getIntExtra("updatePortfolioId", 0)

        val image = sharedPref.getValueString(ConstantPortfolio.portfolioImage)
        var imgLink = "http://54.236.22.91:4000/image/$image"

        Glide.with(binding.ivImgUpdatePortfolio)
            .load(imgLink)
            .placeholder(R.drawable.ic_img_add_portfolio)
            .error(R.drawable.ic_img_add_portfolio)
            .into(binding.ivImgUpdatePortfolio)

        binding.ivImgUpdatePortfolio.setOnClickListener {
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

        binding.etAppName.setText(intent.getStringExtra("appName"))
        binding.etDescriptionPortfolio.setText(intent.getStringExtra("portfolioDesc"))
        binding.etLinkPubPortfolio.setText(intent.getStringExtra("linkPub"))
        binding.etLinkRepoPortfolio.setText(intent.getStringExtra("linkRepo"))
        binding.etWorkplacePortfolio.setText(intent.getStringExtra("workPlace"))

        configSpinnerTypeApp()

        binding.btnDeletePortfolio.setOnClickListener {
            callDeletePortfolioApi(portfolioId)
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
            binding.ivImgUpdatePortfolio.setImageURI(data?.data)

            val filePath = data?.data?.let { getPath(this, it) }
            val file = File(filePath)

            var img : MultipartBody.Part? = null
            val mediaTypeImg = "image/jpeg".toMediaType()
            val inputStream = data?.data?.let { contentResolver.openInputStream(it) }
            val reqFile : RequestBody? = inputStream?.readBytes()?.toRequestBody(mediaTypeImg)

            img = reqFile?.let { it1 ->
                MultipartBody.Part.createFormData("image", file.name, it1)
            }

            binding.btnUpdatePortfolio.setOnClickListener {
                if(binding.etAppName.text.isNullOrEmpty() || binding.etDescriptionPortfolio.text.isNullOrEmpty() || binding.etLinkPubPortfolio.text.isNullOrEmpty() || binding.etLinkRepoPortfolio.text.isNullOrEmpty() || binding.etWorkplacePortfolio.text.isNullOrEmpty()) {
                    showMessage("Please filled all field")
                    binding.etAppName.requestFocus()
                } else {
                    if (img != null) {
                        callUpdatePortfolioApi(intent.getIntExtra("updatePortfolioId", 0), img)
                    }
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

    private fun callDeletePortfolioApi(portfolioId : Int) {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.deletePortfolioByPrId(portfolioId)
                } catch (e: Throwable) {
                    Log.e("errorKah?", e.message.toString())
                    e.printStackTrace()
                }
            }

            if(response is GeneralResponse) {
                if(response.success) {
                    showMessage(response.message)
                    moveActivity()
                }
            }
            showMessage("Something wrong...")
        }
    }

    private fun configSpinnerTypeApp() {
        binding.spinnerTypeApp.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, typeApp)
        binding.spinnerTypeApp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                showMessage("None")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                showMessage("${typeApp[position]} clicked")
                sharedPref.putValue(ConstantPortfolio.typeApp, typeApp[position])
            }

        }
    }

    private fun callUpdatePortfolioApi(portfolioId : Int, image : MultipartBody.Part) {
        coroutineScope.launch {
            val results = withContext(Dispatchers.IO){
                try {
                    val prAppName = binding.etAppName.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prDesc = binding.etDescriptionPortfolio.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prLinkPub = binding.etLinkPubPortfolio.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prLinkRepo = binding.etLinkRepoPortfolio.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prWorkplace = binding.etWorkplacePortfolio.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prType = sharedPref.getValueString(ConstantPortfolio.typeApp)!!
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    service.updatePortfolio(portfolioId, prAppName, prDesc, prLinkPub, prLinkRepo, prWorkplace, prType, image)

                } catch (e:Throwable) {
                    Log.e("errorYa", e.message.toString())
                    e.printStackTrace()
                }
            }
            Log.d("updatePortfolioReq", results.toString())

            if(results is GeneralResponse) {
                Log.d("updatePortfolioReq", results.toString())

                if(results.success) {
                    showMessage(results.message)
                    moveActivity()
                } else {
                    showMessage(results.message)
                }
            }
            showMessage("Something wrong...")
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