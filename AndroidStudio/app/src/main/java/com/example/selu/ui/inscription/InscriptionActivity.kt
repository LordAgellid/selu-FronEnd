package com.example.selu.ui.inscription

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.selu.MainActivity
import com.example.selu.R
import com.example.selu.ui.connexion.ConnexionViewModel

class InscriptionActivity: AppCompatActivity() {

    private lateinit var inscriptionViewModel: InscriptionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscription)

        this.inscriptionViewModel =
            ViewModelProvider(this).get(InscriptionViewModel::class.java)

        //Cacher l'action bar
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        //button inscription
        val prenomInput = findViewById<EditText>(R.id.prenom_input)
        val nomInput = findViewById<EditText>(R.id.nom_input)
        val courrielInput = findViewById<EditText>(R.id.adresse_courriel_input)
        val motDePasseInput1 = findViewById<EditText>(R.id.mot_de_passe_input)
        val motDePasseInput2 = findViewById<EditText>(R.id.confirmation_mdop_input)
        val inscription = findViewById<Button>(R.id.btn_inscription)


        inscription.setOnClickListener {
            //Messages d'erreurs
            // -> Prénom
            if (prenomInput.text.isEmpty() ){
                prenomInput.error = "Veuillez entrer votre prénom"
            }  else if (MainActivity.REGEX_NOM_PRENOM.matches(prenomInput.text)) {
                MainActivity.VALIDE = true
            } else {
                prenomInput.error = "Veuillez entrer un prénom valide, il ne doit contenir que des lettres"
            }
            // -> Nom
            if (nomInput.text.isEmpty()) {
                nomInput.error = "Veuillez entrer votre nom"
            } else if (MainActivity.REGEX_NOM_PRENOM.matches(nomInput.text)) {
                MainActivity.VALIDE = true
            } else {
                nomInput.error = "Veuillez entrer un nom valide, il ne doit contenir que des lettres"
            }
            // -> Courriel
            if (courrielInput.text.isEmpty()) {
                courrielInput.error = "Veuillez entrer votre courriel"
            }else if (MainActivity.REGEX_COURRIEL.matches(courrielInput.text)) {
                MainActivity.VALIDE = true
            } else {
                courrielInput.error = "Veuillez entrer un courriel valide , il doit contenir un @ et un ."
            }
            // -> Mots de passe
            if (motDePasseInput1.text.isEmpty() || motDePasseInput2.text.isEmpty()) {
                motDePasseInput1.error = "Veuillez entrer votre mot de passe"
                motDePasseInput2.error = "Veuillez confirmer votre mot de passe"
            } else if (motDePasseInput1.text.toString() != motDePasseInput2.text.toString()) {
                motDePasseInput1.error = "Les mots de passe ne correspondent pas"
                motDePasseInput2.error = "Les mots de passe ne correspondent pas"
            } else if (MainActivity.REGEX_MOT_DE_PASSE.matches(motDePasseInput1.text)) {
                MainActivity.VALIDE = true
            } else {
                motDePasseInput1.error = "Veuillez entrer un mot de passe valide, il doit contenir au moins 8 caractères, 1 majuscule, 1 minuscule et 1 chiffre"
            }

            //Validation & connexion
            if (MainActivity.VALIDE) {
                inscriptionViewModel.inscription(prenomInput.text.toString(), nomInput.text.toString(), courrielInput.text.toString(), motDePasseInput1.text.toString())
            }

        }


        inscriptionViewModel.IfSucesss.observe(this) {
            if (it) {
                Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, com.example.selu.ui.connexion.ConnexionActivity ::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "ERREUR: Inscription échouée. Veuillez réessayer !", Toast.LENGTH_SHORT).show()
            }
        }
    }
}