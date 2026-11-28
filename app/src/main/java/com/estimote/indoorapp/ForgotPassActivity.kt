package com.estimote.indoorapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_forgot_pass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassActivity : AppCompatActivity() {

    val TAG_LOGS = "estimote"
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var txtPassword2: EditText
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)


        txtEmail = findViewById(R.id.txtEmai)
        txtPassword = findViewById(R.id.txtPass)
        txtPassword2 = findViewById(R.id.txtPass2)
        progressBar = findViewById(R.id.progressBar)

        ActPassword.setOnClickListener {
            val mail: String = txtEmail.text.toString()
            val password: String = txtPassword.text.toString()
            val corfpassword: String = txtPassword2.text.toString()
            val user  = User(null, null, null, mail, null)
            val userdto = UserDTO(user, password, corfpassword)
            val userDes: FindItUsers = ServiceBuilder.buildService(FindItUsers::class.java)
            val solicitudUser: Call<UserDTO> = userDes.updatepaasword(userdto)
            solicitudUser.enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if (response.isSuccessful) {
                        val userdto: UserDTO? = response.body()
                        Log.i(TAG_LOGS, Gson().toJson(userdto))
                        Toast.makeText(this@ForgotPassActivity, "Contraseña modificada correctamente", Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        Toast.makeText(this@ForgotPassActivity, " error al modificar contraseña", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(this@ForgotPassActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}
