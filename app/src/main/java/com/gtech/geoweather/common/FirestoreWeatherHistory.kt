package com.gtech.geoweather.common

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.gtech.geoweather.models.WeatherDatabaseData
import kotlinx.coroutines.tasks.await

class FirestoreWeatherHistory(
    userDocumentId: String?,
) :
    FirestoreHelper<List<WeatherDatabaseData?>, Task<DocumentReference>?, WeatherDatabaseData> {

    private val firebaseCollection: CollectionReference =
        firestoreDb.collection("users").document(userDocumentId.toString()).collection("history")

    override suspend fun get(): List<WeatherDatabaseData?>? {
        val documents = firebaseCollection.get().await().documents

        return try {
            documents.map {
                WeatherDatabaseData(
                    it.get("temperature", Double::class.java) ?: 0.0,
                    it.get("tem_min", Double::class.java) ?: 0.0,
                    it.get("temp_max", Double::class.java) ?: 0.0,
                    it.getString("description") ?: "",
                    it.getString("icon") ?: "",
                    it.get("windSpeed", Double::class.java) ?: 0.0,
                    it.get("timezone", Int::class.java) ?: 0,
                    it.getString("country") ?: "",
                    it.getString("cityName") ?: "",
                    it.get("sunrise", Int::class.java) ?: 0,
                    it.get("sunset", Int::class.java) ?: 0,
                    it.get("dateTime", Int::class.java) ?: 0,
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun insert(data: WeatherDatabaseData): Task<DocumentReference> {
        return firebaseCollection.add(data)
    }

}