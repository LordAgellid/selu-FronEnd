package com.example.selu.ui.connexion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConnexionViewModel(val app: Application) : AndroidViewModel(app) {
    lateinit var courriel : String
    lateinit var motDePasse : String
    var isValid = MutableLiveData<Boolean>(null)

    fun connexion() {
        viewModelScope.launch(Dispatchers.IO) {
            val connexionRepository = ConnexionRepository(getApplication())
            connexionRepository.connexionRequest(courriel, motDePasse, isValid)
        }
    }
}