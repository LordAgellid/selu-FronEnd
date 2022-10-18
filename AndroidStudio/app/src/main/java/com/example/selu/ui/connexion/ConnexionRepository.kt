package com.example.selu.ui.connexion

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.selu.MainActivity
import org.json.JSONObject

class ConnexionRepository(private val application: Application) {
    private val messageSuccess = "Connexion réussi !"
    private val messageEchec = "ERREUR: Connexion échouée. Veuillez vérifier votre adresse courriel & mot de passe!"

    fun connexionRequest(courriel :String, motDePasse : String, isValid: MutableLiveData<Boolean>) {
        val url ="${MainActivity.API_URL}/connexion"
        val queue = Volley.newRequestQueue(application)
        val body = JSONObject()
        body.put("Courriel", courriel)
        body.put("MotDePasse", motDePasse)

        val postRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            body,
            {
                if(it.getBoolean("success")) {
                    println(it)
                    isValid.value = true
                    MainActivity.TOKEN = it.getString("access_token")
                    println(messageSuccess)
                }
            },
            {
                isValid.value = false
                println(it)
                Toast.makeText(application, messageEchec, Toast.LENGTH_LONG).show()
            }
        )
        queue.add(postRequest)
    }

}