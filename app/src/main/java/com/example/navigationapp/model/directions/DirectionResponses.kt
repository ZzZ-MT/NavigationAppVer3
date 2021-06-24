package com.example.navigationapp.model.directions

import com.google.gson.annotations.SerializedName

data class DirectionResponses(
    @SerializedName("geocoded_waypoint")
    var geocodedWayPoint: List<GeocodedWayPoint?>?,
    @SerializedName("routes")
    var routes: List<Route?>?,
    @SerializedName("status")
    var status: String?
)
