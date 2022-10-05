package com.example.selu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.selu.databinding.ConnexionBinding
import com.example.selu.databinding.InscriptionBinding
import org.json.JSONObject

class Inscription : AppCompatActivity() {

    private lateinit var binding : InscriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Regex pour le prénom
        val regexPrenom = Regex("^[A-Za-z]+$")
        //Regex pour le nom
        val regexNom = Regex("^[A-Za-z]+$")
        //Regex pour le courriel
        val regexEmail = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")
        //Regex pour le mot de passe (8 caractères, 1 majuscule, 1 minuscule, 1 chiffre)
        val regexMotDePasse = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}\$")

        //Variable de validation
        var valide: Boolean

        //Valeurs d'entrée
        val nomInput = binding.nomInput
        val prenomInput = binding.prenomInput
        val courrielInput = binding.courrielInput
        val motDePasseInput1 = binding.motDePasseInput1
        val motDePasseInput2 = binding.motDePasseInput2

        //Lien de redirection vers la page d'inscription
        val lienVersConnexion = binding.lienVersConnexion
        lienVersConnexion.setOnClickListener {
            redirectToConnexion()
        }

        //Bouton inscrire
        val btnInscrireption = binding.btnInscription
        btnInscrireption.setOnClickListener {
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

    public fun inscription(prenom : String, nom : String, courriel : String, motDePasse : String) {
        val messageSuccess = "Inscription réussi !"
        val messageEchec = "L'adresse courriel inscrite existe déjà."

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