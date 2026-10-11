package com.example.fillcolor

import com.example.fillcolor.model.TemplateRepository
import com.example.fillcolor.utils.ColorMatchResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ColorComparisonTest {

    @Test
    fun testTemplateRepository_hasTemplates() {
        val templates = TemplateRepository.templates
        assertNotNull(templates)
        assertEquals(5, templates.size)

        val apple = templates.first { it.id == "apple" }
        assertEquals("Juicy Apple", apple.name)
    }

    @Test
    fun testColorMatchResult_starsMapping() {
        val highResult = ColorMatchResult(
            percentage = 95,
            stars = 3,
            message = "Super Artist!",
            subtitle = "Great job"
        )
        assertEquals(3, highResult.stars)
        assertEquals(95, highResult.percentage)
    }
}
