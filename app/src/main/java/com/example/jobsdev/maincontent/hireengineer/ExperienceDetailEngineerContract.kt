package com.example.jobsdev.maincontent.hireengineer

import com.example.jobsdev.maincontent.experienceengineer.ItemExperienceModel

interface ExperienceDetailEngineerContract {
    interface ViewExperienceDetailEngineer {
        fun addListExperience(list : List<ItemExperienceModel>)
        fun failedAdd(msg : String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface PresenterExperienceDetailEngineer {
        fun bindView(view : ViewExperienceDetailEngineer)
        fun unbind()
        fun callListExperienceApi()
    }
}