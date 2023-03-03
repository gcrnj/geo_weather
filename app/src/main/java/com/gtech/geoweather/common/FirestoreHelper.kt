package com.gtech.geoweather.common

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


interface FirestoreHelper<G, I, ID> { //Get, Insert, InsertData

    suspend fun get(): G?

    fun insert(data: ID): I

    val firestoreDb: FirebaseFirestore
        get() = Firebase.firestore
}