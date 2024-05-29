package com.example.taller22048game.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taller22048game.data.Matrix
import com.example.taller22048game.ui.board.Board
import com.example.taller22048game.ui.theme.Colors
import com.example.taller22048game.ui.theme.TZFETheme


var isWin = false
@Composable
fun Game(vm: GameVM = hiltViewModel()) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    Game(
        uiState = uiState,
        onEvent = { vm.onEvent(it) }
    )
}


@Composable
fun Game(
    uiState: GameUiState,
    onEvent: (GameUiEvent) -> Unit,
) {

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Surface(
        color = Colors.GameBackground,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()

                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    },
                    onDragEnd = {
                        onEvent(GameUiEvent.Push(offsetX, offsetY))
                        offsetX = 0f
                        offsetY = 0f
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            HeaderView(
                score = uiState.score,
                highScore = uiState.highScore,
                onUndoClick = { onEvent(GameUiEvent.Undo) },
                onResetClick = { onEvent(GameUiEvent.Reset) },
            )

            Board(matrix = uiState.board)
        }
    }
}

@Composable
fun HeaderView(
    score: Int,
    highScore: Int,
    onUndoClick: () -> Unit,
    onResetClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "2048",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.dark
            )

            Spacer(modifier = Modifier.weight(1f))

            ScoreView("SCORE", score)
            Spacer(modifier = Modifier.width(4.dp))
            ScoreView("HIGH SCORE", highScore)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Join the numbers",
                fontSize = 14.sp,
                color = Colors.dark
            )

            Row {
                ButtonView(btnText = "UNDO") { onUndoClick() }
                Spacer(modifier = Modifier.width(4.dp))
                ButtonView(btnText = "RESET") { onResetClick() }
            }
        }
        if(isWin){
            WinGameDialog(onDismiss = { onResetClick()} , score)
            isWin = false
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ButtonView(btnText: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Colors.BoardBackground)
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = btnText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.light
        )
    }
}

@Composable
fun ScoreView(scoreText: String, scoreValue: Int) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Colors.BoardBackground)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = scoreText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(255, 255, 255, 200)
        )

        Text(
            text = scoreValue.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.light
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TZFETheme {
        Game(
            uiState = GameUiState(
                score = 120,
                highScore = 3245,
                board = Matrix.emptyMatrix()
            ),
            onEvent = {}
        )
    }
}

@Composable
fun WinGameDialog(onDismiss: () -> Unit, score: Int) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        title = { Text("¡Felicidades!") },
        text = { Text("¡Llegaste al 2048, haz ganado el juego!\n ¡Tu puntuación es: $score!") }
    )
}
