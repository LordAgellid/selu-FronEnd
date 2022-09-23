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

        //Lien de redirection vers la vérification de code
        val btnReinitialiserMotDePasse = findViewById<TextView>(R.id.btn_reinitialiserMotDePasse)
        btnReinitialiserMotDePasse.setOnClickListener {
            val nouveauMotDePasseInput1 = ((findViewById<EditText>(R.id.nv_motDePasse_input1)).text).toString()
            val nouveauMotDePasseInput2 = ((findViewById<EditText>(R.id.nv_MotDePasse_input2)).text).toString()
            val courriel = (this.intent.getStringExtra("Courriel"))

            if (nouveauMotDePasseInput1 != nouveauMotDePasseInput2) {
                val Text = "Le mot de passe ne correspond pas au premier."
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, Text, duration)
                toast.show()
            } else if (courriel != null && nouveauMotDePasseInput1 == nouveauMotDePasseInput2) {
                reinitialiserMotDePasse(nouveauMotDePasseInput2, courriel)
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