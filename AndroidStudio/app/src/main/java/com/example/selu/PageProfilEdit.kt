package com.example.selu

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.io.IOException
import java.util.*

class PageProfilEdit : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_edit_profil)

        var valide : Boolean


        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        //declaration des variables
        val prenomInput = findViewById<EditText>(R.id.prenom_input)
        val nomInput = findViewById<EditText>(R.id.nom_input)
        val courrielInput = findViewById<EditText>(R.id.courriel_input)
        val imageProfile = findViewById<ImageView>(R.id.image_profil)

        //declaration des boutons
        val btn_choose_image = findViewById<ImageView>(R.id.uploadImg)
        val btn_upload_all = findViewById<Button>(R.id.btn_modifier)

        //recuperation des donnees et affichage
        getProfil()


        //Regex pour le prénom
        val regexPrenom = Regex("^[A-Za-z]+$")
        //Regex pour le nom
        val regexNom = Regex("^[A-Za-z]+$")
        //Regex pour le courriel
        val regexEmail = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")


        //Bouton pour deconnexion
        val btnDeconnexion = findViewById<ImageView>(R.id.deconnexion)
        btnDeconnexion.setOnClickListener {
            // Redirection vers la page de connexion et supression des tokens
            // et supression des tokens
            val intent = Intent(this, Connexion::class.java)
            startActivity(intent)
        }

        //bouton pour choisir une image
        btn_choose_image.setOnClickListener {
            chooseImage()

        }

        //bouton pour modifier les donnees
        btn_upload_all.setOnClickListener {
            //Messages d'erreurs
            // -> Prénom
            if (prenomInput.text.isEmpty() ){
                prenomInput.error = "Veuillez entrer votre prénom"
                valide = false
            }  else if (regexPrenom.matches(prenomInput.text)) {
                valide = true
            } else {
                prenomInput.error = "Veuillez entrer un prénom valide, il ne doit contenir que des lettres"
                valide = false
            }
            // -> Nom
            if (nomInput.text.isEmpty()) {
                nomInput.error = "Veuillez entrer votre nom"
                valide = false
            } else if (regexNom.matches(nomInput.text)) {
                valide = true
            } else {
                nomInput.error = "Veuillez entrer un nom valide, il ne doit contenir que des lettres"
                valide = false
            }
            // -> Courriel
            if (courrielInput.text.isEmpty()) {
                courrielInput.error = "Veuillez entrer votre courriel"
                valide = false
            }else if (regexEmail.matches(courrielInput.text)) {
                valide = true
            } else {
                courrielInput.error = "Veuillez entrer un courriel valide , il doit contenir un @ et un ."
                valide = false
            }

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
                                    ModifierProfil(
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
                            Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
                            Log.d("Upload", it.toString())
                            ref.downloadUrl.addOnSuccessListener {
                                ModifierProfil(
                                    prenomInput.text.toString(),
                                    nomInput.text.toString(),
                                    courrielInput.text.toString(),
                                    it.toString()
                                )
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed " + it.message, Toast.LENGTH_SHORT).show()
                        }
                        .addOnProgressListener { taskSnapshot ->
                            val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                            Toast.makeText(this, "Uploaded $progress %...", Toast.LENGTH_SHORT).show()
                        }

                }

            }
        }
    }

    //Fonction pour modifier les donnees
    private fun ModifierProfil(prenom: String, nom: String, courriel: String, image: String) {
        val messageSuccess = "Modification réussie"
        val messageFail = "Modification échouée"

        val url = "http://10.0.2.2:3000/profile/Modifierprofile"
        val queue = Volley.newRequestQueue(this)
        val body = JSONObject()


        body.put("Prenom", prenom)
        body.put("NomDeFamille", nom)
        body.put("Courriel", courriel)
        body.put("Image", image)

        val request = JsonObjectRequest(
            Request.Method.POST, url, body,
            { response ->
                Toast.makeText(this, messageSuccess, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PageMonProfil::class.java)
                intent.putExtra("Courriel", courriel)
                startActivity(intent)
            },
            { error ->
                Toast.makeText(this, messageFail, Toast.LENGTH_SHORT).show()
                Log.d("Erreur", error.toString())
            }
        )
        queue.add(request)

    }

    private fun chooseImage() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){

                return
            }
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                val imageProfile = findViewById<ImageView>(R.id.image_profil)
                imageProfile.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getProfil() {
        val courriel = this.intent.getStringExtra("Courriel")

        //prendre les input du profil
        val photoProfile = this.findViewById<ImageView>(R.id.image_profil)
        val prenomInput = findViewById<EditText>(R.id.prenom_input)
        val nomInput = findViewById<EditText>(R.id.nom_input)
        val courrielInput = findViewById<EditText>(R.id.courriel_input)


        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2:3000/profile/${courriel}"
        @Suppress("RedundantSamConstructor")
        val jsonRequest = JsonArrayRequest (
            Request.Method.GET, url, null,
            Response.Listener {
                val data = it.getJSONObject(0)

                //prend les donnees de la bd
                val photoDeProfil = data.getString("PhotoDeProfil").toString()
                val nomDeFamille = data.getString("NomDeFamille").toString()
                val prenom = data.getString("Prenom").toString()

                //Affichage des donnees
                nomInput.setText(nomDeFamille)
                prenomInput.setText(prenom)
                courrielInput.setText(courriel)

                //Affichage de l'image
                if(photoDeProfil == "null" || photoDeProfil == "") {
                    println("Photo est null")
                    Picasso.get().load(R.drawable.image_profil_temp).into(photoProfile)
                } else {
                    println("Photo pas null")
                    Picasso.get().load(photoDeProfil).into(photoProfile)
                }
            },
            Response.ErrorListener {
                println(it)
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
            })
        queue.add(jsonRequest)
    }

}