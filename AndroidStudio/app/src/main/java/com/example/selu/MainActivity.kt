package com.example.selu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_favorites,
                R.id.navigation_add,
                R.id.navigation_msg_notifs,
                R.id.navigation_user_profile
            )
        )
        this.setupActionBarWithNavController(navController, appBarConfiguration)

        val navView: BottomNavigationView = this.findViewById(R.id.nav_view)
        navView.setupWithNavController(navController)
    }

    companion object {
        //Variable du token de départ
        var TOKEN = ""

        var VALIDE = false

        const val API_URL = "https://seluapi.herokuapp.com"

        var EMAIL = ""

        //Regex pour le nom & le prénom
        val REGEX_NOM_PRENOM = Regex("^[A-Za-z]+$")

        //Regex pour l'adresse courriel
        val REGEX_COURRIEL = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")

        //Regex pour le mot de passe (8 caractères, 1 majuscule, 1 minuscule, 1 chiffre)
        val REGEX_MOT_DE_PASSE = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{8,}\$")
    }
}