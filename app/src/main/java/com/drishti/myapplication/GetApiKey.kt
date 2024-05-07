package com.drishti.myapplication


import com.google.gson.annotations.SerializedName

class GetApiKey : ArrayList<GetApiKeyItem>()
    data class GetApiKeyItem(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("email")
        val email: String? = null,
        @SerializedName("gender")
        val gender: String? = null,
        @SerializedName("status")
        val status: String? = null
    )
