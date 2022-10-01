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

class Inscription : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inscription)

        //Regex pour le prénom
        val regexPrenom = Regex("^[A-Za-z]+$")
        //Regex pour le nom
        val regexNom = Regex("^[A-Za-z]+$")
        //Regex pour le courriel
        val regexEmail = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")
        //Regex pour le mot de passe (8 caractères, 1 majuscule, 1 minuscule, 1 chiffre)
        val regexMotDePasse = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}\$")

        //Variable de validation
        var valide : Boolean = false

        //Valeurs d'entrée
        val nomInput = this.findViewById<EditText>(R.id.nom_input)
        val prenomInput = this.findViewById<EditText>(R.id.prenom_input)
        val courrielInput = this.findViewById<EditText>(R.id.courriel_input)
        val motDePasseInput1 = this.findViewById<EditText>(R.id.mdp_input)
        val motDePasseInput2 = this.findViewById<EditText>(R.id.confirmer_mdp_input)

        //Lien de redirection vers la page d'inscription
        val lienVersConnexion : TextView = findViewById(R.id.lienVersConnexion)
        lienVersConnexion.setOnClickListener {
            redirectToConnexion()
        }

        //Bouton inscrire
        val boutonInscrire = this.findViewById<Button>(R.id.btn_inscription)
        boutonInscrire.setOnClickListener {
            //Messages d'erreurs
            // -> Prénom
            if (prenomInput.text.isEmpty() ){
                prenomInput.error = "Veuillez entrer votre prénom"
                valide = false
            }  else if (regexPrenom.matches(prenomInput.text)) {
                valide = true
            } else {
                prenomInput.error = "Veuillez entrer un prénom valide, il ne doit contenir que des lettres"
                valide = false
            }
            // -> Nom
            if (nomInput.text.isEmpty()) {
                nomInput.error = "Veuillez entrer votre nom"
                valide = false
            } else if (regexNom.matches(nomInput.text)) {
                valide = true
            } else {
                nomInput.error = "Veuillez entrer un nom valide, il ne doit contenir que des lettres"
                valide = false
            }
            // -> Courriel
            if (courrielInput.text.isEmpty()) {
                courrielInput.error = "Veuillez entrer votre courriel"
                valide = false
            }else if (regexEmail.matches(courrielInput.text)) {
                valide = true
            } else {
                courrielInput.error = "Veuillez entrer un courriel valide , il doit contenir un @ et un ."
                valide = false
            }
            // -> Mots de passe
            if (motDePasseInput1.text.isEmpty() || motDePasseInput2.text.isEmpty()) {
                motDePasseInput1.error = "Veuillez entrer votre mot de passe"
                motDePasseInput2.error = "Veuillez confirmer votre mot de passe"
                valide = false
            } else if (motDePasseInput1.text.toString() != motDePasseInput2.text.toString()) {
                motDePasseInput1.error = "Les mots de passe ne correspondent pas"
                motDePasseInput2.error = "Les mots de passe ne correspondent pas"
                valide = false
            } else if (regexMotDePasse.matches(motDePasseInput1.text)) {
                valide = true
            } else {
                motDePasseInput1.error = "Veuillez entrer un mot de passe valide, il doit contenir au moins 8 caractères, 1 majuscule, 1 minuscule et 1 chiffre"
                valide = false
            }

            //Validation & connexion
            if (valide) {
                inscription(
                    prenomInput.text.toString(),
                    nomInput.text.toString(),
                    courrielInput.text.toString(),
                    motDePasseInput1.text.toString()
                )
            }
        }
    }

    private fun inscription(prenom : String, nom : String, courriel : String, motDePasse : String) {
        val messageSuccess = "Inscription réussi !"
        val messageEchec = "ERREUR: Inscription échouée. Veuillez réessayer !"

        val url = "http://10.0.2.2:3000/utilisateurs/inscription"
        val queue = Volley.newRequestQueue(this)
        val body = JSONObject()

        body.put("Prenom", prenom.trim())
        body.put("NomDeFamille", nom.trim())
        body.put("Courriel", courriel.trim())
        body.put("MotDePasse", motDePasse.trim())

        val postRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            body,
            {
                println(it)
                redirectToConnexion()
                Toast.makeText(this, messageSuccess, Toast.LENGTH_LONG).show()
            },
            {
                println(it)
                Toast.makeText(this, messageEchec, Toast.LENGTH_LONG).show()
            }
        )
        queue.add(postRequest)
    }

    private fun redirectToConnexion() {
        val intent = Intent(this, Connexion::class.java)
        startActivity(intent)
    }
}