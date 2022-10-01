package com.example.selu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class PageProfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_profil)

        getProfil()
    }

    private fun getProfil() {
        val nom = this.intent.getStringExtra("Nom")
        val prenom = this.intent.getStringExtra("Prenom")
        val courriel = this.intent.getStringExtra("Courriel")
        val token = this.intent.getStringExtra("Token")

        val nomComplet : String = "${prenom} ${nom}"

        findViewById<TextView>(R.id.username).text = nomComplet
//        findViewById<TextView>(R.id.email).text = courriel
//        findViewById<TextView>(R.id.token).text = token
    }
}