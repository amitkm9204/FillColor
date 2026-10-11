package com.example.fillcolor.utils

import android.graphics.Bitmap
import android.graphics.Color
import java.util.ArrayDeque

object FloodFill {

    /**
     * Fills connected pixel region starting at (startX, startY) on [bitmap] with [fillColor].
     * Stops at black/dark outline borders or boundaries.
     */
    fun fill(bitmap: Bitmap, startX: Int, startY: Int, fillColor: Int): Boolean {
        val width = bitmap.width
        val height = bitmap.height

        if (startX !in 0 until width || startY !in 0 until height) return false

        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val targetColor = pixels[startY * width + startX]

        // If clicking on same color or dark outline line, skip
        if (targetColor == fillColor) return false
        if (isDarkOutline(targetColor)) return false

        val queue = ArrayDeque<Int>()
        // Encode (x, y) into single Int: (x shl 16) or y
        queue.add((startX shl 16) or startY)

        val visited = BooleanArray(width * height)
        visited[startY * width + startX] = true

        while (!queue.isEmpty()) {
            val pos = queue.removeFirst()
            val x = pos shr 16
            val y = pos and 0xFFFF

            val idx = y * width + x
            pixels[idx] = fillColor

            // Check 4-connected neighbors
            val neighborsX = intArrayOf(x + 1, x - 1, x, x)
            val neighborsY = intArrayOf(y, y, y + 1, y - 1)

            for (i in 0 until 4) {
                val nx = neighborsX[i]
                val ny = neighborsY[i]

                if (nx in 0 until width && ny in 0 until height) {
                    val nIdx = ny * width + nx
                    if (!visited[nIdx]) {
                        visited[nIdx] = true
                        val currentColor = pixels[nIdx]

                        // Fill if pixel is close to start color and not dark outline
                        if (isSimilarColor(currentColor, targetColor) && !isDarkOutline(currentColor)) {
                            queue.add((nx shl 16) or ny)
                        }
                    }
                }
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return true
    }

    private fun isDarkOutline(color: Int): Boolean {
        val alpha = Color.alpha(color)
        if (alpha < 50) return false // Transparent areas are not outlines
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)

        // Dark threshold (black or nearly black lines)
        return red < 60 && green < 60 && blue < 60
    }

    private fun isSimilarColor(c1: Int, c2: Int): Boolean {
        if (c1 == c2) return true

        val rDiff = Color.red(c1) - Color.red(c2)
        val gDiff = Color.green(c1) - Color.green(c2)
        val bDiff = Color.blue(c1) - Color.blue(c2)
        val aDiff = Color.alpha(c1) - Color.alpha(c2)

        val distanceSq = rDiff * rDiff + gDiff * gDiff + bDiff * bDiff + aDiff * aDiff
        return distanceSq < 2500 // Color distance tolerance threshold
    }
}
