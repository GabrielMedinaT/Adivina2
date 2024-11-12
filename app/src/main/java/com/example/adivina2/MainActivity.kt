package com.example.adivina2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.adivina2.ui.theme.Adivina2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Adivina2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Adivinanza2()
                }
            }
        }
    }
}
@Composable
fun Adivinanza2() {
    val originalNumbers = generateSudoku()  // Genera un tablero de Sudoku válido
    val editableNumbers = originalNumbers.map { if (it == 0) 1 else it }.toMutableStateList()
    val isEditable = originalNumbers.map { it == 0 }.toMutableStateList()  // Solo los ceros son editables
    var isSolved by remember { mutableStateOf(false) }
    var attempts by remember { mutableStateOf(0) }

    fun resetGame() {
        val newBoard = generateSudoku()
        for (i in originalNumbers.indices) {
            originalNumbers[i] = newBoard[i]
            editableNumbers[i] = if (newBoard[i] == 0) 1 else newBoard[i]
            isEditable[i] = (newBoard[i] == 0)
        }
        isSolved = false
        attempts = 0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(17.dp)
    ) {
        GameTitle()
        SudokuGrid(editableNumbers, isEditable)
        ValidateButton(originalNumbers, editableNumbers, isEditable, attempts) { isSolved = it }
        if (isSolved) {
            Text("¡Ganaste! Tablero completo.", color = Color.Green, style = MaterialTheme.typography.bodyMedium)
        }
        Text("Intentos: $attempts", style = MaterialTheme.typography.bodyMedium)
        Button(onClick = { resetGame() }, modifier = Modifier.padding(top = 20.dp)) {
            Text("Reiniciar Juego")
        }
        Debug(originalNumbers)
    }
}

@Composable
fun SudokuGrid(numbers: MutableList<Int>, isEditable: List<Boolean>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        for (i in 0 until 9) {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                for (j in 0 until 9) {
                    val index = i * 9 + j
                    SudokuCell(
                        number = numbers[index],
                        isCorrect = isEditable[index],
                        isEditable = isEditable[index],
                        onClick = {
                            if (isEditable[index]) {
                                numbers[index] = (numbers[index] % 9) + 1
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SudokuCell(number: Int, isCorrect: Boolean, isEditable: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .border(width = 1.dp, color = if (isEditable) Color.Gray else Color.Black, shape = RoundedCornerShape(4.dp))
            .clickable(enabled = isEditable) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (number == 0) "" else number.toString(),
            color = if (isEditable) Color.Blue else Color.Black,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ValidateButton(originalNumbers: List<Int>, editableNumbers: List<Int>, isEditable: List<Boolean>, attempts: Int, onSolved: (Boolean) -> Unit) {
    Button(
        onClick = {
            val isCorrect = originalNumbers.indices.all { index ->
                (originalNumbers[index] == 0 && editableNumbers[index] != 0) || (originalNumbers[index] != 0 && originalNumbers[index] == editableNumbers[index])
            }
            onSolved(isCorrect)
        },
        modifier = Modifier.padding(top = 17.dp)
    ) {
        Text("Validar")
    }
}

fun generateSudoku(): List<Int> {
    // Implementa aquí o usa una función para generar una disposición válida de Sudoku.
    return List(81) { 0 }  // Aquí puedes agregar una lista de ejemplo o un generador de Sudoku.
}

