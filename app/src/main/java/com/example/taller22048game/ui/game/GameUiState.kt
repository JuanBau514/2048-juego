package com.example.taller22048game.ui.game

import com.example.taller22048game.data.Matrix


/**
 * Created by Ronak Harkhani on 29/03/24
 */
data class GameUiState(
    val score: Int,
    val highScore: Int,
    val board: Matrix,
)
