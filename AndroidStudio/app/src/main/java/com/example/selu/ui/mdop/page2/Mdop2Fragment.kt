package com.example.selu.ui.mdop.page2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.selu.R
import com.example.selu.ui.mdop.page1.Mdop1Fragment
import com.example.selu.ui.mdop.page3.Mdop3Fragment

class Mdop2Fragment : Fragment(R.layout.fragment_mdop2) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fun onClick(view: View) {
            val thirdFragment = Mdop3Fragment()
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.mdpFrame, thirdFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        fun retour() {
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.mdpFrame, Mdop1Fragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btn_verificationCode).setOnClickListener {
            onClick(view)
            Log.d("TAG", "go to page 3")
        }

        view.findViewById<ImageView>(R.id.flecheDeRetour2).setOnClickListener {
            retour()
            Log.d("TAG", "go to page 1")
        }
    }
}