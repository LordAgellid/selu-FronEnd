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

class Page1EnvoiCodeDeVerification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mdpo_page1)



        // Flèche de retour vers la page de connexion
        val flecheDeRetour1 = findViewById<ImageView>(R.id.flecheDeRetour1)
        flecheDeRetour1.setOnClickListener {
            retourPrecedentePage()
        }

        //Lien de redirection vers la vérification de code
        val btnEnvoiCodeVerification= findViewById<TextView>(R.id.btn_envoiCodeVerification)
        btnEnvoiCodeVerification.setOnClickListener {
            val courrielInput = findViewById<EditText>(R.id.courriel_input)
            //Regex pour l'email
            val regexEmail = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")

            //Variable de validation
            var valide : Boolean = false

            //Messages d'erreurs
            if (courrielInput.text.isEmpty()) {
                courrielInput.error = "Veuillez entrer votre courriel"
                valide = false
            } else if (regexEmail.matches(courrielInput.text)) {
                    valide = true
            } else {
                courrielInput.error = "Veuillez entrer un courriel valide , il doit contenir un @ & un ."
                valide = false
            }

            //Validation & Envoi
            if (valide) {
                envoyerCodeDeVerification(courrielInput.text.toString())
            } else {
                Toast.makeText(this, "Veuillez entrez votre adresse courriel.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun retourPrecedentePage() {
        val intent = Intent(this, Connexion::class.java)
        startActivity(intent)
    }

    private fun envoyerCodeDeVerification(courriel : String) {
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
                    val intent = Intent(this, Page2VerificationCode::class.java)
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