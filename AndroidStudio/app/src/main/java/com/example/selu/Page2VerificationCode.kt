package com.example.selu

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Page2VerificationCode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mdpo_page2)

        // Flèche de retour vers la page d'envoi du code de vérification
        val flecheDeRetour2 = findViewById<ImageView>(R.id.flecheDeRetour2)
        flecheDeRetour2.setOnClickListener() {
            retourPrecedentePage()
        }

        // Renvoi du code de vérification
        val lienRenvoiCodeDeVerification = findViewById<TextView>(R.id.lienRenvoiCodeVerification)
        lienRenvoiCodeDeVerification.setOnClickListener {
            val courriel = (this.intent.getStringExtra("Courriel"))
            if (courriel != null) {
                renvoiCodeDeVerification(courriel)
            }
        }

        //Lien de redirection vers la vérification de code
        val btnVerificationCode= findViewById<TextView>(R.id.btn_verificationCode)
        btnVerificationCode.setOnClickListener {
            val codeInput = ((findViewById<EditText>(R.id.code_input)).text).toString()
            val courriel = (this.intent.getStringExtra("Courriel")).toString()
            verifierCodeDeVerification(codeInput, courriel)
        }
    }

    private fun retourPrecedentePage() {
        val intent = Intent(this, Page1EnvoiCodeDeVerification::class.java)
        startActivity(intent)
    }

    private fun renvoiCodeDeVerification(courriel : String) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2:3000/codeVerificaion/envoyerMailConfirmationMdp"
        val body = JSONObject()
        body.put("Courriel", courriel)

        val r = JsonObjectRequest(
            Request.Method.POST,
            url,
            body,
            {
                if(it.getBoolean("success")) {
                    val Text = "Le code de vérification a été renvoyé à l'adresse: ${courriel}."
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(applicationContext, Text, duration)
                    toast.show()
                }
            },
            {
                println(it)
            }
        )
        queue.add(r)
    }

    private fun verifierCodeDeVerification(code : String, courriel : String) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2:3000/codeVerificaion/confirmationCode"
        val body = JSONObject()
        body.put("Code", code)
        body.put("Courriel", courriel)

        val r = JsonObjectRequest(
            Request.Method.POST,
            url,
            body,
            {
                if(it.getBoolean("success")) {
                    val intent = Intent(this, Page3ReinitialisationMotDePasse::class.java)
                    intent.putExtra("Courriel", courriel)
                    this.startActivity(intent)
                }
            },
            {
                println(it)
            }
        )
        queue.add(r)
    }
}