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
    // NumOcult = número ocultos
    var HideNumbers = remember { (1..9).shuffled().take(9).toMutableList() }
    // NumMost = números mostrados
    var NumMost = remember { MutableList(2) { 0 }.toMutableStateList() }
    // estado de la caja, cc = celdas correctas
    var cc = remember { MutableList(2) { false }.toMutableStateList() }
    // contador de intentos
    var contador by remember { mutableStateOf(0) }

    // Resetear los valores
    fun resetGame() {
        HideNumbers.clear()
        HideNumbers.addAll((1..9).shuffled().take(2))
        NumMost[0] = 0
        NumMost[1] = 0
        cc[0] = false
        cc[1] = false
        contador = 0
    }

    // columna única
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(17.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(17.dp)
    ) {
        // Llamar a una función título
        GameTitle()
        // Llamar a una función box
        Celda(NumMost, cc)
        // Llamar a una función botón
        Validar(HideNumbers, NumMost, cc, { contador++ })
        // Resultado
        Resultado(cc)
        // Mostramos el contador de intentos
        Text("Intentos: $contador", style = MaterialTheme.typography.bodyMedium)
        // Botón para resetear el juego
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            // Crear solo 2 celdas en una fila
            repeat(2) { index ->
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
