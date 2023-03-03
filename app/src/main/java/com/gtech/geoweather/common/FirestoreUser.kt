package com.gtech.geoweather.common

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ktx.Firebase
import com.gtech.geoweather.models.User
import com.gtech.geoweather.models.WeatherDatabaseData
import kotlinx.coroutines.tasks.await

class FirestoreUser(private val userDocumentId: String?) :
    FirestoreHelper<User?, Task<Void>, User> {

    private val firebaseCollection: CollectionReference = firestoreDb.collection("users")

    override suspend fun get(): User? {
        val userDocument = firebaseCollection.document(userDocumentId.toString()).get().await()

        return try {
            User(
                userDocument.get("id", Int::class.java) ?: 0,
                userDocument.getString("firstName") ?: "",
                userDocument.getString("middleName") ?: "",
                userDocument.getString("lastName") ?: "",
                userDocument.getString("email") ?: "",
                userDocument.getString("mobileNumber") ?: "",
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun insert(data: User): Task<Void> {
        val id = Firebase.auth.currentUser?.uid
        return firebaseCollection.document(id.toString()).set(data.copy(id = null))
    }


}