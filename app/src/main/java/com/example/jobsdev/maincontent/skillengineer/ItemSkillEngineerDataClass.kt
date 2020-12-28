package com.example.jobsdev.maincontent.skillengineer

import com.google.gson.annotations.SerializedName

data class ItemSkillEngineerDataClass(val skillId : String, @SerializedName("en_id") val engineerId : Int, val SkillName : String)