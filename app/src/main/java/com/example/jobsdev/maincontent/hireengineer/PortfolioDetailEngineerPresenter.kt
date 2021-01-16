package com.example.jobsdev.maincontent.hireengineer

import android.util.Log
import com.example.jobsdev.maincontent.portfolioengineer.ItemPortfolioModel
import com.example.jobsdev.retfrofit.GetPortfolioByEnIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantDetailEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PortfolioDetailEngineerPresenter (private val coroutineScope: CoroutineScope,
                                        private val service : JobSDevApiService,
                                        private val sharedPref : PreferencesHelper
) : PortfolioDetailEngineerContract.PresenterPortfolioDetailEngineer {

    private var view : PortfolioDetailEngineerContract.ViewPortfolioDetailEngineer? = null

    override fun bindView(view: PortfolioDetailEngineerContract.ViewPortfolioDetailEngineer) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callListPortfolioApi() {
        coroutineScope.launch {
            view?.showProgressBar()
            val response = withContext(Dispatchers.IO) {

                try {
                    service.getListPortfolioByEnId(sharedPref.getValueString(ConstantDetailEngineer.engineerId)!!.toInt())
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideProgressBar()

                        when {
                            e.code() == 404 -> {
                                view?.failedAdd("Data not found!")
                            }
                            e.code() == 400 -> {
                                view?.failedAdd("expired")
                            }
                            else -> {
                                view?.failedAdd("Server under maintenance!")
                            }
                        }
                    }
                }
            }

            if(response is GetPortfolioByEnIdResponse) {
                if (response.success) {
                    val list = response.data.map {
                        ItemPortfolioModel(it.enId, it.portfolioId, it.portfolioprAppName, it.portfolioDesc, it.portfolioLinkPub, it.portfolioLinkRepo, it.portfolioWorkPlace, it.portfolioType, it.portfolioImage)
                    }
                    view?.addListPortfolioDetailEngineer(list)
                }
            }
        }

    }


}