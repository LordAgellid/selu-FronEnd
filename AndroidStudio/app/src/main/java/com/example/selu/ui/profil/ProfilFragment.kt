package com.example.selu.ui.profil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.selu.MainActivity
import com.example.selu.R
import com.squareup.picasso.Picasso

class ProfilFragment : Fragment() {
    private lateinit var profilViewModel: ProfilViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilViewModel = ViewModelProvider(
            this, ProfilViewModelFactory(
                this.requireActivity()
                    .application, MainActivity.EMAIL
            )
        )
            .get(ProfilViewModel::class.java)

        // get les elements du layout
        val photoProfile = view.findViewById<ImageView>(R.id.image_profil)
        val nomuser = view.findViewById<TextView>(R.id.username)
        profilViewModel.profile.observe(viewLifecycleOwner) {
            nomuser.text = "${it.Prenom} ${it.NomDeFamille}"
            Picasso.get().load(it.PhotoDeProfil).into(photoProfile)
        }
    }
}