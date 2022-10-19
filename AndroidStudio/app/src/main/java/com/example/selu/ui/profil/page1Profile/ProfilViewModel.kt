package com.example.selu.ui.profil.page1Profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.selu.models.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfilViewModel (app: Application, courriel: String) : AndroidViewModel(app) {
    var profile: MutableLiveData<Profile> = MutableLiveData()

    fun modRefresh(courriel: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val profilRepository = ProfilRepository(getApplication())
            profilRepository.getProfile(courriel, profile)
        }
    }
    fun modifierProfile(prenom: String, nom: String, courriel: String, image: String) {
        val profilRepository = ProfilRepository(getApplication())
        profilRepository.editProfile(prenom, nom, courriel, image)
    }
}