package xyz.andreasciagura.glitch

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
 * A modifier that applies a glitchEffect effect to the composable.
 *
 * This implementation uses [LaunchedEffect] and [drawWithContent] to create a
 * non-deterministic glitchEffect effect. It slices the content into horizontal bands
 * and displaces them randomly, while also applying a chromatic aberration effect.
 *
 * @param enabled Whether the glitchEffect effect is active. If false, the original content is drawn normally.
 * @param delay Initial delay in milliseconds before the first glitchEffect animation begins.
 * @param duration The length of time in milliseconds that the glitchEffect effect lasts during each cycle.
 * @param interval The idle time in milliseconds between successive glitchEffect animations.
 */
fun Modifier.glitchEffect(
    enabled: Boolean = true,
    delay: Long = 0,
    duration: Long = 500,
    interval: Long = 2000
): Modifier = composed {
    if (!enabled) return@composed Modifier

    var isGlitching by remember { mutableStateOf(false) }
    val random = remember { Random(Random.nextLong()) }
    
    // Trigger the glitchEffect loop
    LaunchedEffect(enabled, delay, duration, interval) {
        delay(delay)
        while (true) {
            isGlitching = true
            delay(duration)
            isGlitching = false
            delay(interval)
        }
    }

    // State for the current glitchEffect parameters, updated frequently when isGlitching is true
    var glitchState by remember { mutableStateOf(GlitchEffectParams()) }

    LaunchedEffect(isGlitching) {
        if (isGlitching) {
            while (isGlitching) {
                glitchState = GlitchEffectParams(
                    offsetX = random.nextFloat() * 20f - 10f,
                    offsetY = random.nextFloat() * 4f - 2f,
                    slices = List(random.nextInt(3, 8)) {
                        val start = random.nextFloat()
                        val height = random.nextFloat() * 0.2f
                        GlitchEffectSlice(start, (start + height).coerceAtMost(1f), random.nextFloat() * 40f - 20f)
                    },
                    chromaticOffset = random.nextFloat() * 10f
                )
                delay(random.nextLong(30, 100))
            }
        } else {
            glitchState = GlitchEffectParams()
        }
    }

    this.drawWithContent {
        if (!isGlitching) {
            drawContent()
        } else {
            val params = glitchState
            
            withTransform({
                translate(params.offsetX, params.offsetY)
            }) {
                // Draw chromatic aberration (simplified: two extra layers with ghosting)
                
                // Red shift (ghosting)
                withTransform({
                    translate(params.chromaticOffset, 0f)
                }) {
                    drawIntoCanvas { canvas ->
                        val paint = Paint().apply { alpha = 0.5f }
                        canvas.saveLayer(Rect(Offset.Zero, size), paint)
                        this@drawWithContent.drawContent()
                        canvas.restore()
                    }
                }
                
                // Blue shift (ghosting)
                withTransform({
                    translate(-params.chromaticOffset, 0f)
                }) {
                    drawIntoCanvas { canvas ->
                        val paint = Paint().apply { alpha = 0.5f }
                        canvas.saveLayer(Rect(Offset.Zero, size), paint)
                        this@drawWithContent.drawContent()
                        canvas.restore()
                    }
                }
    
                // Main content
                this@drawWithContent.drawContent()
    
                // Displaced slices
                params.slices.forEach { slice ->
                    val sliceTop = slice.top * size.height
                    val sliceBottom = slice.bottom * size.height
                    
                    withTransform({
                        clipRect(
                            left = 0f,
                            top = sliceTop,
                            right = size.width,
                            bottom = sliceBottom,
                            clipOp = ClipOp.Intersect
                        )
                        translate(slice.offset, 0f)
                    }) {
                        this@drawWithContent.drawContent()
                    }
                }
            }
        }
    }
}

private data class GlitchEffectParams(
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
    val slices: List<GlitchEffectSlice> = emptyList(),
    val chromaticOffset: Float = 0f
)

private data class GlitchEffectSlice(
    val top: Float,
    val bottom: Float,
    val offset: Float
)
