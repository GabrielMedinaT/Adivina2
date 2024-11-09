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
            Adivina2Theme  {
                Surface(
                    modifier=Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Adivinanza2()
                }
            }
        }
    }
    // Aqui iria para ser privada
}
// funcion publica
@Composable
fun Adivinanza2(){
    // NumOcult = numero ocultos
    val NumOcultos = remember {(1..9).shuffled().take(2).toMutableList() }
    // NumMost = numero mostrados
    var NumMost = remember { MutableList(1){ 0 }.toMutableStateList() }
    // estado de la caja,cc= celdas correctas
    var cc = remember{ MutableList(1){false}.toMutableStateList() }

    // columna unica
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(17.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(17.dp)
    ){
        // Llamar a una funcion titulo
        GameTitle()
        // LLamar a una funcion box
        Celda(NumMost,cc)
        // LLamar a una funcion boton
        Validar(NumOcultos,NumMost,cc)
        Resultado(cc)
        //Mostramos el debugging
        Debug(NumMost)
    }
}

@Composable
fun GameTitle(){
    Spacer(modifier=Modifier.size(100.dp))
    Text("Adivina dos numeros (1-9)",
        style= MaterialTheme.typography.headlineMedium,
        modifier = Modifier
            .padding(16.dp)
    )
}

@Composable
fun Celda(numerosMostrados: MutableList<Int>,
          celdasCorrectas: MutableList<Boolean>){
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        repeat(1) { index ->
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .border(
                        width = 2.dp,
                        color = if (celdasCorrectas[index]) Color.Green
                        else MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        if (!celdasCorrectas[index]) {
                            numerosMostrados[index] = (numerosMostrados[index] % 9) + 1
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (celdasCorrectas[index]) numerosMostrados[index].toString()
                    else if (numerosMostrados[index] == 0) ""
                    else numerosMostrados[index].toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun Validar(NumOcultos:MutableList<Int>,NumMost:MutableList<Int>,cc:MutableList<Boolean>) {
    Button(
        onClick = {
            NumMost.forEachIndexed{index,numero ->
                cc[index] = (numero == NumOcultos[index])
            }
        },
        modifier = Modifier.padding(top=17.dp)
    ){
        Text("Validar")
    }
}

@Composable
fun Resultado(cc:MutableList<Boolean>){
    if(cc.any{it}){
        Text("Acertaste el numero",
            style = MaterialTheme.typography.bodyMedium,
            color=MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun Debug(nm:MutableList<Int>){
    Text(
        text = "Valores actuales: ${nm.joinToString(", ")}",
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(top = 16.dp)
    )
}