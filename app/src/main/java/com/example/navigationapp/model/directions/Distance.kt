package com.example.navigationapp.model.directions

import com.google.gson.annotations.SerializedName

data class Distance(
    @SerializedName("text")
    var text:String?,
    @SerializedName("value")
    var value:Int?
)
