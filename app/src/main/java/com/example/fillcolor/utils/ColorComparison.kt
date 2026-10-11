package com.example.fillcolor.utils

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class ColorMatchResult(
    val percentage: Int,
    val stars: Int,
    val message: String,
    val subtitle: String
)

object ColorComparison {

    /**
     * Compares user colored bitmap with target reference bitmap and calculates percentage score.
     */
    fun calculateScore(userBitmap: Bitmap, targetBitmap: Bitmap): ColorMatchResult {
        val width = minOf(userBitmap.width, targetBitmap.width)
        val height = minOf(userBitmap.height, targetBitmap.height)

        val userPixels = IntArray(width * height)
        val targetPixels = IntArray(width * height)

        userBitmap.getPixels(userPixels, 0, width, 0, 0, width, height)
        targetBitmap.getPixels(targetPixels, 0, width, 0, 0, width, height)

        var totalEvaluated = 0
        var matchedCount = 0

        val step = 2 // Evaluate every 2nd pixel for optimal speed

        for (y in 0 until height step step) {
            for (x in 0 until width step step) {
                val idx = y * width + x
                val targetColor = targetPixels[idx]
                val userColor = userPixels[idx]

                // Skip black outline pixels
                if (isBlackLine(targetColor)) continue

                totalEvaluated++

                val rDiff = Color.red(userColor) - Color.red(targetColor)
                val gDiff = Color.green(userColor) - Color.green(targetColor)
                val bDiff = Color.blue(userColor) - Color.blue(targetColor)

                val distance = sqrt((rDiff * rDiff + gDiff * gDiff + bDiff * bDiff).toDouble())

                // Tolerance threshold for kid-friendly color matching (~75 distance)
                if (distance <= 75.0) {
                    matchedCount++
                }
            }
        }

        val rawPercentage = if (totalEvaluated > 0) {
            (matchedCount.toFloat() / totalEvaluated.toFloat() * 100f).roundToInt()
        } else {
            0
        }

        val percentage = rawPercentage.coerceIn(0, 100)

        val stars = when {
            percentage >= 85 -> 3
            percentage >= 50 -> 2
            else -> 1
        }

        val (message, subtitle) = when {
            percentage >= 90 -> "🌟 Super Artist!" to "You colored this perfectly!"
            percentage >= 75 -> "🎨 Fantastic Job!" to "Your picture looks amazing!"
            percentage >= 50 -> "👍 Great Effort!" to "Good job! Try matching colors closer."
            else -> "🌈 Keep Practicing!" to "Look at the reference picture and try again!"
        }

        return ColorMatchResult(
            percentage = percentage,
            stars = stars,
            message = message,
            subtitle = subtitle
        )
    }

    private fun isBlackLine(color: Int): Boolean {
        return Color.alpha(color) > 100 &&
                Color.red(color) < 60 &&
                Color.green(color) < 60 &&
                Color.blue(color) < 60
    }
}
