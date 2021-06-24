package com.example.navigationapp.model.directions

import com.google.gson.annotations.SerializedName

data class GeocodedWayPoint(
    @SerializedName("geocoder_status")
    var geocoderStatus:String?,
    @SerializedName("place_id")
    var placeId:String?,
    @SerializedName("types")
    var types:List<String?>?
)
