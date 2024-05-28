package com.example.taller22048game.data

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class GlobalState(
    var highScore: Int
){
    constructor() : this(0)
}
