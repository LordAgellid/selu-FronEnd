package com.example.selu.ui.profil.page2Profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.selu.MainActivity
import com.example.selu.R
import com.example.selu.ui.profil.page1Profile.ProfilFragment
import com.example.selu.ui.profil.page1Profile.ProfilViewModel
import com.example.selu.ui.profil.page1Profile.ProfilViewModelFactory
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.InputStream
import java.util.*


class Profil2Fragment : Fragment() {

    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var profilViewModel: ProfilViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        var valide : Boolean

        profilViewModel = ViewModelProvider(
            this, ProfilViewModelFactory(
                this.requireActivity()
                    .application, MainActivity.EMAIL
            )
        )
            .get(ProfilViewModel::class.java)


        //get les inputs
        val nom = view.findViewById<EditText>(R.id.nom_input)
        val prenom = view.findViewById<EditText>(R.id.prenom_input)
        val email = view.findViewById<EditText>(R.id.courriel_input)
        val image = view.findViewById<ImageView>(R.id.image_profil)

        // pour refresh la data
        this.profilViewModel.modRefresh(MainActivity.EMAIL)

        //set les inputs
        profilViewModel.profile.observe(viewLifecycleOwner) {
            Log.d("Profil2Fragment", "onViewCreated: ${it}")
            nom.setText(it.NomDeFamille)
            prenom.setText(it.Prenom)
            email.setText(MainActivity.EMAIL)
            Picasso.get().load(it.PhotoDeProfil).into(image)
        }

        //get le btn upload
        val btnUpload = view.findViewById<ImageView>(R.id.uploadImg)

        //set le btn upload
        btnUpload.setOnClickListener {
            chooseImageFromGallery()
        }

        //get le btn save
        val btnSave = view.findViewById<Button>(R.id.btn_modifier)

        //get quitter
        val btnQuitter = view.findViewById<ImageView>(R.id.quitter)

        //set le btn quitter
        btnQuitter.setOnClickListener {
            val profil = ProfilFragment()
            view.findNavController().navigate(R.id.navigation_user_profile)
        }

        //set le btn save
        btnSave.setOnClickListener {
            //get les inputs
            val nomInput = view.findViewById<EditText>(R.id.nom_input)
            val prenomInput = view.findViewById<EditText>(R.id.prenom_input)
            val courrielInput = view.findViewById<EditText>(R.id.courriel_input)

            //Messages d'erreurs
            // -> Prénom
            if (prenomInput.text.isEmpty() ){
                prenomInput.error = "Veuillez entrer votre prénom"
                valide = false
                return@setOnClickListener

            }  else if (MainActivity.REGEX_NOM_PRENOM.matches(prenomInput.text)) {
                valide = true
            } else {
                prenomInput.error = "Veuillez entrer un prénom valide, il ne doit contenir que des lettres"
                valide = false
                return@setOnClickListener
            }
            // -> Nom
            if (nomInput.text.isEmpty()) {
                nomInput.error = "Veuillez entrer votre nom"
                valide = false
                return@setOnClickListener

            } else if (MainActivity.REGEX_NOM_PRENOM.matches(nomInput.text)) {
                valide = true
            } else {
                nomInput.error = "Veuillez entrer un nom valide, il ne doit contenir que des lettres"
                valide = false
                return@setOnClickListener

            }
            // -> Courriel
            if (courrielInput.text.isEmpty()) {
                courrielInput.error = "Veuillez entrer votre courriel"
                valide = false
                return@setOnClickListener

            }else if (MainActivity.REGEX_COURRIEL.matches(courrielInput.text)) {
                valide = true
            } else {
                courrielInput.error = "Veuillez entrer un courriel valide , il doit contenir un @ et un ."
                valide = false
                return@setOnClickListener

            }


            Log.d("Profil2Fragment", "onViewCreated: ${valide}")


            //Validation & connexion
            if (valide) {

                //upload de l'image sur firebase
                println(filePath)
                if (filePath.toString() == "" || filePath == null) {
                    val ref2 = storageReference!!
                        .child("Utilisateurs")
                        .child(courrielInput.text.toString())
                        .child("imageProfile")
                    //montrer l'imageProfile de tout les utilisateurs

                    ref2.listAll()
                        .addOnSuccessListener { listResult ->
                            for (item in listResult.items) {
                                // get the url of the image
                                item.downloadUrl.addOnSuccessListener {
                                    // use the url to load the image
                                    this.profilViewModel.modifierProfile(
                                        prenomInput.text.toString(),
                                        nomInput.text.toString(),
                                        courrielInput.text.toString(),
                                        it.toString()
                                    )
                                }
                            }
                        }
                        .addOnFailureListener {
                            Log.d("Erreur", "Erreur lors de la suppression des images")
                        }
                } else {
                    val ref = storageReference!!
                        .child("Utilisateurs")
                        .child(courrielInput.text.toString())
                        .child("imageProfile")
                        .child(UUID.randomUUID().toString())

                    val ref2 = storageReference!!
                        .child("Utilisateurs")
                        .child(courrielInput.text.toString())
                        .child("imageProfile")
                    //montrer l'imageProfile de tout les utilisateurs
                    ref2.listAll()
                        .addOnSuccessListener { listResult ->
                            for (item in listResult.items) {

                                item.delete()
                                Log.d("TAG", "onSuccess: deleted file")
                            }
                        }
                        .addOnFailureListener {
                            Log.d("Erreur", "Erreur lors de la suppression des images")
                        }


                    ref.putFile(filePath!!)
                        .addOnSuccessListener {
                            Toast.makeText(this.context, "Uploaded", Toast.LENGTH_SHORT).show()
                            Log.d("Upload", it.toString())
                            ref.downloadUrl.addOnSuccessListener {
                                this.profilViewModel.modifierProfile(
                                    prenomInput.text.toString(),
                                    nomInput.text.toString(),
                                    courrielInput.text.toString(),
                                    it.toString()
                                )
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this.context, "Failed " + it.message, Toast.LENGTH_SHORT).show()
                        }
                        .addOnProgressListener { taskSnapshot ->
                            val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                            Toast.makeText(this.context, "Uploaded $progress %...", Toast.LENGTH_SHORT).show()
                        }

                }

            }


        }

    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val imageUri = data?.data
            val image = view?.findViewById<ImageView>(R.id.image_profil)
            image?.setImageURI(imageUri)
            filePath = imageUri
        } else {
            Toast.makeText(this.context, "Erreur", Toast.LENGTH_SHORT).show()
        }


    }








}