package com.estimote.indoorapp

import android.content.Intent
import android.media.AudioManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import kotlinx.android.synthetic.main.activity_primer.*
import java.util.*

class PrimerActivity : AppCompatActivity(), TextToSpeech.OnInitListener {


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
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primer)







        Iniciar.setOnClickListener {
            /* Lectura segun el SDK*/
            tts = TextToSpeech(this, this)
            hablar = this.Iniciar
            hablar!!.isEnabled = false
            hablar!!.setOnClickListener {
                var bienvenidos = "Bienvenidos a la nueva aplicación de posicionamiento de interiores, porfavor ingrese su usuario y su contraseña en caso de haber olvidado la contraseña oprime el segundo boton de la parte inferior para poder recuperarla".toString()
                /* var Spinner = lista_destinos.selectedItem.toString()*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts!!.speak(bienvenidos, TextToSpeech.QUEUE_FLUSH, null, null)
                } else {
                    val hash = HashMap<String, String>()
                    hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                            AudioManager.STREAM_NOTIFICATION.toString())
                    tts!!.speak(bienvenidos, TextToSpeech.QUEUE_FLUSH, hash)
                }


                val intent1: Intent = Intent(this, LoginActivity::class.java)
                startActivity(intent1)

            }


        }
    Registrate.setOnClickListener {
        tts = TextToSpeech(this, this)
        hablar = this.Registrate
        hablar!!.isEnabled = false
        hablar!!.setOnClickListener {
            var bienvenidos = "Bienvenidos a la nueva aplicación de posicionamiento de interiores. Porfavor complete el formulario con la información solicitada, esta acción es necesaria una sola vez.".toString()
            /*var instrucciones = "".toString()*/
            /* var Spinner = lista_destinos.selectedItem.toString()*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts!!.speak(bienvenidos /*+ instrucciones*/, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                val hash = HashMap<String, String>()
                hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                        AudioManager.STREAM_NOTIFICATION.toString())
                tts!!.speak(bienvenidos /*+ instrucciones*/, TextToSpeech.QUEUE_FLUSH, hash)
            }
            val intent2: Intent = Intent(this, RegisterActivity::class.java)

            startActivity(intent2)

        }

    }
    }
}
