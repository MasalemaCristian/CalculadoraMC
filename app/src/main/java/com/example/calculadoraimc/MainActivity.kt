package com.example.calculadoraimc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                CalculadoraIMC()
            }
        }
    }
}

@Composable
fun CalculadoraIMC() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {

        composable("inicio") {
            PantallaInicio(navController)
        }

        composable(
            route = "resultado/{nombre}/{imc}",
            arguments = listOf(
                navArgument("nombre") {
                    type = NavType.StringType
                },
                navArgument("imc") {
                    type = NavType.FloatType
                }
            )
        ) { backStackEntry ->

            val nombre =
                backStackEntry.arguments?.getString("nombre") ?: ""

            val imc =
                backStackEntry.arguments?.getFloat("imc") ?: 0f

            PantallaResultado(
                nombre = nombre,
                imc = imc,
                navController = navController
            )
        }
    }
}

@Composable
fun PantallaInicio(
    navController: NavHostController
) {

    var nombre by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }

    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Calculadora de IMC",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso (kg)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = altura,
            onValueChange = { altura = it },
            label = { Text("Altura (m)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        if (error) {
            Text(
                text = "Por favor, ingresa valores válidos",
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {

                val pesoNumero = peso.toFloatOrNull()
                val alturaNumero = altura.toFloatOrNull()

                if (
                    pesoNumero == null ||
                    alturaNumero == null ||
                    pesoNumero <= 0 ||
                    alturaNumero <= 0
                ) {
                    error = true
                } else {

                    error = false

                    val imc =
                        pesoNumero / (alturaNumero * alturaNumero)

                    val nombreCodificado =
                        URLEncoder.encode(
                            nombre,
                            StandardCharsets.UTF_8.toString()
                        )

                    navController.navigate(
                        "resultado/$nombreCodificado/$imc"
                    )
                }
            }
        ) {
            Text("Calcular")
        }
    }
}

@Composable
fun PantallaResultado(
    nombre: String,
    imc: Float,
    navController: NavHostController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Hola $nombre, tu resultado es:",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = String.format("IMC: %.2f", imc),
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text("Volver")
        }
    }
}