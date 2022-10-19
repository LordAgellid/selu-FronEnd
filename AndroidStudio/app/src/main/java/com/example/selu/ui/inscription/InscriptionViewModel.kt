package com.example.selu.ui.inscription

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class InscriptionViewModel(val app: Application) : AndroidViewModel(app) {
    val IfSucesss: MutableLiveData<Boolean> = MutableLiveData()

    fun inscription(prenom: String, nom: String, courriel: String, motDePasse: String) {
        val inscriptionRepository = InscriptionRepository(getApplication())
        inscriptionRepository.inscription(prenom, nom, courriel, motDePasse, IfSucesss)
    }
}
