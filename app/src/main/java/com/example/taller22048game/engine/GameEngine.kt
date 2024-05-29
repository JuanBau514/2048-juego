package com.example.taller22048game.engine

import com.example.taller22048game.data.Direction
import com.example.taller22048game.data.GameState
import com.example.taller22048game.data.GlobalState
import com.example.taller22048game.data.Matrix
import com.example.taller22048game.data.Tile
import com.example.taller22048game.ui.game.isWin
import javax.inject.Inject

class GameEngine @Inject constructor() {

    lateinit var gameState: GameState
    lateinit var globalState: GlobalState

    var score: Int
        get() = gameState.score
        private set(value) { gameState.score = value }

    var highScore: Int
        get() = globalState.highScore
        private set(value) { globalState.highScore = value }

    var board: Matrix
        get() = gameState.board
        private set(value) { gameState.board = value }

    private var moveScore = 0
    private var anyMoved = false
    private var anyCombined = false

    fun init(savedGameState: GameState?) {
        if (savedGameState != null) {
            gameState = savedGameState
        } else {
            gameState = GameState(
                board = Matrix.emptyMatrix(),
                prevBoard = Matrix.emptyMatrix(),
                score = 0
            )
            resetBoard()
        }
    }

    fun initGlobal(savedGlobalState: GlobalState?) {
        if (savedGlobalState != null) {
            globalState = savedGlobalState
        } else {
            globalState = GlobalState(
                highScore = 0
            )
        }
    }

    fun resetBoard() {
        board = Matrix.emptyMatrix()
        repeat(2) { addTile() }
        gameState.prevBoard = board.copy()
        score = 0
    }

    fun undoMove() {
        board = gameState.prevBoard.copy()
        score -= moveScore
        moveScore = 0
    }

    fun push(dir: Direction) {
        initNewMove()
        val tempBoard = board.copy()
        when (dir) {
            Direction.Left -> pushLeft()
            Direction.Right -> pushRight()
            Direction.Up -> pushUp()
            Direction.Down -> pushDown()
        }

        updateScoreBy(moveScore)
        if (anyMoved || anyCombined) {
            addTile()
            gameState.prevBoard = tempBoard
        }
    }

    private fun updateScoreBy(points: Int) {
        score += points
        if (score > highScore) {
            highScore = score
        }
    }

    private fun addTile() {
        val (row, col) = getEmptyPositions().random()
        board[row][col] = Tile.twoOrFour()
    }

    private fun getEmptyPositions(): List<Pair<Int, Int>> {
        return board.flatMapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, tile ->
                if (tile.isEmpty) Pair(rowIndex, colIndex) else null
            }
        }
    }

    private fun initNewMove() {
        anyMoved = false
        anyCombined = false
        moveScore = 0
    }

    private fun pushLeft() {
        slide()
        combine()
        slide()
    }

    private fun pushRight() {
        board.flipHorizontally()
        pushLeft()
        board.flipHorizontally()
    }

    private fun pushUp() {
        board.transpose()
        pushLeft()
        board.transpose()
    }

    private fun pushDown() {
        board.transpose()
        board.flipHorizontally()
        pushLeft()
        board.flipHorizontally()
        board.transpose()
    }

    private fun slide() {
        board.forEach { row ->
            val newRow = row.filter { !it.isEmpty } + row.filter { it.isEmpty }
            newRow.zip(row).forEach {
                if (it.first.value != it.second.value) anyMoved = true
            }
            for (col in 0 until row.size) {
                row[col] = newRow[col]
            }
        }
    }

    private fun combine() {
        for (row in board) {
            for (col in 0 until row.size - 1) {
                if (Tile.canCombine(row[col], row[col + 1])) {
                    row[col] = Tile.combine(row[col], row[col + 1])
                    row[col + 1] = Tile.EMPTY
                    moveScore += row[col].value
                    if(row[col].value==2048){
                        isWin = true
                    }
                    anyCombined = true
                }
            }
        }
    }
}
