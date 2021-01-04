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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentAccountEngineerBinding
import com.example.jobsdev.login.LoginActivity
import com.example.jobsdev.maincontent.adapter.TabPagerAdapter
import com.example.jobsdev.maincontent.editprofile.EditAccountEngineerActivity
import com.example.jobsdev.maincontent.skillengineer.AddSkillActivity
import com.example.jobsdev.maincontent.skillengineer.ItemSkillEngineerModel
import com.example.jobsdev.maincontent.skillengineer.RecyclerViewSkillEngineerAdapter
import com.example.jobsdev.maincontent.skillengineer.UpdateSkillActivity
import com.example.jobsdev.maincontent.webview.GitHubWebViewActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.DetailEngineerByAcIdResponse
import com.example.jobsdev.retfrofit.GetSkillByEnIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*

class AccountEngineerFragment : Fragment(), RecyclerViewSkillEngineerAdapter.OnSkillClickListener {

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var pagerAdapter: TabPagerAdapter
    private lateinit var viewPager : ViewPager
    private lateinit var tabLayout : TabLayout
    private lateinit var binding : FragmentAccountEngineerBinding
    var listSkill = ArrayList<ItemSkillEngineerModel>()
    private lateinit var coroutineScope : CoroutineScope
    val imageLink = "http://54.236.22.91:4000/image/"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_engineer, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        sharedPref = PreferencesHelper(requireContext())
        val acId = sharedPref.getValueString(Constant.prefAccountId)
        getEngineerId(acId!!)
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(activity, EditAccountEngineerActivity::class.java))
        }

        getListSkill()

        binding.btnLogout.setOnClickListener {
            showDialogLogOut()
            showMessage("Log Out")
        }
        binding.tvGithub.setOnClickListener {
            startActivity(Intent(activity, GitHubWebViewActivity::class.java))
        }

        binding.btnAddSkill.setOnClickListener {
            startActivity(Intent(activity, AddSkillActivity::class.java))
        }

        pagerAdapter =
            TabPagerAdapter(
                childFragmentManager
            )
        addFragment(binding.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSkillEngineer.adapter = RecyclerViewSkillEngineerAdapter(listSkill, this)
        binding.rvSkillEngineer.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

    }

    private fun getEngineerId(acId : String) {
        val service = ApiClient.getApiClient(requireContext())?.create(JobSDevApiService::class.java)
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getEngineerByAcId(acId)
                } catch (e:Throwable) {
                    Log.e("errorMessage : ", e.message.toString())
                    e.printStackTrace()
                }
            }

            if (result is DetailEngineerByAcIdResponse) {
                Log.d("engineerResponse", result.toString())
                binding.model = result.data
                Glide.with(view!!.context)
                    .load(imageLink + result.data.enProfilePict)
                    .placeholder(R.drawable.profile_pict_base)
                    .into(binding.civProfilePict)
            }
        }
    }

    private fun addFragment(view: View?) {
        viewPager = view!!.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)

        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)

    }

    private fun moveActivity() {
        val  intent = Intent(activity, LoginActivity::class.java)
        activity!!.startActivity(intent)
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

    private fun getListSkill() {
        val service = ApiClient.getApiClient(requireContext())?.create(JobSDevApiService::class.java)

        coroutineScope.launch {
            Log.d("listSkill", "Start: ${Thread.currentThread().name}")

            val response = withContext(Dispatchers.IO) {
                Log.d("listSkill", "CallApi: ${Thread.currentThread().name}")

                try {
                    service?.getSkillByEnId(sharedPref.getValueString(ConstantAccountEngineer.engineerId)!!.toInt())
                } catch (e:Throwable) {
                    Log.e("errorM", e.message.toString())
                    e.printStackTrace()
                }
            }

            Log.d("listSkillResponse", response.toString())

            if(response is GetSkillByEnIdResponse) {
                Log.d("listSkillResponse", response.toString())

                val list = response.data.map {
                    ItemSkillEngineerModel(it?.skId, it?.enId, it?.skillName)
                }
                (binding.rvSkillEngineer.adapter as RecyclerViewSkillEngineerAdapter).addSkill(list)
            }
        }

    }

    override fun onSkillItemClicked(position: Int) {
        Toast.makeText(requireContext(), "Skill $position clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), UpdateSkillActivity::class.java)
        intent.putExtra("skillName", listSkill[position].SkillName)
        startActivity(intent)
    }

}