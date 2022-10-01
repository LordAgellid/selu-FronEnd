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

class Page3ReinitialisationMotDePasse : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mdpo_page3)

        //Regex pour le mot de passe (8 caractères, 1 majuscule, 1 minuscule, 1 chiffre)
        val regexMotDePasse = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}\$")

        //Valeurs d'entrée
        val courriel = (this.intent.getStringExtra("Courriel"))
        val nouveauMotDePasseInput1 = this.findViewById<EditText>(R.id.nv_motDePasse_input1)
        val nouveauMotDePasseInput2 = this.findViewById<EditText>(R.id.nv_MotDePasse_input2)

        //Variable de validation
        var valide : Boolean = false

        //Flèche de retour vers la page de connexion
        val flecheDeRetour3 = findViewById<ImageView>(R.id.flecheDeRetour3)
        flecheDeRetour3.setOnClickListener {
            retourPrecedentePage()
        }

        //Lien de redirection vers la vérification de code
        val btnReinitialiserMotDePasse = findViewById<TextView>(R.id.btn_reinitialiserMotDePasse)
        btnReinitialiserMotDePasse.setOnClickListener {
            //Messages d'erreurs
            // -> Mots de passe
            if (nouveauMotDePasseInput1.text.isEmpty() || nouveauMotDePasseInput2.text.isEmpty()) {
                nouveauMotDePasseInput1.error = "Veuillez entrer votre mot de passe"
                nouveauMotDePasseInput2.error = "Veuillez entrer votre mot de passe"
                valide = false
            } else if (nouveauMotDePasseInput2.text.toString() != nouveauMotDePasseInput1.text.toString()) {
                nouveauMotDePasseInput2.error = "Le mot de passe ne correspondent pas au premier"
                valide = false
            } else if (regexMotDePasse.matches(nouveauMotDePasseInput1.text)) {
                valide = true
            } else {
                nouveauMotDePasseInput1.error = "Veuillez entrer un mot de passe valide, il doit contenir au moins 8 caractères, 1 majuscule, 1 minuscule et 1 chiffre"
                valide = false
            }

            //Validation & Envoi
            if(valide && courriel != null) {
                reinitialiserMotDePasse(nouveauMotDePasseInput2.text.toString(), courriel)
            }
        }
    }

    private fun retourPrecedentePage() {
        val intent = Intent(this, Page2VerificationCode::class.java)
        startActivity(intent)
    }

    private fun reinitialiserMotDePasse(nouveauMotDePasse: String, courriel : String) {
        val messageSuccess = "Réinitialisation de mot de passe réussi !"
        val messageEchec = "ERREUR: Réinitialisation de mot de passe échouée. Veuillez réessayer !"

        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2:3000/utilisateurs/modifierMotDePasse"
        val body = JSONObject()
        body.put("MotDePasse", nouveauMotDePasse)
        body.put("Courriel", courriel)

        val r = JsonObjectRequest(
            Request.Method.PUT,
            url,
            body,
            {
                println(it)
                if(it.getBoolean("success")) {
                    val intent = Intent(this, Connexion::class.java)
                    intent.putExtra("Courriel", courriel)
                    intent.putExtra("MotDePasse", nouveauMotDePasse)
                    this.startActivity(intent)

                    Toast.makeText(this, messageSuccess, Toast.LENGTH_LONG).show()
                }
            },
            {
                println(it)
                Toast.makeText(this, messageEchec, Toast.LENGTH_LONG).show()
            }
        )
        queue.add(r)
    }
}