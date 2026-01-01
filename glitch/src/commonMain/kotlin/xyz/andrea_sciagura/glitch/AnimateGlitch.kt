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

package xyz.andrea_sciagura.glitch

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * An alternative implementation of the glitchEffect effect using standard Compose Animation APIs.
 *
 * This implementation uses [rememberInfiniteTransition] to drive the animation loop,
 * making it more suitable for integration with Compose's animation inspection tools.
 * It provides a similar visual effect to [Modifier.glitchEffect] by calculating glitchEffect
 * parameters based on the animation's progress.
 *
 * @param enabled Whether the glitchEffect effect is active. If false, the original content is drawn normally.
 * @param delay Initial delay in milliseconds before the first glitchEffect animation begins.
 * @param duration The length of time in milliseconds that the glitchEffect effect lasts during each cycle.
 * @param interval The idle time in milliseconds between successive glitchEffect animations.
 */
fun Modifier.animateGlitch(
    enabled: Boolean = true,
    delay: Long = 0,
    duration: Long = 500,
    interval: Long = 2000
): Modifier = composed {
    if (!enabled) return@composed Modifier

    var isStarted by remember { mutableStateOf(delay <= 0L) }
    if (delay > 0) {
        LaunchedEffect(delay) {
            delay(delay)
            isStarted = true
        }
    }

    if (!isStarted) return@composed Modifier

    // Using rememberInfiniteTransition to drive the animation loop
    val infiniteTransition = rememberInfiniteTransition(label = "GlitchLoop")
    val totalPeriod = (duration + interval).toInt()

    // progress goes from 0 to 1 over the duration + interval
    val progressState = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(totalPeriod, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "GlitchProgress"
    )

    // Calculate if we are currently in the glitching phase
    // Using derivedStateOf to avoid recomposing the entire modifier every frame
    val isGlitching by remember {
        derivedStateOf { progressState.value < (duration.toFloat() / totalPeriod) }
    }
    
    // To achieve true randomness, we maintain a seed that changes each cycle.
    var cycleSeed by remember { mutableStateOf(Random.nextLong()) }

    // Update the seed when we are not glitching, so it's ready for the next cycle.
    // This ensures that each glitchEffect animation cycle looks different.
    LaunchedEffect(isGlitching) {
        if (!isGlitching) {
            cycleSeed = Random.nextLong()
        }
    }

    // We use the progress and cycleSeed to create a seed for randomness within each frame,
    // but changing fast enough to create the "jitter".
    val glitchParams by remember(isGlitching) {
        derivedStateOf {
            if (isGlitching) {
                // Change glitchEffect parameters every 50ms (jitter)
                // totalPeriod * progress gives current ms in the loop
                val loopMs = (progressState.value * totalPeriod).toLong()
                val jitterSeed = loopMs / 50
                val seed = cycleSeed xor jitterSeed
                val random = Random(seed)
                
                AnimateGlitchParams(
                    offsetX = random.nextFloat() * 20f - 10f,
                    offsetY = random.nextFloat() * 4f - 2f,
                    chromaticOffset = random.nextFloat() * 10f,
                    slices = List(random.nextInt(3, 8)) {
                        val start = random.nextFloat()
                        val height = random.nextFloat() * 0.2f
                        AnimateGlitchSlice(start, (start + height).coerceAtMost(1f), random.nextFloat() * 40f - 20f)
                    }
                )
            } else {
                AnimateGlitchParams()
            }
        }
    }

    this.drawWithContent {
        if (!isGlitching) {
            drawContent()
        } else {
            val params = glitchParams
            withTransform({
                translate(params.offsetX, params.offsetY)
            }) {
                // Chromatic Aberration
                if (params.chromaticOffset != 0f) {
                    // Red shift
                    withTransform({ translate(params.chromaticOffset, 0f) }) {
                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply { alpha = 0.5f }
                            canvas.saveLayer(Rect(Offset.Zero, size), paint)
                            this@drawWithContent.drawContent()
                            canvas.restore()
                        }
                    }
                    // Blue shift
                    withTransform({ translate(-params.chromaticOffset, 0f) }) {
                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply { alpha = 0.5f }
                            canvas.saveLayer(Rect(Offset.Zero, size), paint)
                            this@drawWithContent.drawContent()
                            canvas.restore()
                        }
                    }
                }

                this@drawWithContent.drawContent()

                // Slices
                params.slices.forEach { slice ->
                    val sliceTop = slice.top * size.height
                    val sliceBottom = slice.bottom * size.height
                    withTransform({
                        clipRect(0f, sliceTop, size.width, sliceBottom, ClipOp.Intersect)
                        translate(slice.offset, 0f)
                    }) {
                        this@drawWithContent.drawContent()
                    }
                }
            }
        }
    }
}

private data class AnimateGlitchParams(
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
    val chromaticOffset: Float = 0f,
    val slices: List<AnimateGlitchSlice> = emptyList()
)

private data class AnimateGlitchSlice(
    val top: Float,
    val bottom: Float,
    val offset: Float
)
