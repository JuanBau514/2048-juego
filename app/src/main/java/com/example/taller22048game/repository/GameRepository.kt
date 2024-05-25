package com.example.taller22048game.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.taller22048game.data.GameState
import com.example.taller22048game.moshi.MoshiProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("tzfe")

class GameRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val gameStateKey = stringPreferencesKey("gameState")

    suspend fun getSavedGameState(): GameState? = withContext(Dispatchers.IO) {
        val gameStateFlow = context.dataStore.data.map {  preferences ->
            preferences[gameStateKey]?.let { MoshiProvider.gameStateAdapter.fromJson(it) }
        }
        gameStateFlow.firstOrNull()
    }

    suspend fun saveGameState(gameState: GameState) = withContext(Dispatchers.IO) {
        context.dataStore.edit { preferences ->
            preferences[gameStateKey] = MoshiProvider.gameStateAdapter.toJson(gameState)
        }
    }

}
