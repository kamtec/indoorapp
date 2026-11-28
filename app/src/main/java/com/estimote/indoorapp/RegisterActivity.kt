package com.estimote.indoorapp

import android.content.Intent
import android.media.AudioManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    val TAG_LOGS = "estimote"
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtLastName2: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var progressBar: ProgressBar


    /* Implementar Lectura */
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
        setContentView(R.layout.activity_register)

        txtName = findViewById(R.id.txtName)
        txtLastName = findViewById(R.id.txtLastName)
        txtLastName2 = findViewById(R.id.txtLastName2)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)

        progressBar = findViewById(R.id.progressBar)

        CrearCuenta.setOnClickListener{
            var name:String = txtName.text.toString()
            var lastname:String = txtLastName.text.toString()
            var lastname2:String = txtLastName2.text.toString()
            var mail:String = txtEmail.text.toString()
            var password:String = txtPassword.text.toString()


            val user  = User(name,lastname,lastname2,mail,password)
            var userDes : FindItUsers = ServiceBuilder.buildService(FindItUsers::class.java)
            val solicitudUser : Call<User> =  userDes.insert(user)

            solicitudUser.enqueue(object : Callback <User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {


                        val user: User? = response.body()
                        Log.i(TAG_LOGS, Gson().toJson(user))
                        Toast.makeText(this@RegisterActivity , "Usuario registrado ", Toast.LENGTH_SHORT).show()
                        solomensaje()
                    }else
                    {
                        Toast.makeText(this@RegisterActivity , "No se pudo registraar ", Toast.LENGTH_LONG).show()
                     }


                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
            }


        }


    fun Inicio(view: View) {
        volverInicio()

    }

    private fun volverInicio() {
        startActivity(Intent(this, PrimerActivity::class.java))
    }


    private fun solomensaje() {

        tts = TextToSpeech(this, this)
        hablar = this.CrearCuenta
        hablar!!.isEnabled = false
        hablar!!.setOnClickListener {
            var bienvenidos = "Los datos ingresados fueron almacenados exitosamente en le Nube de Find TI. Bienvenidos a la nueva aplicación de posicionamiento de interiores, porfavor seleccione el destino y presione sobre el boton Prueba 1".toString()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts!!.speak(bienvenidos, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                val hash = HashMap<String, String>()
                hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                        AudioManager.STREAM_NOTIFICATION.toString())
                tts!!.speak(bienvenidos, TextToSpeech.QUEUE_FLUSH, hash)
            }


            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)

        }



    }






}
