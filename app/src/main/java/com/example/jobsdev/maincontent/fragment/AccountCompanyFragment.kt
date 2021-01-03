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
import com.example.jobsdev.maincontent.webview.GitHubWebViewActivity
import com.example.jobsdev.onboard.OnBoardLoginActivity
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.ConstantAccountCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper

class AccountCompanyFragment : Fragment() {

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var binding : FragmentAccountCompanyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_company, container, false)

        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(activity, EditAccountCompanyActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            showDialogLogOut()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = PreferencesHelper(view.context)

        val image = sharedPref.getValueString(Constant.prefProfilePict)
        var img = "http://54.236.22.91:4000/image/$image"

        Glide.with(binding.civProfilePict)
            .load(img)
            .placeholder(R.drawable.profile_pict_base)
            .error(R.drawable.profile_pict_base)
            .into(binding.civProfilePict)

        binding.tvCompanyName.text = sharedPref.getValueString(ConstantAccountCompany.companyName)
        binding.tvField.text = sharedPref.getValueString(ConstantAccountCompany.fields)
        binding.tvPosition.text = sharedPref.getValueString(ConstantAccountCompany.position)
        binding.tvLocation.text = sharedPref.getValueString(Constant.prefLocation)
        binding.tvDesc.text = sharedPref.getValueString(Constant.prefDescription)
        binding.tvEmailProfile.text = sharedPref.getValueString(Constant.prefEmail)
        binding.tvInstagram.text = sharedPref.getValueString(ConstantAccountCompany.instagram)
        binding.tvPhoneNumber.text = sharedPref.getValueString(Constant.prefPhoneNumber)
        binding.tvLinkedin.text = sharedPref.getValueString(ConstantAccountCompany.linkedin)

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