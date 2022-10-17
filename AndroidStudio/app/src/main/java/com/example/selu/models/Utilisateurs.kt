package com.example.selu.models

data class Utilisateurs(
    val id: Int,
    val prenom : String,
    val nomDeFamille : String,
    val courriel : String,
    val motDePasse : String,
    val dateDeCreation : String,
    val derniereConnexion : String,
    val photoProfil : String
)