package com.example.fillcolor.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF

enum class TemplateCategory {
    FRUIT, NATURE, BUILDINGS, SHAPES
}

data class ColoringTemplate(
    val id: String,
    val name: String,
    val category: TemplateCategory,
    val width: Int = 512,
    val height: Int = 512,
    val drawOutline: (Canvas, Paint) -> Unit,
    val drawTargetReference: (Canvas) -> Unit
) {
    /**
     * Generates a black line outline bitmap for overlay on the coloring canvas.
     */
    fun createOutlineBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 10f
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
        drawOutline(canvas, paint)
        return bitmap
    }

    /**
     * Generates the reference sample colored bitmap for display and score comparison.
     */
    fun createTargetReferenceBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        // Default white background
        canvas.drawColor(Color.WHITE)
        drawTargetReference(canvas)

        // Draw outline on top of reference so outline stays sharp
        val paint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 10f
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
        drawOutline(canvas, paint)
        return bitmap
    }
}

object TemplateRepository {
    val templates = listOf(
        // 1. APPLE
        ColoringTemplate(
            id = "apple",
            name = "Juicy Apple",
            category = TemplateCategory.FRUIT,
            drawOutline = { canvas, paint ->
                // Apple body
                canvas.drawCircle(210f, 280f, 130f, paint)
                canvas.drawCircle(302f, 280f, 130f, paint)
                // Bottom curve connector
                val appleBottom = Path().apply {
                    moveTo(120f, 320f)
                    cubicTo(160f, 440f, 352f, 440f, 392f, 320f)
                }
                canvas.drawPath(appleBottom, paint)

                // Stem
                val stem = Path().apply {
                    moveTo(256f, 160f)
                    quadTo(270f, 110f, 290f, 80f)
                }
                val stemPaint = Paint(paint).apply { strokeWidth = 12f }
                canvas.drawPath(stem, stemPaint)

                // Leaf
                val leaf = Path().apply {
                    moveTo(270f, 120f)
                    quadTo(340f, 90f, 370f, 120f)
                    quadTo(310f, 160f, 270f, 120f)
                }
                canvas.drawPath(leaf, paint)
            },
            drawTargetReference = { canvas ->
                val fillPaint = Paint().apply {
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }

                // Apple body - Red
                fillPaint.color = Color.rgb(229, 57, 53) // Bright Red
                canvas.drawCircle(210f, 280f, 130f, fillPaint)
                canvas.drawCircle(302f, 280f, 130f, fillPaint)
                val appleBottom = Path().apply {
                    moveTo(120f, 320f)
                    cubicTo(160f, 440f, 352f, 440f, 392f, 320f)
                    lineTo(392f, 280f)
                    lineTo(120f, 280f)
                    close()
                }
                canvas.drawPath(appleBottom, fillPaint)

                // Stem - Brown
                fillPaint.color = Color.rgb(121, 85, 72)
                val stemPaint = Paint().apply {
                    color = Color.rgb(121, 85, 72)
                    style = Paint.Style.STROKE
                    strokeWidth = 16f
                    strokeCap = Paint.Cap.ROUND
                    isAntiAlias = true
                }
                val stem = Path().apply {
                    moveTo(256f, 160f)
                    quadTo(270f, 110f, 290f, 80f)
                }
                canvas.drawPath(stem, stemPaint)

                // Leaf - Green
                fillPaint.color = Color.rgb(76, 175, 80)
                val leaf = Path().apply {
                    moveTo(270f, 120f)
                    quadTo(340f, 90f, 370f, 120f)
                    quadTo(310f, 160f, 270f, 120f)
                    close()
                }
                canvas.drawPath(leaf, fillPaint)
            }
        ),

        // 2. HAPPY STAR
        ColoringTemplate(
            id = "star",
            name = "Happy Star",
            category = TemplateCategory.SHAPES,
            drawOutline = { canvas, paint ->
                val starPath = createStarPath(256f, 256f, 5, 200f, 90f)
                canvas.drawPath(starPath, paint)

                // Eyes
                val eyePaint = Paint(paint).apply { style = Paint.Style.FILL }
                canvas.drawCircle(216f, 230f, 14f, eyePaint)
                canvas.drawCircle(296f, 230f, 14f, eyePaint)

                // Smile
                val smile = Path().apply {
                    moveTo(220f, 270f)
                    quadTo(256f, 310f, 292f, 270f)
                }
                canvas.drawPath(smile, paint)
            },
            drawTargetReference = { canvas ->
                val fillPaint = Paint().apply {
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }

                // Star - Yellow
                fillPaint.color = Color.rgb(255, 213, 79) // Warm Yellow
                val starPath = createStarPath(256f, 256f, 5, 200f, 90f)
                canvas.drawPath(starPath, fillPaint)

                // Cheeks - Pink
                fillPaint.color = Color.rgb(240, 98, 146)
                canvas.drawCircle(190f, 260f, 16f, fillPaint)
                canvas.drawCircle(322f, 260f, 16f, fillPaint)
            }
        ),

        // 3. SUN AND CLOUD
        ColoringTemplate(
            id = "sun_cloud",
            name = "Sunny Day",
            category = TemplateCategory.NATURE,
            drawOutline = { canvas, paint ->
                // Sun body
                canvas.drawCircle(180f, 180f, 85f, paint)

                // Sun rays
                for (i in 0 until 8) {
                    val angle = Math.toRadians((i * 45).toDouble())
                    val startX = (180 + 95 * Math.cos(angle)).toFloat()
                    val startY = (180 + 95 * Math.sin(angle)).toFloat()
                    val endX = (180 + 130 * Math.cos(angle)).toFloat()
                    val endY = (180 + 130 * Math.sin(angle)).toFloat()
                    canvas.drawLine(startX, startY, endX, endY, paint)
                }

                // Cloud
                val cloud = Path().apply {
                    moveTo(220f, 380f)
                    lineTo(420f, 380f)
                    cubicTo(460f, 380f, 460f, 320f, 420f, 310f)
                    cubicTo(430f, 250f, 360f, 230f, 330f, 270f)
                    cubicTo(300f, 230f, 230f, 260f, 240f, 310f)
                    cubicTo(200f, 320f, 190f, 380f, 220f, 380f)
                    close()
                }
                canvas.drawPath(cloud, paint)
            },
            drawTargetReference = { canvas ->
                val fillPaint = Paint().apply {
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }

                // Sky background - Very light blue
                canvas.drawColor(Color.rgb(227, 242, 253))

                // Sun body - Bright Yellow
                fillPaint.color = Color.rgb(255, 235, 59)
                canvas.drawCircle(180f, 180f, 85f, fillPaint)

                // Sun rays - Orange
                val rayPaint = Paint().apply {
                    color = Color.rgb(255, 152, 0)
                    style = Paint.Style.STROKE
                    strokeWidth = 12f
                    strokeCap = Paint.Cap.ROUND
                    isAntiAlias = true
                }
                for (i in 0 until 8) {
                    val angle = Math.toRadians((i * 45).toDouble())
                    val startX = (180 + 95 * Math.cos(angle)).toFloat()
                    val startY = (180 + 95 * Math.sin(angle)).toFloat()
                    val endX = (180 + 130 * Math.cos(angle)).toFloat()
                    val endY = (180 + 130 * Math.sin(angle)).toFloat()
                    canvas.drawLine(startX, startY, endX, endY, rayPaint)
                }

                // Cloud - Light Cyan Blue
                fillPaint.color = Color.rgb(129, 212, 250)
                val cloud = Path().apply {
                    moveTo(220f, 380f)
                    lineTo(420f, 380f)
                    cubicTo(460f, 380f, 460f, 320f, 420f, 310f)
                    cubicTo(430f, 250f, 360f, 230f, 330f, 270f)
                    cubicTo(300f, 230f, 230f, 260f, 240f, 310f)
                    cubicTo(200f, 320f, 190f, 380f, 220f, 380f)
                    close()
                }
                canvas.drawPath(cloud, fillPaint)
            }
        ),

        // 4. COZY HOUSE
        ColoringTemplate(
            id = "house",
            name = "Cozy House",
            category = TemplateCategory.BUILDINGS,
            drawOutline = { canvas, paint ->
                // House base
                canvas.drawRect(120f, 220f, 392f, 440f, paint)

                // Roof (Triangle)
                val roof = Path().apply {
                    moveTo(90f, 220f)
                    lineTo(256f, 80f)
                    lineTo(422f, 220f)
                    close()
                }
                canvas.drawPath(roof, paint)

                // Door
                canvas.drawRect(220f, 320f, 292f, 440f, paint)
                // Door knob
                val knobPaint = Paint(paint).apply { style = Paint.Style.FILL }
                canvas.drawCircle(280f, 380f, 5f, knobPaint)

                // Window
                canvas.drawRect(150f, 260f, 200f, 310f, paint)
                canvas.drawLine(175f, 260f, 175f, 310f, paint)
                canvas.drawLine(150f, 285f, 200f, 285f, paint)

                // Window 2
                canvas.drawRect(312f, 260f, 362f, 310f, paint)
                canvas.drawLine(337f, 260f, 337f, 310f, paint)
                canvas.drawLine(312f, 285f, 362f, 285f, paint)
            },
            drawTargetReference = { canvas ->
                val fillPaint = Paint().apply {
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }

                // Wall - Light Warm Cream/Yellow
                fillPaint.color = Color.rgb(255, 241, 118)
                canvas.drawRect(120f, 220f, 392f, 440f, fillPaint)

                // Roof - Red
                fillPaint.color = Color.rgb(229, 57, 53)
                val roof = Path().apply {
                    moveTo(90f, 220f)
                    lineTo(256f, 80f)
                    lineTo(422f, 220f)
                    close()
                }
                canvas.drawPath(roof, fillPaint)

                // Door - Brown
                fillPaint.color = Color.rgb(141, 110, 99)
                canvas.drawRect(220f, 320f, 292f, 440f, fillPaint)

                // Windows - Bright Cyan
                fillPaint.color = Color.rgb(77, 208, 225)
                canvas.drawRect(150f, 260f, 200f, 310f, fillPaint)
                canvas.drawRect(312f, 260f, 362f, 310f, fillPaint)
            }
        ),

        // 5. CUTE FLOWER
        ColoringTemplate(
            id = "flower",
            name = "Pretty Flower",
            category = TemplateCategory.NATURE,
            drawOutline = { canvas, paint ->
                // Stem
                val stemPaint = Paint(paint).apply { strokeWidth = 14f }
                canvas.drawLine(256f, 256f, 256f, 460f, stemPaint)

                // Leaf
                val leaf = Path().apply {
                    moveTo(256f, 380f)
                    quadTo(340f, 340f, 360f, 380f)
                    quadTo(300f, 420f, 256f, 380f)
                }
                canvas.drawPath(leaf, paint)

                // Petals (6 petals)
                for (i in 0 until 6) {
                    val angle = Math.toRadians((i * 60).toDouble())
                    val px = (256 + 90 * Math.cos(angle)).toFloat()
                    val py = (256 + 90 * Math.sin(angle)).toFloat()
                    canvas.drawCircle(px, py, 45f, paint)
                }

                // Flower Center
                canvas.drawCircle(256f, 256f, 50f, paint)
            },
            drawTargetReference = { canvas ->
                val fillPaint = Paint().apply {
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }

                // Stem - Dark Green
                val stemPaint = Paint().apply {
                    color = Color.rgb(56, 142, 60)
                    style = Paint.Style.STROKE
                    strokeWidth = 18f
                    strokeCap = Paint.Cap.ROUND
                    isAntiAlias = true
                }
                canvas.drawLine(256f, 256f, 256f, 460f, stemPaint)

                // Leaf - Green
                fillPaint.color = Color.rgb(102, 187, 106)
                val leaf = Path().apply {
                    moveTo(256f, 380f)
                    quadTo(340f, 340f, 360f, 380f)
                    quadTo(300f, 420f, 256f, 380f)
                    close()
                }
                canvas.drawPath(leaf, fillPaint)

                // Petals - Bright Pink / Magenta
                fillPaint.color = Color.rgb(236, 64, 122)
                for (i in 0 until 6) {
                    val angle = Math.toRadians((i * 60).toDouble())
                    val px = (256 + 90 * Math.cos(angle)).toFloat()
                    val py = (256 + 90 * Math.sin(angle)).toFloat()
                    canvas.drawCircle(px, py, 45f, fillPaint)
                }

                // Center - Yellow
                fillPaint.color = Color.rgb(255, 238, 88)
                canvas.drawCircle(256f, 256f, 50f, fillPaint)
            }
        )
    )

    private fun createStarPath(cx: Float, cy: Float, points: Int, outerRadius: Float, innerRadius: Float): Path {
        val path = Path()
        val angleStep = Math.PI / points
        var angle = -Math.PI / 2

        for (i in 0 until points * 2) {
            val r = if (i % 2 == 0) outerRadius else innerRadius
            val x = cx + (r * Math.cos(angle)).toFloat()
            val y = cy + (r * Math.sin(angle)).toFloat()

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
            angle += angleStep
        }
        path.close()
        return path
    }
}
