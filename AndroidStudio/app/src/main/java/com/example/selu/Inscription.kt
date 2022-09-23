package com.example.selu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Inscription : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inscription)

        //Regex pour le mot de passe (8 caractères, 1 majuscule, 1 minuscule, 1 chiffre)
        val regexMDP = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}\$")

        //Regex pour l'email
        val regexEmail = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")

        //Regex pour le nom
        val regexNom = Regex("^[A-Za-z]{1,}$")

        //Regex pour le prénom
        val regexPrenom = Regex("^[A-Za-z]{1,}$")

        //Input inscription
        val nom_input = this.findViewById<EditText>(R.id.nom_input)
        val prenom_input = this.findViewById<EditText>(R.id.prenom_input)
        val courriel_input = this.findViewById<EditText>(R.id.courriel_input)
        val mdp_input = this.findViewById<EditText>(R.id.mdp_input)
        val confirmer_mdp_input = this.findViewById<EditText>(R.id.confirmer_mdp_input)

        //Lien de redirection vers la page d'inscription
        val lienVersConnexion : TextView = findViewById(R.id.lienVersConnexion)
        //Bouton inscrire
        val boutonInscrire = this.findViewById<Button>(R.id.btn_inscription)

        //Variable de validation
        var valide : Boolean = false

        lienVersConnexion.setOnClickListener() {
            redirectToConnexion()
        }
        boutonInscrire.setOnClickListener {
            if (nom_input.text.isEmpty()) {
                nom_input.error = "Veuillez entrer votre nom"
                valide = false
            } else{
                if (regexNom.matches(nom_input.text)) {
                    valide = true
                } else {
                    nom_input.error = "Veuillez entrer un nom valide, il ne doit contenir que des lettres"
                    valide = false
                }
            }
            if (prenom_input.text.isEmpty() ){
                prenom_input.error = "Veuillez entrer votre prénom"
                valide = false
            }  else{
                if (regexPrenom.matches(prenom_input.text)) {
                    valide = true
                } else {
                    prenom_input.error = "Veuillez entrer un prénom valide, il ne doit contenir que des lettres"
                    valide = false
                }
            }
            if (courriel_input.text.isEmpty()) {
                courriel_input.error = "Veuillez entrer votre courriel"
                valide = false
            }else{
                if (regexEmail.matches(courriel_input.text)) {
                    valide = true
                } else {
                    courriel_input.error = "Veuillez entrer un courriel valide , il doit contenir un @ et un ."
                    valide = false
                }
            }
            if (mdp_input.text.isEmpty() || confirmer_mdp_input.text.isEmpty()) {
                mdp_input.error = "Veuillez entrer votre mot de passe"
                confirmer_mdp_input.error = "Veuillez confirmer votre mot de passe"
                valide = false
            } else {
                if (mdp_input.text.toString() != confirmer_mdp_input.text.toString())
                {
                    mdp_input.error = "Les mots de passe ne correspondent pas"
                    confirmer_mdp_input.error = "Les mots de passe ne correspondent pas"
                    valide = false
                } else {
                    if (regexMDP.matches(mdp_input.text)) {
                        valide = true
                    } else {
                        mdp_input.error = "Veuillez entrer un mot de passe valide, il doit contenir au moins 8 caractères, 1 majuscule, 1 minuscule et 1 chiffre"
                        valide = false
                    }
                }
            }

            if (valide) {
                println(nom_input.text.toString().trim())
                println(prenom_input.text.toString().trim())
                println(courriel_input.text.toString().trim())
                println(mdp_input.text.toString().trim())
                println(confirmer_mdp_input.text.toString().trim())
                println("dans volley")
                val url = "http://10.0.2.2:3000/utilisateurs/inscription"
                val queue = Volley.newRequestQueue(this)
                val body = JSONObject()
                body.put("Prenom", prenom_input.text.toString().trim())
                body.put("NomDeFamille", nom_input.text.toString().trim())
                body.put("Courriel", courriel_input.text.toString().trim())
                body.put("MotDePasse", mdp_input.text.toString().trim())
                val postRequest = JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    body,
                    {
                        println(it)
                        redirectToConnexion()
                    },
                    {
                        println(it)
                        Toast.makeText(this, "Erreur", Toast.LENGTH_LONG).show()
                    }
                )
                queue.add(postRequest)
            } else {
                Toast.makeText(this, "Le mot de passe ne correspond pas deux fois", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun redirectToConnexion() {
        val intent = Intent(this, Connexion::class.java)
        startActivity(intent)
    }
}