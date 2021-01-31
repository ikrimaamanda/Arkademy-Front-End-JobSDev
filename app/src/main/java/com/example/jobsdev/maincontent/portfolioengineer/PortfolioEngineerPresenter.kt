package com.example.jobsdev.maincontent.portfolioengineer

import com.example.jobsdev.retfrofit.GetPortfolioByEnIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PortfolioEngineerPresenter(private val coroutineScope: CoroutineScope,
                                 private val service : JobSDevApiService,
                                 private val sharedPref : PreferencesHelper
) : PortfolioContract.PresenterPortfolio {

    private var view : PortfolioContract.ViewPortfolio? = null

    override fun bindView(view: PortfolioContract.ViewPortfolio) {
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
                    service.getListPortfolioByEnId(sharedPref.getValueString(
                        ConstantAccountEngineer.engineerId)!!.toInt())
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideProgressBar()

                        when {
                            e.code() == 400 -> {
                                view?.failedAdd("expired")
                            }
                            else -> {
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
                    view?.addListPortfolio(list)
                } else {
                    view?.failedAdd(response.message)
                }
            }
        }

    }


}