package com.estimote.indoorapp


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat.Builder
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import com.estimote.indoorsdk.EstimoteCloudCredentials
import com.estimote.indoorsdk.IndoorLocationManagerBuilder
import com.estimote.indoorsdk_module.algorithm.OnPositionUpdateListener
import com.estimote.indoorsdk_module.algorithm.ScanningIndoorLocationManager
import com.estimote.indoorsdk_module.cloud.Location
import com.estimote.indoorsdk_module.cloud.LocationPosition
import com.estimote.indoorsdk_module.view.IndoorLocationView
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream
import java.util.*


@Suppress("DEPRECATION")
/**
 * Main view para la ubicación en el interior
 */

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    val TAG_LOGS = "estimote"
    private lateinit var indoorLocationView: IndoorLocationView
    private lateinit var indoorLocationManager: ScanningIndoorLocationManager
    private lateinit var location: Location
    private lateinit var locationPin: LocationPin
    private lateinit var notification: Notification
    private lateinit var locationPosition: LocationPosition
    private var mCustomPoint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mLocationPinPath = Path()
    private val mDrawParams = DrawParams()
    private val canvas = Canvas()
    private val mMatrix: Matrix = Matrix()
    private val mPositionPinBitmap = createPositionPinBitmap()

    companion object {
        val intentKeyLocationId = "location_id"
        fun createIntent(context: Context, locationId: String): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(intentKeyLocationId, locationId)
            return intent

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val locationId = intent.extras.getString(intentKeyLocationId)
        location = (application as IndoorApplication).locationsById[locationId]!!

        // Notificación en la barra del usuario.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val NOTIFICATION_CHANNEL_ID = "com.estimote.indoorapp"
            val channelName = "My Background Service"
            val chan = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, IMPORTANCE_HIGH)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(chan)

            val notificationBuilder = Builder(this, NOTIFICATION_CHANNEL_ID)
            notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.logo_app)
                    .setContentTitle("Find IT \u00AE")
                    .setContentText("Estas en " + location.name + " conócela por dentro")
                    .setPriority(PRIORITY_DEFAULT)
                    .build()

        } else {
            notification = Notification.Builder(this)
                    .setSmallIcon(R.drawable.logo_app)
                    .setContentTitle("Find IT \u00AE")
                    .setContentText("Estas en " + location.name + " conócela por dentro")
                    .setPriority(PRIORITY_DEFAULT)
                    .build()

        }


        //  ID y ubicaciones
        setupLocation()

        // Iniciar
        indoorLocationView = findViewById(R.id.indoor_view) as IndoorLocationView
        // Dar el obj de ubicación a la vista para dibujarlo en la pantalla
        indoorLocationView.setLocation(location)

        locationPin = LocationPin()
        singleLocation()
        drawocationPins(canvas)


        /* */
