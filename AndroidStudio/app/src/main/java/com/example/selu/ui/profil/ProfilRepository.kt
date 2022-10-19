package com.example.selu.ui.profil

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.selu.MainActivity
import com.example.selu.models.Profile
import com.google.gson.Gson
import org.json.JSONObject

class ProfilRepository  (private val application: Application){
    fun getProfile(courriel: String, infoprofile: MutableLiveData<Profile>) {
        val queue = Volley.newRequestQueue(application)
        val r = StringRequest(
            Request.Method.GET,
            "${MainActivity.API_URL}/profil/${courriel}",
            {
                println(it)
                println(Gson().fromJson(it, Profile::class.java))
//                val profilelist: List<Profile> = Gson().fromJson(it, Array<Profile>::class.java).toList()
                infoprofile.postValue(Gson().fromJson(it, Profile::class.java))
            },
            {
                println(it)
            })
        queue.add(r)
    }
    fun editProfile(prenom: String, nom: String, courriel: String, image: String) {
        val url = "https://seluapi.herokuapp.com/profil/modifier-profil"
        val queue = Volley.newRequestQueue(application)
        val body = JSONObject()

        body.put("Prenom", prenom)
        body.put("NomDeFamille", nom)
        body.put("Courriel", courriel)
        body.put("Image", image)

        val request = JsonObjectRequest(
            Request.Method.POST, url, body,
            { response ->
                Toast.makeText(application, "success de modification", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(application, "erreur de modification", Toast.LENGTH_SHORT).show()
                Log.d("Erreur de modification", error.toString())
            }
        )
        queue.add(request)

    }
}