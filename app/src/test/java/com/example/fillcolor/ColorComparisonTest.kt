package com.example.fillcolor

import com.example.fillcolor.model.TemplateRepository
import com.example.fillcolor.utils.ColorMatchResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ColorComparisonTest {

    @Test
    fun testTemplateRepository_has100TemplatesAcross20Levels() {
        val templates = TemplateRepository.templates
        assertNotNull(templates)
        assertEquals(100, templates.size)

        val levels = TemplateRepository.templatesByLevel
        assertEquals(20, levels.size)
        levels.forEach { (_, levelTemplates) ->
            assertEquals(5, levelTemplates.size)
        }
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
