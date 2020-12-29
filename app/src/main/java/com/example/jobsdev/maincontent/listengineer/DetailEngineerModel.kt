package com.example.jobsdev.maincontent.listengineer

import com.example.jobsdev.maincontent.hireengineer.ItemSkillHireEngineerDataClass

data class DetailEngineerModel(val engineerId : String?,
                                val accountId : String?,
                                val accountName : String?,
                                val accountEmail : String?,
                                val accountPhoneNumber : String?,
                                val engineerJobTitle : String?,
                                val engineerJobType : String?,
                                val engineerLocation : String?,
                                val engineerDescription : String?,
                                val engineerProfilePict : String?,
                                val skillEngineer : List<ItemSkillHireEngineerDataClass?>)