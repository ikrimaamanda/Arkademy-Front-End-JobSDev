package com.example.jobsdev.maincontent.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentAccountCompanyBinding
import com.example.jobsdev.login.LoginActivity
import com.example.jobsdev.maincontent.editprofile.EditAccountCompanyActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.DetailCompanyByAcIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.ConstantAccountCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class AccountCompanyFragment : Fragment() {

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var binding : FragmentAccountCompanyBinding
    private lateinit var coroutineScope : CoroutineScope
    val imageLink = "http://54.236.22.91:4000/image/"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_company, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        sharedPref = PreferencesHelper(requireContext())
        val acId = sharedPref.getValueString(Constant.prefAccountId)
        getCompanyId(acId!!)

        binding.tvField.text = sharedPref.getValueString(ConstantAccountCompany.fields)
        binding.tvLocation.text = sharedPref.getValueString(Constant.prefLocation)

        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(activity, EditAccountCompanyActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            showDialogLogOut()
        }

        return binding.root
    }

    private fun getCompanyId(acId : String) {
        val service = ApiClient.getApiClient(requireContext())?.create(JobSDevApiService::class.java)
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
                Glide.with(view!!.context)
                    .load(imageLink + response.data.cnProfilePict)
                    .placeholder(R.drawable.profile_pict_base)
                    .into(binding.civProfilePict)
                Log.d("cnIdReqByCom", response.toString())
            }
        }
    }

    private fun showMessage(message : String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showDialogLogOut() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Log Out")
        builder.setMessage("Do you want to log out?")
        builder.setPositiveButton("Yes", { dialogInterface : DialogInterface, i : Int -> sharedPref.putValue(Constant.prefIsLogin, false)
            showMessage("Log Out")
            moveActivity()
            sharedPref.clear()
            activity!!.finish()
        })
        builder.setNegativeButton("No", {dialogInterface : DialogInterface, i : Int ->})
        builder.show()
    }

    private fun moveActivity() {
        val  intent = Intent(activity, LoginActivity::class.java)
        activity!!.startActivity(intent)
    }

}