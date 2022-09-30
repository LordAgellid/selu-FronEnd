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

        // Flèche de retour vers la page de connexion
        val flecheDeRetour3 = findViewById<ImageView>(R.id.flecheDeRetour3)
        flecheDeRetour3.setOnClickListener {
            retourPrecedentePage()
        }
        //Regex pour le mot de passe (8 caractères, 1 majuscule, 1 minuscule, 1 chiffre)
        val regexMDP = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}\$")


        val nouveauMotDePasseInput1 = this.findViewById<EditText>(R.id.nv_motDePasse_input1)
        val nouveauMotDePasseInput2 = this.findViewById<EditText>(R.id.nv_MotDePasse_input2)
        val courriel = (this.intent.getStringExtra("Courriel"))

        //Variable de validation
        var valide : Boolean = false

        //Lien de redirection vers la vérification de code
        val btnReinitialiserMotDePasse = findViewById<TextView>(R.id.btn_reinitialiserMotDePasse)
        btnReinitialiserMotDePasse.setOnClickListener {
            //Messages d'erreurs
            if (nouveauMotDePasseInput1.text.isEmpty() || nouveauMotDePasseInput2.text.isEmpty()) {
                nouveauMotDePasseInput1.error = "Veuillez entrer votre mot de passe"
                nouveauMotDePasseInput2.error = "Veuillez confirmer votre mot de passe"
                valide = false
                println("1")
            } else {
                if (nouveauMotDePasseInput1.text.toString() != nouveauMotDePasseInput2.text.toString())
                {
                    nouveauMotDePasseInput1.error = "Les mots de passe ne correspondent pas"
                    nouveauMotDePasseInput2.error = "Les mots de passe ne correspondent pas"
                    valide = false
                    println("2")
                } else {
                    if (regexMDP.matches(nouveauMotDePasseInput1.text)) {
                        valide = true
                        println("3")
                    } else {
                        nouveauMotDePasseInput1.error = "Veuillez entrer un mot de passe valide, il doit contenir au moins 8 caractères, 1 majuscule, 1 minuscule et 1 chiffre"
                        valide = false
                        println("4")
                    }
                }
            }

            //Validation & Envoi
            if (courriel != null && nouveauMotDePasseInput1.text == nouveauMotDePasseInput2.text) {
                    println("5")
                if(valide) {
                    reinitialiserMotDePasse(nouveauMotDePasseInput2.text.toString(), courriel)
                    println("6")
                }
            }
        }
    }

    private fun retourPrecedentePage() {
        val intent = Intent(this, Page2VerificationCode::class.java)
        startActivity(intent)
    }

    private fun reinitialiserMotDePasse(nouveauMotDePasse: String, courriel : String) {
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
                }
            },
            {
                println(it)
            }
        )
        queue.add(r)
    }

}