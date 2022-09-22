package com.example.selu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast


class Connexion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connexion)

        val lienVersInscription : TextView= findViewById(R.id.lienVersInscription)
        lienVersInscription.setOnClickListener() {
            redirectToInscription()
        }
    }

    private fun redirectToInscription() {
        val intent = Intent(this, Inscription::class.java)
        val text = "Redirection vers la page d'inscription"
        /*val duration = Toast.LENGTH_SHORT

        startActivity(intent)
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()*/
    }
}