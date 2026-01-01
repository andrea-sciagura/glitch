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
