package com.example.taller22048game.moshi

import com.example.taller22048game.data.GameState
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiProvider {
    val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(MatrixAdapter())
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    val gameStateAdapter by lazy {
        moshi.adapter(GameState::class.java)
    }
}
