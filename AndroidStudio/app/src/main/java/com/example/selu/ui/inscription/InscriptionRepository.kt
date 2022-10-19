package com.example.selu.ui.inscription

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.selu.MainActivity
import org.json.JSONObject

class InscriptionRepository (private val application: Application) {
    fun inscription(prenom: String, nom: String, courriel: String, motDePasse: String, IfSucesss: MutableLiveData<Boolean>) {
        val queue = Volley.newRequestQueue(application)
        val url = MainActivity.API_URL + "/utilisateurs/inscription"

        val params = JSONObject()
        params.put("Prenom", prenom.trim())
        params.put("NomDeFamille", nom.trim())
        params.put("Courriel", courriel.trim())
        params.put("MotDePasse", motDePasse.trim())

        val r = object : JsonObjectRequest(
            Request.Method.POST,
            url,
            params,
            {
                Log.d("InscriptionRepository", it.toString())
                IfSucesss.value = true
            },
            {
                println(it)
                Log.e("ERREUR", it.message.toString())
                IfSucesss.value = false
            })
        {
        }
          queue.add(r)
        }
}