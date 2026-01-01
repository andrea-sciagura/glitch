/*
 * Copyright 2024-2026 Andrea Sciagura
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.andrea_sciagura.glitch.sample

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import xyz.andrea_sciagura.glitch.glitchEffect
import xyz.andrea_sciagura.glitch.animateGlitch
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import xyz.andrea_sciagura.glitch.sample.generated.resources.Res
import xyz.andrea_sciagura.glitch.sample.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize()
                .animateGlitch(
                    delay = 1500,
                    duration = 300,
                    interval = 1500
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(20.dp))

            Text(
                text = "GLITCH EFFECT",
                fontSize = 40.sp,
            )

            Spacer(Modifier.height(20.dp))

            Text("Comparison: Original vs Standard", fontSize = 18.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Original", fontSize = 12.sp)
                    Text(
                        text = "GLITCH",
                        fontSize = 24.sp,
                        modifier = Modifier.glitchEffect(duration = 500, interval = 1000)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Standard", fontSize = 12.sp)
                    Text(
                        text = "GLITCH",
                        fontSize = 24.sp,
                        modifier = Modifier.animateGlitch(duration = 500, interval = 1000)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "STANDARD API GLITCH",
                fontSize = 24.sp,
            )

            Spacer(Modifier.height(40.dp))

            Image(
                painter = painterResource(Res.drawable.compose_multiplatform),
                contentDescription = null,
            )

            Spacer(Modifier.height(40.dp))

            var showContent by remember { mutableStateOf(false) }
            Button(onClick = { showContent = !showContent }) {
                Text("Toggle More Content")
            }

            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth().animateGlitch(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Compose: $greeting")
                }
            }
        }
    }
}