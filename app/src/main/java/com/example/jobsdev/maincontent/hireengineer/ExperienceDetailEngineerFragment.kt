package com.example.jobsdev.maincontent.hireengineer

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
import com.example.jobsdev.databinding.FragmentExperienceDetailEngineerBinding
import com.example.jobsdev.maincontent.experienceengineer.ItemExperienceModel
import com.example.jobsdev.maincontent.experienceengineer.RecyclerViewListExperienceAdapter
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class ExperienceDetailEngineerFragment : Fragment(), RecyclerViewListExperienceAdapter.OnListExperienceClickListener, ExperienceDetailEngineerContract.ViewExperienceDetailEngineer {
    private lateinit var binding : FragmentExperienceDetailEngineerBinding
    private val listExperience = ArrayList<ItemExperienceModel>()
    private lateinit var coroutineScope : CoroutineScope
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var service : JobSDevApiService
    private var presenter : ExperienceDetailEngineerContract.PresenterExperienceDetailEngineer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_experience_detail_engineer, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(requireContext())
        service = ApiClient.getApiClient(requireContext())!!.create(JobSDevApiService::class.java)
        presenter = ExperienceDetailEngineerPresenter(coroutineScope, service, sharedPref)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewExperience.adapter = RecyclerViewListExperienceAdapter(listExperience, this)
        binding.recyclerViewExperience.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

    }

    override fun onExperienceItemClicked(position: Int) {
        Toast.makeText(requireContext(), "${listExperience[position].position} clicked", Toast.LENGTH_SHORT).show()
    }

    override fun addListExperience(list: List<ItemExperienceModel>) {
        (binding.recyclerViewExperience.adapter as RecyclerViewListExperienceAdapter).addListExperience(list)
        binding.recyclerViewExperience.visibility = View.VISIBLE
        binding.ivEmptyIllustration.visibility= View.GONE
        binding.progressBar.visibility = View.GONE
    }

    override fun failedAdd(msg: String) {
        if (msg == "expired") {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_LONG).show()
        }
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
        binding.progressBar.visibility = View.GONE
    }

    override fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
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