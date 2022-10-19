package com.example.selu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.selu.ui.mdop.page1.Mdop1Fragment

class MdpOublier : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mdp_main)
        //Cacher l'action bar
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        val firstFragment = Mdop1Fragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.bay, firstFragment)
            commit()
        }


    }
}
