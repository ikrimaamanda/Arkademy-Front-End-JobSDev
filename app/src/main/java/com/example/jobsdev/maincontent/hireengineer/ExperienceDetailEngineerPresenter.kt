package com.example.jobsdev.maincontent.hireengineer

import com.example.jobsdev.maincontent.experienceengineer.ItemExperienceModel
import com.example.jobsdev.retfrofit.GetExperienceByEnIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantDetailEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ExperienceDetailEngineerPresenter(private val coroutineScope: CoroutineScope,
                                        private val service : JobSDevApiService,
                                        private val sharedPref : PreferencesHelper
) : ExperienceDetailEngineerContract.PresenterExperienceDetailEngineer {

    private var view : ExperienceDetailEngineerContract.ViewExperienceDetailEngineer? = null

    override fun bindView(view: ExperienceDetailEngineerContract.ViewExperienceDetailEngineer) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callListExperienceApi() {
        coroutineScope.launch {
            view?.showProgressBar()

            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getListExperienceByEnId(sharedPref.getValueString(ConstantDetailEngineer.engineerId)!!.toInt())
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

            if(response is GetExperienceByEnIdResponse) {
                if (response.success) {
                    val list = response.data?.map {
                        ItemExperienceModel(it.enId, it.exId, it.exPosition, it.exCompany, it.exStartDate, it.exEndDate, it.exDesc)
                    }
                    view?.addListExperience(list)
                } else {
                    view?.failedAdd(response.message)
                }
            } else {
                view?.failedAdd("Hello, your list experience is empty!")
            }
        }

    }


}