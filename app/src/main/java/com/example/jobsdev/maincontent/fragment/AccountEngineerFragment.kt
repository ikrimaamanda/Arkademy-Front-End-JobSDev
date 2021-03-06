package com.example.jobsdev.maincontent.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentAccountEngineerBinding
import com.example.jobsdev.login.LoginActivity
import com.example.jobsdev.maincontent.account.AccountEngineerContract
import com.example.jobsdev.maincontent.account.AccountEngineerPresenter
import com.example.jobsdev.maincontent.adapter.TabPagerAdapter
import com.example.jobsdev.maincontent.editprofile.EditAccountEngineerActivity
import com.example.jobsdev.maincontent.editprofile.UpdateProfilePictActivity
import com.example.jobsdev.maincontent.skillengineer.AddSkillActivity
import com.example.jobsdev.maincontent.skillengineer.ItemSkillEngineerModel
import com.example.jobsdev.maincontent.skillengineer.RecyclerViewSkillEngineerAdapter
import com.example.jobsdev.maincontent.skillengineer.UpdateSkillActivity
import com.example.jobsdev.maincontent.webview.GitHubWebViewActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.DetailEngineerByAcIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*

class AccountEngineerFragment : Fragment(), RecyclerViewSkillEngineerAdapter.OnSkillClickListener, AccountEngineerContract.ViewAcEngineer {

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var pagerAdapter: TabPagerAdapter
    private lateinit var viewPager : ViewPager
    private lateinit var tabLayout : TabLayout
    private lateinit var binding : FragmentAccountEngineerBinding
    private var listSkill = ArrayList<ItemSkillEngineerModel>()
    private lateinit var coroutineScope : CoroutineScope
    private val imageLink = "http://54.236.22.91:4000/image/"
    private var presenter : AccountEngineerPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_engineer, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = ApiClient.getApiClient(requireContext())?.create(JobSDevApiService::class.java)

        sharedPref = PreferencesHelper(requireContext())
        presenter = AccountEngineerPresenter(coroutineScope, service, sharedPref)

        binding.fabUpdateImage.setOnClickListener {
            startActivity(Intent(requireContext(), UpdateProfilePictActivity::class.java))
        }
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(activity, EditAccountEngineerActivity::class.java))
        }

        binding.btnAddSkill.setOnClickListener {
            startActivity(Intent(activity, AddSkillActivity::class.java))
        }

        pagerAdapter =
            TabPagerAdapter(
                childFragmentManager
            )
        addFragment(binding.root)

        binding.btnLogout.setOnClickListener {
            showDialogLogOut()
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.github_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.github_web_view) {
            startActivity(Intent(activity, GitHubWebViewActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSkillEngineer.adapter = RecyclerViewSkillEngineerAdapter(listSkill, this)
        binding.rvSkillEngineer.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

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
        showMessage("Log Out")
    }

    private fun showMessage(message : String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showDialogLogOut() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Log Out")
        builder.setMessage("Do you want to log out?")
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int -> sharedPref.putValue(Constant.prefIsLogin, false)
            moveActivity()
            sharedPref.clear()
            activity!!.finish()
        }
        builder.setNegativeButton("No") { _: DialogInterface, _: Int ->}
        builder.show()
    }

    private fun View.showOrGone(show: Boolean) {
        visibility = if(show) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onSkillItemClicked(position: Int) {
        val intent = Intent(requireContext(), UpdateSkillActivity::class.java)
        intent.putExtra("skillName", listSkill[position].skillName)
        intent.putExtra("skillId", listSkill[position].skillId)
        startActivity(intent)
    }

    override fun setDataEngineer(data: DetailEngineerByAcIdResponse.Engineer) {
        binding.model = data
        Glide.with(view!!.context)
            .load(imageLink + data.enProfilePict)
            .placeholder(R.drawable.img_loading)
            .error(R.drawable.profile_pict_base)
            .into(binding.civProfilePict)
        binding.progressBar.showOrGone(false)
        binding.btnEditProfile.showOrGone(true)
        binding.fabUpdateImage.showOrGone(true)
    }

    override fun addSkill(list: List<ItemSkillEngineerModel>) {
        (binding.rvSkillEngineer.adapter as RecyclerViewSkillEngineerAdapter).addSkill(list)
        binding.rvSkillEngineer.showOrGone(true)
        binding.progressBarSkill.showOrGone(false)
        binding.tvEmptyListSkill.showOrGone(false)
    }

    override fun failedAddSkill() {
        binding.rvSkillEngineer.showOrGone(false)
        binding.tvEmptyListSkill.showOrGone(true)
        binding.progressBarSkill.showOrGone(false)
    }

    override fun failedSetData(message: String) {
        if (message == "expired") {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            binding.progressBar.showOrGone(false)
        }
        binding.btnEditProfile.showOrGone(true)
        binding.fabUpdateImage.showOrGone(true)
    }

    override fun showProgressBar() {
        binding.progressBar.showOrGone(true)
        binding.progressBarSkill.showOrGone(true)
        binding.tvEmptyListSkill.showOrGone(false)
    }

    override fun hideProgressBar() {
        binding.progressBar.showOrGone(false)
        binding.progressBarSkill.showOrGone(false)
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindView(this)
        presenter?.callEngineerIdApi()
        presenter?.callSkillApi()
    }

    override fun onStop() {
        super.onStop()
        presenter?.unbind()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        presenter = null
        super.onDestroy()
    }

}