/*Se crea el obj y se determina la ubicacion xy. Estamos usando .withScannerInForegroundService (notificación) para el trabajo en segundo plano*/
        val cloudCredentials = EstimoteCloudCredentials("findit-esq", "eb80ed8051e67a9271b760494f7eeaa5")
        indoorLocationManager = IndoorLocationManagerBuilder(this, location, cloudCredentials)
                .withScannerInForegroundService(notification)
                .build()

        // Enganchar al Listener para eventos de actualización de posición
        indoorLocationManager.setOnPositionUpdateListener(object : OnPositionUpdateListener {
            override fun onPositionOutsideLocation() {
                indoorLocationView.hidePosition()

            }


            override fun onPositionUpdate(locationPosition: LocationPosition) {
                indoorLocationView.updatePosition(locationPosition)

            }

        })


        /*locationPosition = LocationPosition()

        tts = TextToSpeech(this, this)
        if( locationPosition.x == locationPin.x && locationPosition.y == locationPin.y) {
            val arrayofString2 = arrayOf("Estas  parado en " + locationPin.name)
            for (i in arrayofString2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts!!.speak(i, TextToSpeech.QUEUE_FLUSH, null, null)
                } else {
                    val hash = HashMap<String, String>()
                    hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                            AudioManager.STREAM_NOTIFICATION.toString())
                    tts!!.speak(i, TextToSpeech.QUEUE_FLUSH, hash)

                }

            }
        }*/

        // Compruebe si el Bluetooth está habilitado, se otorgan permisos de ubicación, etc.
        RequirementsWizardFactory.createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        onRequirementsFulfilled = {
                            indoorLocationManager.startPositioning()

                        },
                        onRequirementsMissing = {
                            Toast.makeText(applicationContext, "No se puede buscar Sensores Find IT. Requisitos faltantes: ${it.joinToString()}", Toast.LENGTH_SHORT)
                                    .show()
                        },
                        onError = {
                            Toast.makeText(applicationContext, "No se puede buscar sensores Find It. Error: ${it.message}", Toast.LENGTH_SHORT)
                                    .show()
                        })

    }

    private fun setupLocation() {
// obtener el id de la ubicación para mostrar desde el intent
        val locationId = intent.extras.getString(intentKeyLocationId)
// obtener el obj de la ubicación Si algo salió mal, construimos una ubicación vacía sin datos.
        location = (application as IndoorApplication).locationsById[locationId]
                ?: buildEmptyLocation()
// Establecer el título
        title = location.name
    }

    private fun buildEmptyLocation(): Location {
        return Location("", "", true, "", 0.0, emptyList(), emptyList(), emptyList())

    }

    /* Implementar Lectura*/
    private var tts: TextToSpeech? = null
    private var hablar: Button? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.getDefault())
            if (result != TextToSpeech.LANG_MISSING_DATA ||
                    result != TextToSpeech.LANG_NOT_SUPPORTED) {
                hablar!!.isEnabled = true
            }
        }
    }

    public override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        indoorLocationManager.stopPositioning()
        super.onDestroy()
    }

    fun singleLocation() {
        val locationId = intent.extras.getString(intentKeyLocationId)
        val userDes: FindItLocations = CloudServiceBuilder.buildService(FindItLocations::class.java)
        val solicitudUser: Call<LocationPin> = userDes.getSingleLocation(locationId)

        solicitudUser.enqueue(object : Callback<LocationPin> {
            override fun onResponse(call: Call<LocationPin>, response: Response<LocationPin>) =
                    if (response.isSuccessful) {

                        val locationPins: List<LocationPin> = response.body()!!.pins

                        Log.i(TAG_LOGS, Gson().toJson(locationPins))
                        Toast.makeText(this@MainActivity, "Pines mostrados correctamente", Toast.LENGTH_LONG).show()

                    } else {
                        Toast.makeText(this@MainActivity, "Pines mostrados erroneamente", Toast.LENGTH_LONG).show()
                    }

            override fun onFailure(call: Call<LocationPin>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })


    }

    fun createPositionPinBitmap(): Bitmap {
        val file = "Phone\\Android\\data\\com.estimote.indoorapp\\files\\pin.png"
        val Path:InputStream = File(file).inputStream()

            val bitmap: Bitmap = BitmapFactory.decodeStream(Path)
        return bitmap
}

    fun drawocationPins(canvas: Canvas) {
       locationPin.pins.forEach {
           mMatrix.reset()
           val bitmapWidth = mPositionPinBitmap.width
           val bitmapHeight = mPositionPinBitmap.height
           val x = realXtoViewX(it.x)
           val y = realYtoViewY(it.y)
           mMatrix.setTranslate(x - bitmapWidth / 2f, y - bitmapHeight / 2f)
           mMatrix.postRotate(it.orientation.toFloat(), x, y)
           canvas.drawBitmap(mPositionPinBitmap, mMatrix, null)
       }
       canvas.drawPath(mLocationPinPath, mCustomPoint)
   }
    fun realXtoViewX(x: Double): Float {
        val vectorToCenterX = mDrawParams.centerX - mDrawParams.locationWidth / 2 * mDrawParams.scale
        return (x * mDrawParams.scale + vectorToCenterX).toFloat()
    }

    fun realYtoViewY(y: Double): Float {
        val vectorToCenterY = mDrawParams.centerY - mDrawParams.locationHeight / 2 * mDrawParams.scale
        return (mDrawParams.viewHeight - (y * mDrawParams.scale + vectorToCenterY)).toFloat()
    }
}










