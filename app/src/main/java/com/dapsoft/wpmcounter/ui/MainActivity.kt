package com.dapsoft.wpmcounter.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dapsoft.wpmcounter.ui.root.AppEntry
import com.dapsoft.wpmcounter.ui.theme.WordsPerMinuteCounterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*enableEdgeToEdge()
        setContent {
            WordsPerMinuteCounterTheme {
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.Companion.padding(innerPadding)
                    )
                }
            }
        }*/
        setContent { AppEntry() }
        /*setContent {
            WordsPerMinuteCounterTheme {
                Column(Modifier.padding(24.dp)) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        label = { Text("Your name") }
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = {  }, enabled = true) {
                        Text("Start typing")
                    }
                }
            }
        }*/
    }
}

/*
@Composable
fun Greeting(modifier: Modifier = Modifier.Companion) {
    val name = remember { mutableStateOf("") }

    TextField(
        value = name.value,
        onValueChange = { name.value = it },
        label = { Text("Your name") },
        placeholder = { Text("Please write your name") },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WordsPerMinuteCounterTheme {
        Greeting()
    }
}*/