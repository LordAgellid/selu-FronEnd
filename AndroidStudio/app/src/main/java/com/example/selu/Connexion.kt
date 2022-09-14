package com.example.selu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class Connexion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        Thread.sleep(2500)
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.connexion)
    }
}