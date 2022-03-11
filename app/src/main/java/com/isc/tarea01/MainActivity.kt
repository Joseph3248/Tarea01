package com.isc.tarea01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.isc.tarea01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        auth = Firebase.auth


        //Metodos de login y Registro
        binding.btLogin.setOnClickListener { haceLogin() }
        binding.btRegister.setOnClickListener { haceRegistro() }
    }

    private fun haceRegistro() {

        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    actualiza(user)
                } else {

                    Toast.makeText(
                        baseContext,
                        getString(R.string.msg_fallo_registro),
                        Toast.LENGTH_LONG
                    ).show()
                    actualiza(null)
                }


            }

    }

    private fun haceLogin() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    actualiza(user)
                } else {

                    Toast.makeText(
                        baseContext,
                        getString(R.string.msg_fallo_login),
                        Toast.LENGTH_LONG
                    ).show()
                    actualiza(null)
                }


            }

    }

    private fun actualiza(user: FirebaseUser?) {
        if (user != null) {

            val intent = Intent(this, Principal::class.java)
            startActivity(intent)
        }

    }

    public override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        actualiza(user)
    }
}