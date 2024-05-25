package com.example.taller22048game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.taller22048game.ui.game.Game
import com.example.taller22048game.ui.theme.TZFETheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TZFETheme {
                Game()
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TZFETheme {
        Game()
    }
}
