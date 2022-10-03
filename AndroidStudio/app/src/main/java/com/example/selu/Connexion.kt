package com.example.selu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Connexion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connexion)

        //Regex pour le courriel
        val regexEmail = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")
        //Regex pour le mot de passe (8 caractères, 1 majuscule, 1 minuscule, 1 chiffre)
        val regexMotDePasse = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}\$")

        //Variable de validation
        var valide: Boolean

        //Valeurs d'entrée
        val courrielInput = findViewById<EditText>(R.id.courriel_input)
        val motDePasseInput = findViewById<EditText>(R.id.motDePasse_input)

        //Lien de redirection vers la page de mot de passe oublié
        val lienVersMdpOublie= findViewById<TextView>(R.id.lienVersMdpOublie)
        lienVersMdpOublie.setOnClickListener {
            redirectToMotDePasseOublie()
        }

        //Connexion au profil
        val btnConnexion = findViewById<Button>(R.id.btn_connexion)
        btnConnexion.setOnClickListener {
            //Messages d'erreurs
            // -> Courriel
            if (courrielInput.text.isEmpty()) {
                courrielInput.error = "Veuillez entrer votre courriel"
                valide = false
            } else if (regexEmail.matches(courrielInput.text.toString())) {
                    valide = true
            } else {
                courrielInput.error = "Veuillez entrer un courriel valide, il doit contenir un @ & un ."
                valide = false
            }
            // -> Mot de passe
            if (motDePasseInput.text.isEmpty()) {
                motDePasseInput.error = "Veuillez entrer votre mot de passe"
                valide = false
            } else if (regexMotDePasse.matches(motDePasseInput.text)) {
                    valide = true
            } else {
                motDePasseInput.error = "Veuillez entrer un mot de passe valide, il doit contenir au moins 8 caractères, 1 majuscule, 1 minuscule et 1 chiffre"
                valide = false
            }

            //Validation & connexion
            if (valide) {
                connexion(
                    courrielInput.text.toString(),
                    motDePasseInput.text.toString()
                )
            }
        }

        //Lien de redirection vers la page d'inscription
        val lienVersInscription= findViewById<TextView>(R.id.lienVersInscription)
        lienVersInscription.setOnClickListener {
            redirectToInscription()
        }
    }

    private fun redirectToMotDePasseOublie() {
        val intent = Intent(this, Page1EnvoiCodeDeVerification::class.java)
        startActivity(intent)
    }

    private fun connexion(courriel :String, motDePasse : String) {
        val messageSuccess = "Connexion réussi !"
        val messageEchec = "ERREUR: Connexion échouée. Veuillez  !"

        val url = "http://10.0.2.2:3000/connexion"
        val queue = Volley.newRequestQueue(this)
        val body = JSONObject()
        body.put("Courriel", courriel)
        body.put("MotDePasse", motDePasse)

        val postRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            body,
            {
                println(it)
                if(it.getBoolean("success")) {
                    val intent = Intent(this, PageMonProfil::class.java)
                    val token = it.getString("access_token")

                    intent.putExtra("Courriel", courriel)
                    intent.putExtra("Token", token)

                    this.startActivity(intent)
                    Toast.makeText(this, messageSuccess, Toast.LENGTH_LONG).show()
                }
            },
            {
                println(it)
                Toast.makeText(this, messageEchec, Toast.LENGTH_LONG).show()
            }
        )
        queue.add(postRequest)
    }

    private fun redirectToInscription() {
        val intent = Intent(this, Inscription::class.java)
        startActivity(intent)
    }
}