package com.example.jobsdev.maincontent.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobsdev.R
import com.example.jobsdev.databinding.FragmentPortfolioEngineerBinding
import com.example.jobsdev.maincontent.dataclass.ItemPortfolioDataClass
import com.example.jobsdev.maincontent.recyclerview.RecyclerViewListPortfolioAdapter

class PortfolioEngineerFragment : Fragment() {
    private lateinit var binding : FragmentPortfolioEngineerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio_engineer, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exampleListPortfolio = generateDummyList(15)

        binding.recyclerView.apply {
            adapter =
                RecyclerViewListPortfolioAdapter(
                    exampleListPortfolio
                )
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun generateDummyList(size : Int) : List<ItemPortfolioDataClass> {
        val list = ArrayList<ItemPortfolioDataClass>()

        for (i in 0 until size) {
            val drawable = when(i%2) {
                0 -> R.drawable.porto1
                else -> R.drawable.porto2
            }

            val item = ItemPortfolioDataClass(drawable)
            list += item
        }
        return list
    }
}