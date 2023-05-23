package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.calculator.ui.theme.CalculatorTheme
import kotlin.math.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (calculate()) {
                        "/" -> {
                            Output("options:")
                            Output("1. convert (c)")
                            Output("2. bodies (b)")
                            Output("3. rule of three (Ro3)")
                            Output("4. quadratic function (q)")
                            Output("5. cube (cb)")
                            Output("6. prism (p)")
                        }
                        "/prism", "/6", "/p" -> Adv().Prism()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun calculate(): String {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        // Input
        TextField(
            value = expression,
            onValueChange = { expression = it },
            label = { Text("Enter an expression") },
            modifier = Modifier.fillMaxWidth()
        )

        // Calculate button
        Button(
            onClick = {
                val expressionObj = Expression()
                result =expressionObj.calc(expression)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Calculate")
        }

        // Output
        Text(
            text = result,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
    return expression
}

internal class Expression {
    fun calc(ex: String): String {
        val result = eval(ex)
        return "$ex = $result"
    }

    companion object {
        private fun eval(str: String): Double {
            return object : Any() {
                var pos = -1
                var ch = 0
                fun nextChar() {
                    ch = if (++pos < str.length) str[pos].code else -1
                }

                fun skip(charToSkip: Char): Boolean {
                    while (ch == ' '.code) nextChar()
                    if (ch == charToSkip.code) {
                        nextChar()
                        return true
                    }
                    return false
                }

                fun parse(): Double {
                    nextChar()
                    val x = parseExpression()
                    if (pos < str.length) println("Unexpected: " + ch.toChar())
                    return x
                }

                fun parseExpression(): Double {
                    var x = parseTerm()
                    while (true) {
                        if (skip('+')) x += parseTerm() // addition
                        else if (skip('-')) x -= parseTerm() // subtraction
                        else return x
                    }
                }

                fun parseTerm(): Double {
                    var x = parseFactor()
                    while (true) {
                        if (skip('*')) x *= parseFactor() // multiplication
                        else if (skip('/')) x /= parseFactor() // division
                        else return x
                    }
                }

                fun parseFactor(): Double {
                    if (skip('+')) return parseFactor() // unary plus
                    if (skip('-')) return -parseFactor() // unary minus
                    var x: Double
                    val startPos = pos
                    if (skip('(')) { // parentheses
                        x = parseExpression()
                        skip(')')
                    } else if (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) { // numbers
                        while (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) nextChar()
                        x = str.substring(startPos, pos).toDouble()
                    } else if (ch >= 'a'.code && ch <= 'z'.code) { // functions
                        while (ch >= 'a'.code && ch <= 'z'.code) nextChar()
                        val func = str.substring(startPos, pos)
                        x = parseFactor()
                        x = when (func) {
                            "sqrt" -> sqrt(x)
                            "sin" -> sin(Math.toRadians(x))
                            "cos" -> cos(Math.toRadians(x))
                            "tan" -> tan(Math.toRadians(x))
                            "ln" -> ln(x)
                            "log" -> log10(x)
                            "fact" -> factorial(x)
                            else -> {
                                println("Unknown function: $func")
                                0.0
                            }
                        }
                    } else {
                        println("Unexpected: " + ch.toChar())
                        x = 0.0
                    }
                    if (skip('^')) x = x.pow(parseFactor()) // exponentiation
                    return x
                }
            }.parse()
        }

        private fun factorial(x: Double): Double {
            if (x < 0) {
                println("factorial: invalid number: $x")
                return 0.0
            }
            else if (x == floor(x)) {
                var factorial = 1
                var i = 1
                while (i <= x) {
                    factorial *= i
                    i++
                }
                return factorial.toDouble()
            }
            else {
                println("factorial: invalid number: $x")
                return 0.0
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class Adv {
    @Composable
    fun Prism() {
        var a by remember { mutableStateOf("") }
        var b by remember { mutableStateOf("") }
        var c by remember { mutableStateOf("") }
        var result by remember { mutableStateOf("") }

        Column(modifier = Modifier.padding(16.dp)) {
            // Input
            TextField(
                value = a,
                onValueChange = { newValue ->
                    a = newValue.replace(Regex("[^\\d.]+"), "")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Enter a number") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = b,
                onValueChange = { newValue ->
                    b = newValue.replace(Regex("[^\\d.]+"), "")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Enter a number") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = c,
                onValueChange = { newValue ->
                    c = newValue.replace(Regex("[^\\d.]+"), "")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Enter a number") },
                modifier = Modifier.fillMaxWidth()
            )

            // Calculate button
            Button(
                onClick = {
                    val volume = (a.toDoubleOrNull()?.times(b.toDoubleOrNull()!!) ?: 0.0) * c.toDoubleOrNull()!!
                    val surface = 2 * ((a.toDoubleOrNull()?.times(b.toDoubleOrNull()!!) ?: 0.0) + (b.toDoubleOrNull()?.times(c.toDoubleOrNull()!!) ?: 0.0) + (a.toDoubleOrNull()?.times(c.toDoubleOrNull()!!) ?: 0.0))
                    result = "volume: $volume\nsurface: $surface"
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Calculate")
            }

            // Output
            Text(
                text = result,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun Output(mes: String) {
    Text(
        text = mes,
        modifier = Modifier.padding(top = 16.dp)
    )
}