package com.example.adivina2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    // Números ocultos
    var HideNumbers by remember { mutableStateOf((1..9).shuffled().take(9).toMutableList()) }
    // Números mostrados en pantalla (inicialmente en 0 para que estén vacíos)
    var NumMost = remember { mutableStateListOf(*MutableList(9) { 0 }.toTypedArray()) }
    // Estado de celdas correctas (inicialmente todas en falso)
    var cc = remember { mutableStateListOf(*MutableList(9) { false }.toTypedArray()) }
    // Contador de intentos
    var contador by remember { mutableStateOf(0) }

    // Resetear los valores
    fun resetGame() {
        HideNumbers = (1..9).shuffled().take(9).toMutableList()
        NumMost.clear()
        NumMost.addAll(List(9) { 0 }) // Cambia los números mostrados a 0
        cc.clear()
        cc.addAll(List(9) { false })   // Cambia  las celdas a no acertadas
        contador = 0
    }

    // Columna principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(17.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(17.dp)
    ) {
        // Título del juego
        GameTitle()
        // Mostrar celdas de números
        Celda(NumMost, cc)
        // Botón de validación
        Validar(HideNumbers, NumMost, cc, { contador++ })
        // Resultado del juego
        Resultado(cc)
        // Contador de intentos
        Text("Intentos: $contador", style = MaterialTheme.typography.bodyMedium)
        // Botón para reiniciar el juego
        Button(
            onClick = { resetGame() },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("Reiniciar Juego")
        }
        // Mostrar el estado de depuración
        Debug(HideNumbers)
    }
}


@Composable
fun GameTitle() {
    Spacer(modifier = Modifier.size(100.dp))
    Text("Adivina dos números (1-9)",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier
            .padding(16.dp)
    )
}

@Composable
fun Celda(numerosMostrados: MutableList<Int>, celdasCorrectas: MutableList<Boolean>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        // Crear 3 filas de 3 celdas
        repeat(3) { rowIndex ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                repeat(3) { colIndex ->
                    val index = rowIndex * 3 + colIndex
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .border(
                                width = 2.dp,
                                color = if (celdasCorrectas.getOrElse(index) { false }) Color.Green
                                else MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                if (!celdasCorrectas.getOrElse(index) { false }) {
                                    numerosMostrados[index] = (numerosMostrados[index] % 9) + 1
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (celdasCorrectas.getOrElse(index) { false }) numerosMostrados[index].toString()
                            else if (numerosMostrados[index] == 0) ""
                            else numerosMostrados[index].toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Validar(NumOcultos: MutableList<Int>, NumMost: MutableList<Int>, cc: MutableList<Boolean>, aumentarContador: () -> Unit) {
    Button(
        onClick = {
            NumMost.forEachIndexed { index, numero ->
                cc[index] = (numero == NumOcultos[index])
            }
            aumentarContador()
        },
        modifier = Modifier.padding(top = 17.dp)
    ) {
        Text("Validar")
    }
}

@Composable
fun Resultado(cc: MutableList<Boolean>) {
    if (cc.all { it }) {
        Text("¡Ganaste! Todos los números son correctos.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Green
        )
    }
}

@Composable
fun Debug(HideNumbers: List<Int>) {
    Text(
        text = "Valores actuales: ${HideNumbers.joinToString(", ")}",
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(top = 16.dp)
    )
}
