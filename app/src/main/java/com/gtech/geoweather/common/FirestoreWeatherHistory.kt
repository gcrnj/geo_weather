package com.gtech.geoweather.common

import android.util.Log
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
                    it.getDouble("temperature") ?: 0.0,
                    it.getDouble("tem_min") ?: 0.0,
                    it.getDouble("temp_max") ?: 0.0,
                    it.getString("description") ?: "",
                    it.getString("icon") ?: "",
                    it.getDouble("windSpeed") ?: 0.0,
                    it.get("timezone").toString().toIntOrNull() ?: 0,
                    it.getString("country") ?: "",
                    it.getString("cityName") ?: "",
                    it.get("sunrise").toString().toIntOrNull() ?: 0,
                    it.get("sunset").toString().toIntOrNull() ?: 0,
                    it.get("dateTime").toString().toIntOrNull() ?: 0,
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