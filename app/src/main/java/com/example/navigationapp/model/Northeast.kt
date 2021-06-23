package com.example.navigationapp.model

import com.google.gson.annotations.SerializedName

data class Northeast(
    @SerializedName("lat")
    var lat:String?,
    @SerializedName("lng")
    var lng:String?
)
