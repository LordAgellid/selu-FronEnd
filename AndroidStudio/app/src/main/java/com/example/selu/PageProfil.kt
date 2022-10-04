package com.example.selu

import android.content.Intent
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.selu.Connexion.Companion.EXTRA_NAME
import com.example.selu.databinding.PageProfilBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso

class PageProfil : AppCompatActivity() {

    private lateinit var binding : PageProfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PageProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContentView(R.layout.page_profil)
        getProfil()
    }

    @SuppressLint("SetTextI18n")
    private fun getProfil() {
        val courriel = this.intent.getStringExtra("Courriel")
        val token = this.intent.getStringExtra("Token")
        val photoProfile = this.findViewById<ImageView>(R.id.image_profil)
        photoProfile.setImageResource(R.drawable.google_g_logo)

        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2:3000/profile/${courriel}"
        @Suppress("RedundantSamConstructor")
        val jsonRequest = JsonArrayRequest (Request.Method.GET, url, null,
            Response.Listener {
                val data = it.getJSONObject(0)
                println(data)
                val photoDeProfil = data.getString("PhotoDeProfil").toString()
                println(photoDeProfil)
                val nomDeFamille = data.getString("NomDeFamille").toString()
                val prenom = data.getString("Prenom").toString()
                findViewById<TextView>(R.id.username).text = "$prenom $nomDeFamille"
                if(photoDeProfil == "null" || photoDeProfil == "") {
                    println("Photo est null")
                } else {
                    println("Photo pas null")
                    Picasso.get().load(photoDeProfil).into(photoProfile)
                }
            },
            Response.ErrorListener {
                println(it)
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
            })
        queue.add(jsonRequest)
    }
}