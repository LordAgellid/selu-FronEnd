package com.example.selu.ui.profil.page1Profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("JvmDefault")
class ProfilViewModelFactory (val application: Application, private val courriel: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfilViewModel(this.application, this.courriel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}