package com.example.selu.ui.mdop.page1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.selu.R
import com.example.selu.ui.mdop.page2.Mdop2Fragment

class Mdop1Fragment : Fragment(R.layout.fragment_mdop1) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fun onClick(view: View) {
            val secondFragment = Mdop2Fragment()
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.mdpFrame, secondFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        fun retour() {
            val intent = Intent(this.context, com.example.selu.ui.connexion.ConnexionActivity::class.java)
            startActivity(intent)
        }
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btn_envoiCodeVerification).setOnClickListener {
            onClick(view)
            Log.d("TAG", "go to page 2")
        }

        view.findViewById<ImageView>(R.id.flecheDeRetour1).setOnClickListener {
            retour()
            Log.d("TAG", "go to connexion")
        }
    }

}