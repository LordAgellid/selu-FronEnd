package com.example.selu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Inscription : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inscription)
    }
    private fun redirectToConnexion() {
        val intent = Intent(this, Connexion::class.java)
        val text = "Redirection vers la page de connexion"
        /*val duration = Toast.LENGTH_SHORT

        startActivity(intent)
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()*/
    }
}