package com.estimote.indoorapp

import android.app.Application
import com.estimote.indoorsdk.EstimoteCloudCredentials
import com.estimote.indoorsdk_module.cloud.Location

/**
 * Main app class
 */
class IndoorApplication : Application() {

/* Lista y credenciales */
    val locationsById: MutableMap<String, Location> = mutableMapOf()
    val cloudCredentials = EstimoteCloudCredentials("findit-esq", "eb80ed8051e67a9271b760494f7eeaa5")








}