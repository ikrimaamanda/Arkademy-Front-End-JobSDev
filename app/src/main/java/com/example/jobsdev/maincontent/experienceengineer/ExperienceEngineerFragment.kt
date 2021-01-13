package com.example.jobsdev.maincontent.experienceengineer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentExperienceEngineerBinding
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class ExperienceEngineerFragment : Fragment(), RecyclerViewListExperienceAdapter.OnListExperienceClickListener, ExperienceEngineerContract.ViewExperienceEngineer {

    private lateinit var binding : FragmentExperienceEngineerBinding
    private val listExperience = ArrayList<ItemExperienceModel>()
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var service : JobSDevApiService
    private var presenter : ExperienceEngineerContract.PresenterExperienceEngineer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_experience_engineer, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(requireContext())
        service = ApiClient.getApiClient(requireContext())!!.create(JobSDevApiService::class.java)
        presenter = ExperienceEngineerPresenter(coroutineScope, service, sharedPref)

        binding.btnAddExperience.setOnClickListener {
            startActivity(Intent(activity, AddExperienceActivity::class.java))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewExperience.adapter = RecyclerViewListExperienceAdapter(listExperience, this)
        binding.recyclerViewExperience.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    override fun onExperienceItemClicked(position: Int) {
        Toast.makeText(requireContext(), "${listExperience[position].exId} clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), UpdateExperienceActivity::class.java)
        intent.putExtra("exId", listExperience[position].exId)
        intent.putExtra("exPosition", listExperience[position].position)
        intent.putExtra("exCompany", listExperience[position].company)
        intent.putExtra("exStartDate", listExperience[position].startDate)
        intent.putExtra("exEndDate", listExperience[position].endDate)
        intent.putExtra("exDesc", listExperience[position].description)

        startActivity(intent)
    }

    private fun View.showOrGone(show : Boolean) {
        if (show) {
            visibility = View.VISIBLE
        } else {
            visibility = View.GONE
        }
    }

    override fun addListExperience(list: List<ItemExperienceModel>) {
        (binding.recyclerViewExperience.adapter as RecyclerViewListExperienceAdapter).addListExperience(list)
        binding.recyclerViewExperience.showOrGone(true)
        binding.ivEmptyIllustration.showOrGone(false)
        binding.progressBar.showOrGone(false)
    }

    override fun failedAdd(msg: String) {
        if (msg == "expired") {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_LONG).show()
        }
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
        binding.progressBar.showOrGone(false)
    }

    override fun showProgressBar() {
        binding.progressBar.showOrGone(true)
    }

    override fun hideProgressBar() {
        binding.progressBar.showOrGone(false)
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindView(this)
        presenter?.callListExperienceApi()
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