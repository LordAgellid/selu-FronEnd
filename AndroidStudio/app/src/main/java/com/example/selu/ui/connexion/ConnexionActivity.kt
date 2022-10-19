package com.example.selu.ui.connexion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.example.selu.MainActivity
import com.example.selu.MdpOublier
import com.example.selu.R

class ConnexionActivity : AppCompatActivity() {
    private lateinit var connexionViewModel: ConnexionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Écran de chargement
        //Thread.sleep(2500)
        val splashScreen = installSplashScreen()

        setContentView(R.layout.activity_connexion)
        connexionViewModel = ViewModelProvider(this)[ConnexionViewModel::class.java]

        //Cacher l'action bar
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }


        //Lien pour rediriger vers l'inscription
        val inscription = findViewById<TextView>(R.id.lien_vers_inscription)
        inscription.setOnClickListener {
            val intent = Intent(this, com.example.selu.ui.inscription.InscriptionActivity::class.java)
            startActivity(intent)
        }

        //lien vers mdp oublié
        val mdpOublie = findViewById<TextView>(R.id.lien_vers_mdop)
        mdpOublie.setOnClickListener {
            val intent = Intent(this, MdpOublier::class.java)
            startActivity(intent)
        }

        //Fonction de connexion
        val btnConnexion = findViewById<Button>(R.id.btn_connexion)
        btnConnexion.setOnClickListener {
            //Valeurs d'entrée
            val adresseCourrielInput = findViewById<EditText>(R.id.adresse_courriel_input)
            val motDePasseInput = findViewById<EditText>(R.id.mot_de_passe_input)

            //Messages d'erreurs
            // -> Courriel
            if (adresseCourrielInput.text.isEmpty()) {
                adresseCourrielInput.error = "Veuillez entrer votre courriel"
            } else if (MainActivity.REGEX_COURRIEL.matches(adresseCourrielInput.text.toString())) {
                MainActivity.VALIDE = true
            } else {
                adresseCourrielInput.error = "Veuillez entrer un courriel valide, il doit contenir un @ & un ."
            }
            // -> Mot de passe
            if (motDePasseInput.text.isEmpty()) {
                motDePasseInput.error = "Veuillez entrer votre mot de passe"
            } else if (MainActivity.REGEX_MOT_DE_PASSE.matches(motDePasseInput.text)) {
                MainActivity.VALIDE = true
            } else {
                motDePasseInput.error = "Veuillez entrer un mot de passe valide, il doit contenir au moins 8 caractères, 1 majuscule, 1 minuscule et 1 chiffre"
            }

            connexionViewModel.courriel = adresseCourrielInput.text.toString()
            connexionViewModel.motDePasse = motDePasseInput.text.toString()
            if (MainActivity.VALIDE) {
                connexionViewModel.connexion()
            }
        }

        //Vérificaiton de la connexion & redirection
        connexionViewModel.isValid.observe(this) {
            if(it == true) {
                val adresseCourrielInput = findViewById<EditText>(R.id.adresse_courriel_input)
                MainActivity.EMAIL = adresseCourrielInput.text.toString()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        //Lien pour rediriger vers la page d'inscription
        val lienVersInscription = findViewById<TextView>(R.id.lien_vers_inscription)
        lienVersInscription.setOnClickListener {
            val intent = Intent(this, com.example.selu.ui.inscription.InscriptionActivity::class.java)
            startActivity(intent)
        }
    }
}
