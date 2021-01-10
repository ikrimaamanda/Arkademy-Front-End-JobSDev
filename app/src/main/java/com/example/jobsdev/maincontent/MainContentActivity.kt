package com.example.jobsdev.maincontent

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityMainContentBinding
import com.example.jobsdev.login.LoginActivity
import com.example.jobsdev.maincontent.editprofile.EditAccountEngineerActivity
import com.example.jobsdev.maincontent.fragment.*
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.DetailCompanyByAcIdResponse
import com.example.jobsdev.retfrofit.DetailEngineerByAcIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.ConstantAccountCompany
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class MainContentActivity : AppCompatActivity() {

    private lateinit var sharedPref : PreferencesHelper

    private lateinit var binding : ActivityMainContentBinding
    private lateinit var homeFragment : HomeFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var listProjectCompanyFragment : ListProjectCompanyFragment
    private lateinit var listHireEngineerFragment : ListHireEngineerFragment
    private lateinit var accountEngineerFragment: AccountEngineerFragment
    private lateinit var accountCompanyFragment : AccountCompanyFragment
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var service : JobSDevApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_content)
        sharedPref = PreferencesHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(JobSDevApiService::class.java)

        homeFragment = HomeFragment()
        searchFragment = SearchFragment()
        listHireEngineerFragment = ListHireEngineerFragment()
        listProjectCompanyFragment = ListProjectCompanyFragment()
        accountEngineerFragment = AccountEngineerFragment()
        accountCompanyFragment = AccountCompanyFragment()

        val acId = sharedPref.getValueString(Constant.prefAccountId)
//        getEngineerId(acId.toString())
//        getCompanyId(acId.toString())

        homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fl_container, homeFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()


//        if(sharedPref.getValueString(ConstantAccountEngineer.jobType) != null) {
//            homeFragment = HomeFragment()
//            supportFragmentManager.beginTransaction().replace(R.id.fl_container, homeFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
//        } else {
//            showDialog()
//        }

        binding.bottomNavMenu.setOnNavigationItemSelectedListener{
                item -> when(item.itemId) {
            R.id.home -> {
                homeFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fl_container, homeFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                binding.tvTitleToolbar.setText("Home")
//                if(sharedPref.getValueString(ConstantAccountEngineer.jobType) == null) {
//                    showDialog()
//                }
            }
            R.id.search -> {
                searchFragment = SearchFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fl_container, searchFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                binding.tvTitleToolbar.setText("Search")
//                if(sharedPref.getValueString(ConstantAccountEngineer.jobType) == null) {
//                    showDialog()
//                }
            }
            R.id.list -> {
                if(sharedPref.getValueInt(Constant.prefLevel) == 0) {
                    listHireEngineerFragment = ListHireEngineerFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, listHireEngineerFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                    binding.tvTitleToolbar.setText("List Hire")
                } else if(sharedPref.getValueInt(Constant.prefLevel) == 1) {
                    listProjectCompanyFragment = ListProjectCompanyFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, listProjectCompanyFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                    binding.tvTitleToolbar.setText("List Project")
                }
            }
            R.id.account -> {
                accountEngineerFragment = AccountEngineerFragment()
                accountCompanyFragment = AccountCompanyFragment()

                if(sharedPref.getValueInt(Constant.prefLevel) == 0) {
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, accountEngineerFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                } else if(sharedPref.getValueInt(Constant.prefLevel) == 1) {
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, accountCompanyFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                }
                binding.tvTitleToolbar.setText("Profile")
            }
        }
            true
        }

    }

    private fun getEngineerId(acId : String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getEngineerByAcId(acId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is DetailEngineerByAcIdResponse) {
                if (result.success) {
                    if (result.data.enJobType != null && result.data.enJobTitle != null && result.data.enLocation != null && result.data.enDescription != null) {
                        sharedPref.putValue(ConstantAccountEngineer.jobType, result.data.enJobType)
                        sharedPref.putValue(ConstantAccountEngineer.jobTitle, result.data.enJobTitle)
                        sharedPref.putValue(Constant.prefLocation, result.data.enLocation)
                        sharedPref.putValue(Constant.prefDescription, result.data.enDescription)
                    } else {
                        Toast.makeText(this@MainContentActivity, "Please complete your profile", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun getCompanyId(acId : String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getCompanyByAcId(acId)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is DetailCompanyByAcIdResponse) {
                if (result.success) {
                    if (result.data.fields != null) {
                        sharedPref.putValue(ConstantAccountCompany.fields, result.data.fields)
                    } else {
                        Toast.makeText(this@MainContentActivity, "Please complete your profile", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Please complete profile")
        builder.setMessage("You must complete your profile before enjoy this app!")
        builder.setPositiveButton("Ok", { dialogInterface : DialogInterface, i : Int -> moveActivity()
            finish()
        })
        builder.show()
    }

    private fun moveActivity() {
        val  intent = Intent(this, EditAccountEngineerActivity::class.java)
        startActivity(intent)
    }

}