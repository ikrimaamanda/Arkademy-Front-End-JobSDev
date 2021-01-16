package com.example.jobsdev.maincontent.hireengineer

import com.example.jobsdev.maincontent.skillengineer.ItemSkillEngineerModel
import com.example.jobsdev.retfrofit.GetSkillByEnIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantDetailEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DetailEngineerPresenter(private val coroutineScope: CoroutineScope,
                              private val service : JobSDevApiService,
                              private val sharedPref : PreferencesHelper
) : DetailEngineerContract.PresenterDetailEngineer {

    private var view : DetailEngineerContract.ViewDetailEngineer? = null

    override fun bindView(view: DetailEngineerContract.ViewDetailEngineer) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callListSkillApi() {
        coroutineScope.launch {
            view?.showProgressBarSkill()

            val response = withContext(Dispatchers.IO) {

                try {
                    service.getSkillByEnId(sharedPref.getValueString(ConstantDetailEngineer.engineerId)!!.toInt())
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideProgressBarSkill()

                        when {
                            e.code() == 404 -> {
                                view?.failedAddSkill("Data not found!")
                            }
                            e.code() == 400 -> {
                                view?.failedAddSkill("expired")
                            }
                            else -> {
                                view?.failedAddSkill("Server under maintenance!")
                            }
                        }
                    }
                }
            }

            if(response is GetSkillByEnIdResponse) {
                if (response.success) {
                    val list = response.data.map {
                        ItemSkillEngineerModel(it?.skId, it?.enId, it?.skillName)
                    }
                    view?.addListSkill(list)
                }
            }
        }

    }


}