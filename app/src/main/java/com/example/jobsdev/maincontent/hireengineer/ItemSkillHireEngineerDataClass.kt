package com.example.jobsdev.maincontent.hireengineer

import com.google.gson.annotations.SerializedName

data class ItemSkillHireEngineerDataClass(val skillId : String, @SerializedName("en_id") val engineerId : Int, val SkillName : String)