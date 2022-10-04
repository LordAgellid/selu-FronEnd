package com.example.selu

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.selu.databinding.ConnexionBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.json.JSONObject

class Connexion : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ConnexionBinding
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ConnexionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Regex pour le courriel
        val regexEmail = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")
        //Regex pour le mot de passe (8 caractères, 1 majuscule, 1 minuscule, 1 chiffre)
        val regexMotDePasse = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}\$")

        //Variable de validation
        var valide: Boolean

        //Valeurs d'entrée
        val courrielInput = binding.courrielInput
        val motDePasseInput = binding.motDePasseInput

        //Connexion via Google
        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("427619450967-0fmipg0o93anma4it0pq7uo8gsg4uoq7.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnConnexionGoogle.setOnClickListener {
            connexionGoogle()
        }


        //Lien de redirection vers la page de mot de passe oublié
        val lienVersMdpOublie = binding.lienVersMdpOublie
        lienVersMdpOublie.setOnClickListener {
            redirectToMotDePasseOublie()
        }

        //Connexion au profil
        val btnConnexion = binding.btnConnexion
        btnConnexion.setOnClickListener {
            //Messages d'erreurs
            // -> Courriel
            if (courrielInput.text.isEmpty()) {
                courrielInput.error = "Veuillez entrer votre courriel"
                valide = false
            } else if (regexEmail.matches(courrielInput.text.toString())) {
                    valide = true
            } else {
                courrielInput.error = "Veuillez entrer un courriel valide, il doit contenir un @ & un ."
                valide = false
            }
            // -> Mot de passe
            if (motDePasseInput.text.isEmpty()) {
                motDePasseInput.error = "Veuillez entrer votre mot de passe"
                valide = false
            } else if (regexMotDePasse.matches(motDePasseInput.text)) {
                    valide = true
            } else {
                motDePasseInput.error = "Veuillez entrer un mot de passe valide, il doit contenir au moins 8 caractères, 1 majuscule, 1 minuscule et 1 chiffre"
                valide = false
            }

            //Validation & connexion
            if (valide) {
                connexion(
                    courrielInput.text.toString(),
                    motDePasseInput.text.toString()
                )
            }
        }

        //Lien de redirection vers la page d'inscription
        val lienVersInscription = binding.lienVersInscription
        lienVersInscription.setOnClickListener {
            redirectToInscription()
        }
    }

    private fun connexionGoogle() {
        val singInIntent = googleSignInClient.signInIntent
        startActivityForResult(singInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                println(account)
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
            println(task)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(applicationContext, PageProfil::class.java)
            intent.putExtra(EXTRA_NAME, user.displayName)
            startActivity(intent)
        }
    }

    private fun redirectToMotDePasseOublie() {
        val intent = Intent(this, Page1EnvoiCodeDeVerification::class.java)
        startActivity(intent)
    }

    private fun connexion(courriel :String, motDePasse : String) {
        val messageSuccess = "Connexion réussi !"
        val messageEchec = "ERREUR: Connexion échouée. Veuillez  !"

        val url = "http://10.0.2.2:3000/connexion"
        val queue = Volley.newRequestQueue(this)
        val body = JSONObject()
        body.put("Courriel", courriel)
        body.put("MotDePasse", motDePasse)

        val postRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            body,
            {
                println(it)
                if(it.getBoolean("success")) {
                    val intent = Intent(this, PageMonProfil::class.java)
                    val token = it.getString("access_token")

                    intent.putExtra("Courriel", courriel)
                    intent.putExtra("Token", token)

                    this.startActivity(intent)
                    Toast.makeText(this, messageSuccess, Toast.LENGTH_LONG).show()
                }
            },
            {
                println(it)
                Toast.makeText(this, messageEchec, Toast.LENGTH_LONG).show()
            }
        )
        queue.add(postRequest)
    }

    private fun redirectToInscription() {
        val intent = Intent(this, Inscription::class.java)
        startActivity(intent)
    }

    companion object {
        const val RC_SIGN_IN = 1001
        const val EXTRA_NAME = "EXTRA NAME"
    }
}