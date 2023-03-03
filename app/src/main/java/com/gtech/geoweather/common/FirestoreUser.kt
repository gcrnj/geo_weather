package com.gtech.geoweather.common

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ktx.Firebase
import com.gtech.geoweather.models.User

class FirestoreUser : FirestoreHelper<User?, Task<Void>, User> {

    private val firebaseCollection: CollectionReference = firestoreDb.collection("users")

    override suspend fun get(): User? {
        return null
    }

    override fun insert(data: User): Task<Void> {
        val id = Firebase.auth.currentUser?.uid
        return firebaseCollection.document(id.toString()).set(data)
    }


}