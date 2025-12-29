package xyz.andreasciagura.glitch

import androidx.compose.ui.Modifier
import kotlin.test.Test
import kotlin.test.assertNotNull

class GlitchTest {

    @Test
    fun testGlitchEffectModifierCreation() {
        val modifier = Modifier.glitchEffect(
            enabled = true,
            delay = 100,
            duration = 200,
            interval = 300
        )
        assertNotNull(modifier)
    }

    @Test
    fun testAnimateGlitchModifierCreation() {
        val modifier = Modifier.animateGlitch(
            enabled = true,
            delay = 100,
            duration = 200,
            interval = 300
        )
        assertNotNull(modifier)
    }
    
    @Test
    fun testGlitchDisabled() {
        val modifier = Modifier.glitchEffect(enabled = false)
        assertNotNull(modifier)
    }

    @Test
    fun testGlitchWithNegativeValues() {
        // Just ensure it doesn't throw during creation
        val modifier = Modifier.glitchEffect(
            delay = -100,
            duration = -200,
            interval = -300
        )
        assertNotNull(modifier)
    }
}
