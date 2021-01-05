package com.estimote.indoorapp

import androidx.annotation.Keep


@Keep
data class LocationPin(val pins: List<LocationPin> = emptyList(),
                       var x: Double = 0.0,
                       var y: Double = 0.0,
                       var orientation: Double = 0.0,
                       var name: String = "",
                       var type: String = "") {


}
