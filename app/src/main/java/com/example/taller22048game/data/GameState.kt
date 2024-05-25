package com.example.taller22048game.data

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class GameState(
    var board: Matrix,
    var prevBoard: Matrix,
    var score: Int,
    var highScore: Int
)
