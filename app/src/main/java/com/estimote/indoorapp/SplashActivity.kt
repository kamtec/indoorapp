package com.estimote.indoorapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.widget.Toast
import com.estimote.indoorsdk_module.cloud.CloudCallback
import com.estimote.indoorsdk_module.cloud.EstimoteCloudException
import com.estimote.indoorsdk_module.cloud.IndoorCloudManagerFactory
import com.estimote.indoorsdk_module.cloud.Location
import com.estimote.indoorsdk.EstimoteCloudCredentials


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

          val cloudCredentials = EstimoteCloudCredentials("findit-esq", "eb80ed8051e67a9271b760494f7eeaa5")
        val cloudManager = IndoorCloudManagerFactory().create(this, cloudCredentials)

        cloudManager.getAllLocations(object : CloudCallback<List<Location>> {
            override fun success(locations: List<Location>) {
                // Tomar ubicación y asignarlos a los identificadores
                val locationIds = locations.associateBy { it.identifier }
                // guarda las ubicaciones mapeadas
                (application as IndoorApplication).locationsById.putAll(locationIds)

                startMainActivity()
            }

            override fun failure(serverException: EstimoteCloudException) {

                Toast.makeText(this@SplashActivity, "No se pueden obtener datos de ubicación de la nube. " +
                        "Verifique su conexión a Internet y asegúrese de haber inicializado el mapa", Toast.LENGTH_LONG).show()
            }
        })
        // Hacer la barra de acciones invisible.
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        /* Crea un obj para comunicarse con la nube */

    }

    private fun startMainActivity() {
        startActivity(Intent(this, LocationListActivity::class.java))
    }

}
