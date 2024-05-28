package com.example.taller22048game.data


data class Matrix(
    val base: MutableList<MutableList<Tile>>
) : MutableList<MutableList<Tile>> by base {

    companion object {
        fun emptyMatrix(size: Int = 4): Matrix {
            return Matrix(MutableList(size) { MutableList(size) { Tile.EMPTY } })
        }

        fun fromFirestore(data: List<List<Map<String, Any>>>): Matrix {
            val base = data.map { row ->
                row.map { col ->
                    Tile(value = (col["value"] as Long).toInt()) // Asegúrate de convertir Long a Int si es necesario
                }.toMutableList()
            }.toMutableList()
            return Matrix(base)
        }
    }

    val rows: Int
        get() = size

    val cols: Int
        get() = first().size

    fun flipHorizontally() {
        this.forEach {
            it.reverse()
        }
    }

    fun transpose() {
        for (row in 0 until size) {
            for (col in row+1 until size) {
                this[row][col] = this[col][row].also {
                    this[col][row] = this[row][col]
                }
            }
        }
    }

    inline fun iterateIndexed(block: (rowIndex: Int, colIndex: Int, tile: Tile) -> Unit) {
        this.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, tile ->
                block(rowIndex, colIndex, tile)
            }
        }
    }

    fun copy(): Matrix {
        return Matrix(
            MutableList(size) { row ->
                MutableList(size) { col ->
                    this[row][col]
                }
            }
        )
    }

    fun toFirestore(): List<List<Map<String, Any>>> {
        return this.map { row ->
            row.map { tile ->
                mapOf("value" to tile.value)
            }
        }
    }
}

