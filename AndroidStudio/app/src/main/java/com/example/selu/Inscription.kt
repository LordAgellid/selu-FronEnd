package com.example.selu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Inscription : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inscription)

        //Lien de redirection vers la page d'inscription
        val lienVersConnexion : TextView = findViewById(R.id.lienVersConnexion)
        lienVersConnexion.setOnClickListener() {
            redirectToConnexion()
        }
    }
    private fun redirectToConnexion() {
        val intent = Intent(this, Connexion::class.java)
        startActivity(intent)
    }
}