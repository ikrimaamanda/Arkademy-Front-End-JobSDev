package com.example.jobsdev.maincontent.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentListHireEngineerBinding
import com.example.jobsdev.maincontent.listhireengineer.*
import com.example.jobsdev.maincontent.projectcompany.ProjectCompanyModel
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class ListHireEngineerFragment : Fragment(), ListHireEngineerAdapter.OnListHireEngineerClickListener, ListHireEngineerView {

    private lateinit var binding : FragmentListHireEngineerBinding
    private var listHireEngineer = ArrayList<DetailHireEngineerModel>()
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var viewModel : ListHireEngineerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_hire_engineer, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        sharedPref = PreferencesHelper(requireContext())
        viewModel = ViewModelProvider(this).get(ListHireEngineerViewModel::class.java)
        val service = ApiClient.getApiClient(requireContext())?.create(HireEngineerApiService::class.java)

        viewModel.setSharedPrefHire(sharedPref)

        if (service != null) {
            viewModel.setListHireEngineerService(service)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewHireEngineer.adapter = ListHireEngineerAdapter(listHireEngineer, this)
        binding.recyclerViewHireEngineer.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    override fun addListHireEngineer(list: List<DetailHireEngineerModel>) {
        (binding.recyclerViewHireEngineer.adapter as ListHireEngineerAdapter).addListHireEngineer(list)
        binding.recyclerViewHireEngineer.showOrGone(true)
        binding.progressBar.showOrGone(false)
        binding.ivEmptyIllustration.showOrGone(false)
        binding.tvEmptyList.showOrGone(false)
    }

    override fun failedAdd(message: String) {
        binding.recyclerViewHireEngineer.showOrGone(false)
        if (message == "expired") {
            Toast.makeText(requireContext(), "Please sign in!", Toast.LENGTH_LONG).show()
        }
        binding.ivEmptyIllustration.showOrGone(true)
        binding.tvEmptyList.showOrGone(true)
        binding.progressBar.showOrGone(false)
    }

    override fun showProgressBar() {
        binding.recyclerViewHireEngineer.showOrGone(false)
        binding.progressBar.showOrGone(true)
        binding.ivEmptyIllustration.showOrGone(false)
        binding.tvEmptyList.showOrGone(false)
    }

    override fun hideProgressBar() {
        binding.progressBar.showOrGone(false)
    }

    override fun onStart() {
        super.onStart()
        viewModel.bindToView(this)
        viewModel.callListHireEngineerApi()
    }

    override fun onStop() {
        super.onStop()
        viewModel.unbind()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onHireEngineerItemClicked(position: Int) {
        Toast.makeText(requireContext(), "${listHireEngineer[position].hireId} clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), DetailHireEngineerActivity::class.java)
        intent.putExtra("hireId", listHireEngineer[position].hireId)
        intent.putExtra("projectName", listHireEngineer[position].projectName)
        intent.putExtra("companyName", listHireEngineer[position].companyName)
        intent.putExtra("location", listHireEngineer[position].companyCity)
        intent.putExtra("hirePrice", listHireEngineer[position].price)
        intent.putExtra("deadline", listHireEngineer[position].projectDeadline)
        intent.putExtra("message", listHireEngineer[position].message)
        intent.putExtra("description", listHireEngineer[position].projectDescription)
        intent.putExtra("projectImage", listHireEngineer[position].projectImage)
        intent.putExtra("status", listHireEngineer[position].status)

        startActivity(intent)
    }

    private fun View.showOrGone(show: Boolean) {
        visibility = if(show) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}

interface ListHireEngineerView {
    fun addListHireEngineer(list : List<DetailHireEngineerModel>)
    fun failedAdd(message : String)
    fun showProgressBar()
    fun hideProgressBar()
}