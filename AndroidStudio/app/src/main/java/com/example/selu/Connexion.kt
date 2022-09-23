package com.example.selu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Connexion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connexion)

        //Données de l'utilisateur


        //Lien de redirection vers la page de mot de passe oublié
        val lienVersMdpOublie= findViewById<TextView>(R.id.lienVersMdpOublie)
        lienVersMdpOublie.setOnClickListener() {
            redirectToMotDePasseOublie()
        }

        //Connexion au profil
        val btnConnexion = findViewById<Button>(R.id.btn_connexion)
        btnConnexion.setOnClickListener {
            val courrielInput : String = (findViewById<EditText>(R.id.courriel_input).text).toString()
            val motDePasseInput : String = (findViewById<EditText>(R.id.motDePasse_input).text).toString()
            connexion(courrielInput, motDePasseInput)
        }

        //Lien de redirection vers la page d'inscription
        val lienVersInscription= findViewById<TextView>(R.id.lienVersInscription)
        lienVersInscription.setOnClickListener() {
            redirectToInscription()
        }
    }

    private fun redirectToMotDePasseOublie() {
        val intent = Intent(this, Page1EnvoiCodeDeVerification::class.java)
        startActivity(intent)
    }

    private fun connexion(courriel :String, motDePasse : String) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2:3000/connexion"
        val body = JSONObject()
        body.put("Courriel", courriel)
        body.put("MotDePasse", motDePasse)

        val r = JsonObjectRequest(
            Request.Method.POST,
            url,
            body,
            {
                if(it.getBoolean("success")) {
                    val intent = Intent(this, PageProfil::class.java)

                    val nom = it.getString("Nom")
                    val prenom = it.getString("Prenom")
                    val token = it.getString("access_token")

                    intent.putExtra("Nom", nom)
                    intent.putExtra("Prenom", prenom)
                    intent.putExtra("Courriel", courriel)
                    intent.putExtra("Token", token)

                    this.startActivity(intent)
                }
            },
            {
                println(it)
            }
        )
        queue.add(r)
    }
    private fun redirectToInscription() {
        val intent = Intent(this, Inscription::class.java)
        startActivity(intent)
    }
}