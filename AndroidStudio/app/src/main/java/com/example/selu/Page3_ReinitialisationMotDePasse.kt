package com.example.selu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Page3_ReinitialisationMotDePasse : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mdpo_page3)

        // Flèche de retour vers la page de connexion
        val flecheDeRetour3 = findViewById<ImageView>(R.id.flecheDeRetour3)
        flecheDeRetour3.setOnClickListener() {
            retourPrecedentePage()
        }

        //Lien de redirection vers la vérification de code
        val btn_reinitialiserMotDePasse= findViewById<TextView>(R.id.btn_reinitialiserMotDePasse)
        btn_reinitialiserMotDePasse.setOnClickListener() {
            reinitialiserMotDePasse()
        }
    }

    private fun retourPrecedentePage() {
        val intent = Intent(this, Page2_VerificationCode::class.java)
        startActivity(intent)
    }

    private fun reinitialiserMotDePasse() {
        val intent = Intent(this, Connexion::class.java)
        startActivity(intent)
    }
}