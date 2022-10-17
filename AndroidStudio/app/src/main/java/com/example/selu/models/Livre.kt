package com.example.selu.models

data class Livre (
    val id: Int,
    val titre: String,
    val descriptionLivre: String,
    val nbPages: Int,
    val prix: Float,
    val coursId: Int,
    val collectionId: Int,
    val maisonEditionId: Int,
    val datePublication: String,
    val photoId: String
)