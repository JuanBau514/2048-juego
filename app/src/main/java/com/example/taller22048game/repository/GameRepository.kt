package com.example.taller22048game.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.taller22048game.data.GameState
import com.example.taller22048game.data.GlobalState
import com.example.taller22048game.moshi.MoshiProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.google.firebase.firestore.ktx.toObject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("tzfe")

class GameRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val TAG = "MainActivity"
    private val gameStateKey = stringPreferencesKey("gameState")

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getSavedGameState(): GameState? = withContext(Dispatchers.IO) {
        val gameStateFlow = context.dataStore.data.map { preferences ->
            preferences[gameStateKey]?.let { MoshiProvider.gameStateAdapter.fromJson(it) }
        }
        gameStateFlow.firstOrNull()
    }

    suspend fun getSavedGlobalState(): GlobalState? = withContext(Dispatchers.IO) {
        val documentReference = db.collection("gameStates").document("currentGame")
        try {
            val documentSnapshot = documentReference.get().await()
            return@withContext documentSnapshot.toObject<GlobalState>()
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener el estado global", e)
            // Maneja la excepción según sea necesario
            e.printStackTrace()
            return@withContext null
        }
    }


    suspend fun saveGameState(gameState: GameState, globalState: GlobalState) = withContext(Dispatchers.IO) {
        val db = FirebaseFirestore.getInstance()

        // Convertir el objeto gameState a un mapa
        val gameStateMap = mapOf(
            "highScore" to globalState.highScore
        )

        // Crear una referencia al documento de la jugada actual dentro de la colección "gameStates"
        val currentGameDocument = db.collection("gameStates").document("currentGame")

        // Guardar el estado del juego en Firestore
        currentGameDocument
            .set(gameStateMap) // Establecer datos en el documento actual
            .addOnSuccessListener {
                Log.d(TAG, "Estado del juego actual añadido o actualizado")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error al añadir o actualizar el estado del juego actual", e)
            }

        // Almacenar el estado del juego en el DataStore
        context.dataStore.edit { preferences ->
            preferences[gameStateKey] = MoshiProvider.gameStateAdapter.toJson(gameState)
        }
    }
}
