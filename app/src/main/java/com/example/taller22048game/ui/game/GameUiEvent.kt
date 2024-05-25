package com.example.taller22048game.ui.game


sealed interface GameUiEvent {

    data object Reset : GameUiEvent

    data object Undo: GameUiEvent

    data class Push(val offsetX: Float, val offsetY: Float): GameUiEvent

}
