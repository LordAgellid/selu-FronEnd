package com.example.selu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Page2_VerificationCode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mdpo_page2)

        // Flèche de retour vers la page d'envoi du code de vérification
        val flecheDeRetour2 = findViewById<ImageView>(R.id.flecheDeRetour2)
        flecheDeRetour2.setOnClickListener() {
            retourPrecedentePage()
        }

        //Lien de redirection vers la vérification de code
        val btn_verificationCode= findViewById<TextView>(R.id.btn_verificationCode)
        btn_verificationCode.setOnClickListener() {
            verifierCodeDeVerification()
        }
    }

    private fun retourPrecedentePage() {
        val intent = Intent(this, Page1_EnvoiCodeDeVerification::class.java)
        startActivity(intent)
    }

    private fun verifierCodeDeVerification() {
        val intent = Intent(this, Page3_ReinitialisationMotDePasse::class.java)
        startActivity(intent)
    }
}