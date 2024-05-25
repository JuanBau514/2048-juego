package com.example.taller22048game.moshi

import com.example.taller22048game.data.Matrix
import com.example.taller22048game.data.Tile
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class MatrixAdapter {

    @FromJson
    fun fromJson(base: MutableList<MutableList<Tile>>): Matrix = Matrix(base)

    @ToJson
    fun toJson(matrix: Matrix): MutableList<MutableList<Tile>> = matrix.base
}
