package com.example.selu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.selu.databinding.PageMonprofilBinding
import com.example.selu.databinding.PageProfilBinding
import com.squareup.picasso.Picasso

class PageMonProfil : AppCompatActivity() {

    private lateinit var binding: PageMonprofilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_monprofil)
        getProfil()
        val btnEdit = findViewById<ImageView>(R.id.edit)
        btnEdit.setOnClickListener {
            redirectToEditProfil()
        }
    }

    private fun redirectToEditProfil() {
        val intent = Intent(this, PageProfilEdit::class.java)

        val courriel = this.intent.getStringExtra("Courriel")
        val token = this.intent.getStringExtra("Token")

        intent.putExtra("Courriel", courriel)
        intent.putExtra("Token", token)
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun getProfil() {
        val courriel = this.intent.getStringExtra("Courriel")
        val token = this.intent.getStringExtra("Token")
        val photoProfile = this.findViewById<ImageView>(R.id.image_profil)


        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2:3000/profile/${courriel}"
        @Suppress("RedundantSamConstructor")
        val jsonRequest = JsonArrayRequest (
            Request.Method.GET, url, null,
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
                    Picasso.get().load("https://bit.ly/3e4JL4V").into(photoProfile)
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