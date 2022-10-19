package com.example.selu.ui.mdop.page3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.selu.R
import com.example.selu.ui.mdop.page1.Mdop1Fragment
import com.example.selu.ui.mdop.page2.Mdop2Fragment

class Mdop3Fragment : Fragment(R.layout.fragment_mdop3) {

        fun retour() {
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.bay, Mdop2Fragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            view.findViewById<ImageView>(R.id.flecheDeRetour3).setOnClickListener {
                retour()
                Log.d("TAG", "go to page 2")
            }

        }
}