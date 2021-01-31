package com.example.jobsdev.maincontent.account

import com.example.jobsdev.maincontent.skillengineer.ItemSkillEngineerModel
import com.example.jobsdev.retfrofit.DetailEngineerByAcIdResponse

interface AccountEngineerContract {

    interface ViewAcEngineer{
        fun setDataEngineer(data : DetailEngineerByAcIdResponse.Engineer)
        fun addSkill(list : List<ItemSkillEngineerModel>)
        fun failedAddSkill()
        fun failedSetData(message : String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface PresenterAcEngineer {
        fun bindView(view : ViewAcEngineer)
        fun unbind()
        fun callEngineerIdApi()
        fun callSkillApi()
    }
}