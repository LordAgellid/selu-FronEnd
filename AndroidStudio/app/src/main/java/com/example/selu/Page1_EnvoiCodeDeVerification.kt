package com.example.selu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Page1_EnvoiCodeDeVerification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mdpo_page1)

        // Flèche de retour vers la page de connexion
        val flecheDeRetour1 = findViewById<ImageView>(R.id.flecheDeRetour1)
        flecheDeRetour1.setOnClickListener() {
            retourPrecedentePage()
        }

        //Lien de redirection vers la vérification de code
        val btn_envoiCodeVerification= findViewById<TextView>(R.id.btn_envoiCodeVerification)
        btn_envoiCodeVerification.setOnClickListener() {
            envoyerCodeDeVerification()
        }
    }

    private fun retourPrecedentePage() {
        val intent = Intent(this, Connexion::class.java)
        startActivity(intent)
    }

    private fun envoyerCodeDeVerification() {
        val intent = Intent(this, Page2_VerificationCode::class.java)
        startActivity(intent)
    }
}