package com.estimote.indoorapp

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EjemploActivity:  AppCompatActivity() {
    val TAG_LOGS = "ejemplo"
private lateinit var textNombre: EditText
private lateinit var textLastname: EditText
private lateinit var textLastname2: EditText
private lateinit var textEmail: EditText
private lateinit var textPass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ejemplo1)

        textNombre = findViewById(R.id.textNombre)
        textLastname = findViewById(R.id.txtLastname)
        textLastname2 = findViewById(R.id.txtLastname2)
        textEmail = findViewById(R.id.textEmail)
        textPass = findViewById(R.id.textPass)


    }
}