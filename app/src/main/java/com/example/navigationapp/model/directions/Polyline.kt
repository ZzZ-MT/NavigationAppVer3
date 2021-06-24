package com.example.navigationapp.model.directions

import com.google.gson.annotations.SerializedName

data class Polyline(
    @SerializedName("points")
    var points:String?
)
