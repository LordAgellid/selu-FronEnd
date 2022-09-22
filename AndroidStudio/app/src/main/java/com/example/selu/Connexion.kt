package com.example.selu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class Connexion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connexion)

        //Données de l'utilisateur
        val adresseCourriel : String = (findViewById<EditText>(R.id.courriel_input)).toString()
        val motDePasse : String = (findViewById<EditText>(R.id.motDePasse_input)).toString()

        //Lien de redirection vers la page d'inscription
        val lienVersInscription= findViewById<TextView>(R.id.lienVersInscription)
        lienVersInscription.setOnClickListener() {
            redirectToInscription()
        }
    }

    private fun redirectToInscription() {
        val intent = Intent(this, Inscription::class.java)
        startActivity(intent)
    }
}