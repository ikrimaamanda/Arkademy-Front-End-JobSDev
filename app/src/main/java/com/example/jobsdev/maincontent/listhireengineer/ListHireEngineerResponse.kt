package com.example.jobsdev.maincontent.listhireengineer

import com.google.gson.annotations.SerializedName


data class ListHireEngineerResponse(val success : Boolean, val message : String, val data : List<Hire>) {
    data class Hire(@SerializedName("hr_id") val hireId : Int?,
                    @SerializedName("cn_id") val companyId : Int?,
                    @SerializedName("cn_company") val companyName : String?,
                    @SerializedName("cn_position") val position : String?,
                    @SerializedName("cn_fields") val companyFields : String?,
                    @SerializedName("cn_city") val companyCity : String?,
                    @SerializedName("cn_description") val companyDescription : String?,
                    @SerializedName("cn_instagram") val companyInstagram : String?,
                    @SerializedName("cn_linkedin") val companyLinkedIn : String?,
                    @SerializedName("en_id") val engineerId : Int?,
                    @SerializedName("pj_id") val projectId : Int?,
                    @SerializedName("pj_project_name") val projectName : String?,
                    @SerializedName("pj_description") val projectDescription : String?,
                    @SerializedName("pj_deadline") val projectDeadline : String?,
                    @SerializedName("pj_image")val projectImage : String?,
                    @SerializedName("hr_price") val price : Int?,
                    @SerializedName("hr_message") val message : String?,
                    @SerializedName("hr_status") val status : String?,
                    @SerializedName("hr_date_confirm") val dateConfirm : String?)
}