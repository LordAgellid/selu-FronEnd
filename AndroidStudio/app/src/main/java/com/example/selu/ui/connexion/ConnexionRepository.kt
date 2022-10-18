package com.example.selu.ui.connexion

import android.app.Application
import com.android.volley.toolbox.Volley
import com.example.selu.MainActivity

class ConnexionRepository(private val application: Application) {
    fun connexion() {
        val queue = Volley.newRequestQueue(application)
        val url ="${MainActivity.API_URL}/auth/token"
    }

}