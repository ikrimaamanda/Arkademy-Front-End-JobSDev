package com.example.jobsdev.maincontent.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
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
import com.example.jobsdev.maincontent.account.AccountCompanyContract
import com.example.jobsdev.maincontent.account.AccountCompanyPresenter
import com.example.jobsdev.maincontent.editprofile.EditAccountCompanyActivity
import com.example.jobsdev.maincontent.editprofile.UpdateProfilePictActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.DetailCompanyByAcIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class AccountCompanyFragment : Fragment(), AccountCompanyContract.ViewAcCompany {

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var binding : FragmentAccountCompanyBinding
    private lateinit var coroutineScope : CoroutineScope
    val imageLink = "http://54.236.22.91:4000/image/"
    private lateinit var service : JobSDevApiService
    private var presenter : AccountCompanyPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_company, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        sharedPref = PreferencesHelper(requireContext())
        service = ApiClient.getApiClient(requireContext())!!.create(JobSDevApiService::class.java)
        presenter = AccountCompanyPresenter(coroutineScope, service, sharedPref)

        binding.fabUpdateImage.setOnClickListener {
            startActivity(Intent(requireContext(), UpdateProfilePictActivity::class.java))
        }

        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(activity, EditAccountCompanyActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            showDialogLogOut()
        }

        return binding.root
    }

    private fun showMessage(message : String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showDialogLogOut() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Log Out")
        builder.setMessage("Do you want to log out?")
        builder.setPositiveButton("Yes", { dialogInterface : DialogInterface, i : Int -> sharedPref.putValue(Constant.prefIsLogin, false)
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
        showMessage("Log Out")
    }

    override fun setDataCompany(data: DetailCompanyByAcIdResponse.Company) {
        binding.model = data
        Glide.with(view!!.context)
            .load(imageLink + data.cnProfilePict)
            .placeholder(R.drawable.img_loading)
            .error(R.drawable.profile_pict_base)
            .into(binding.civProfilePict)
        binding.btnEditProfile.showOrGone(true)
        binding.fabUpdateImage.showOrGone(true)
        binding.progressBar.showOrGone(false)
    }

    override fun failedSetData(msg: String) {
        if (msg == "expired") {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
        }
        binding.btnEditProfile.showOrGone(true)
        binding.fabUpdateImage.showOrGone(true)
        binding.progressBar.showOrGone(false)
    }

    override fun showProgressBar() {
        binding.progressBar.showOrGone(true)
    }

    override fun hideProgressBar() {
        binding.progressBar.showOrGone(false)
    }

    private fun View.showOrGone(show: Boolean) {
        visibility = if(show) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindView(this)
        presenter?.callCompanyIdApi()
    }

    override fun onStop() {
        super.onStop()
        presenter?.unbindView()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        presenter = null
        super.onDestroy()
    }

}