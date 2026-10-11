package com.example.fillcolor.model

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path

enum class TemplateCategory {
    FRUIT, SHAPES, NATURE, ANIMALS, FOOD, VEHICLES, BUILDINGS, OCEAN, SPACE, TOYS, WEATHER, SPORTS, PARTY, MASTER
}

data class LevelInfo(
    val levelNumber: Int,
    val title: String,
    val icon: String
)

val levelList = listOf(
    LevelInfo(1, "Juicy Fruits", "🍎"),
    LevelInfo(2, "Happy Shapes", "⭐"),
    LevelInfo(3, "Flowers & Nature", "🌸"),
    LevelInfo(4, "Cute Animals", "🐱"),
    LevelInfo(5, "Yummy Treats", "🍦"),
    LevelInfo(6, "Cool Vehicles", "🚀"),
    LevelInfo(7, "Houses & Castles", "🏠"),
    LevelInfo(8, "Healthy Veggies", "🥕"),
    LevelInfo(9, "Ocean World", "🦀"),
    LevelInfo(10, "Outer Space", "🪐"),
    LevelInfo(11, "Fun Toys", "🧸"),
    LevelInfo(12, "Music & Art", "🎨"),
    LevelInfo(13, "Weather & Sky", "🌈"),
    LevelInfo(14, "Fast Food", "🍕"),
    LevelInfo(15, "Trees & Plants", "🌲"),
    LevelInfo(16, "Sports & Games", "⚽"),
    LevelInfo(17, "Birthday Party", "🎂"),
    LevelInfo(18, "Little Bugs", "🐞"),
    LevelInfo(19, "Magic & Dress Up", "👑"),
    LevelInfo(20, "Master Treasures", "🏆")
)

data class ColoringTemplate(
    val id: String,
    val name: String,
    val level: Int,
    val category: TemplateCategory,
    val width: Int = 512,
    val height: Int = 512,
    val drawOutline: (Canvas, Paint) -> Unit,
    val drawTargetReference: (Canvas) -> Unit
) {
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

    fun createTargetReferenceBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        drawTargetReference(canvas)

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

    val templates: List<ColoringTemplate> by lazy {
        generateAll100Templates()
    }

    val templatesByLevel: Map<Int, List<ColoringTemplate>> by lazy {
        templates.groupBy { it.level }
    }

    val templateIdsByLevel: Map<Int, List<String>> by lazy {
        templatesByLevel.mapValues { entry -> entry.value.map { it.id } }
    }

    private fun generateAll100Templates(): List<ColoringTemplate> {
        val list = mutableListOf<ColoringTemplate>()

        for (lvl in 1..20) {
            val levelInfo = levelList.find { it.levelNumber == lvl } ?: LevelInfo(lvl, "Level $lvl", "🎨")
            for (imgIdx in 1..5) {
                val templateId = "lvl_${lvl}_img_$imgIdx"
                val (name, drawOutlineFunc, drawTargetFunc) = getTemplateDrawers(lvl, imgIdx)

                list.add(
                    ColoringTemplate(
                        id = templateId,
                        name = name,
                        level = lvl,
                        category = getCategoryForLevel(lvl),
                        drawOutline = drawOutlineFunc,
                        drawTargetReference = drawTargetFunc
                    )
                )
            }
        }
        return list
    }

    private fun getCategoryForLevel(level: Int): TemplateCategory {
        return when (level) {
            1 -> TemplateCategory.FRUIT
            2 -> TemplateCategory.SHAPES
            3 -> TemplateCategory.NATURE
            4 -> TemplateCategory.ANIMALS
            5 -> TemplateCategory.FOOD
            6 -> TemplateCategory.VEHICLES
            7 -> TemplateCategory.BUILDINGS
            8 -> TemplateCategory.FOOD
            9 -> TemplateCategory.OCEAN
            10 -> TemplateCategory.SPACE
            11 -> TemplateCategory.TOYS
            12 -> TemplateCategory.SHAPES
            13 -> TemplateCategory.WEATHER
            14 -> TemplateCategory.FOOD
            15 -> TemplateCategory.NATURE
            16 -> TemplateCategory.SPORTS
            17 -> TemplateCategory.PARTY
            18 -> TemplateCategory.ANIMALS
            19 -> TemplateCategory.PARTY
            else -> TemplateCategory.MASTER
        }
    }

    private fun getTemplateDrawers(
        level: Int,
        index: Int
    ): Triple<String, (Canvas, Paint) -> Unit, (Canvas) -> Unit> {
        val names = listOf(
            // L1
            listOf("Juicy Apple", "Sweet Banana", "Fresh Orange", "Ripe Strawberry", "Golden Pear"),
            // L2
            listOf("Happy Star", "Radiant Sun", "Loving Heart", "Shining Diamond", "Crescent Moon"),
            // L3
            listOf("Pretty Flower", "Sweet Tulip", "Bright Sunflower", "Fluffy Cloud", "Cute Mushroom"),
            // L4
            listOf("Kitty Cat", "Puppy Dog", "Little Fish", "Gentle Butterfly", "Rubber Duckie"),
            // L5
            listOf("Sweet Cupcake", "Ice Cream Cone", "Glazed Donut", "Wrapped Candy", "Swirl Lollipop"),
            // L6
            listOf("Speedy Car", "Space Rocket", "Sailboat", "Choo-Choo Train", "Flying Airplane"),
            // L7
            listOf("Cozy House", "Royal Castle", "Tall Lighthouse", "Dutch Windmill", "Snowy Igloo"),
            // L8
            listOf("Crunchy Carrot", "Juicy Tomato", "Green Broccoli", "Purple Eggplant", "Sweet Corn"),
            // L9
            listOf("Red Crab", "Ocean Starfish", "Jellyfish", "Sea Turtle", "Friendly Whale"),
            // L10
            listOf("Ringed Saturn", "Night Sky", "Space Helmet", "Flying Saucer UFO", "Shooting Star"),
            // L11
            listOf("Teddy Bear", "Party Balloon", "Toy Blocks", "Soaring Kite", "Spinning Top"),
            // L12
            listOf("Acoustic Guitar", "Marching Drum", "Paint Palette", "Jingle Bell", "Musical Notes"),
            // L13
            listOf("Magic Rainbow", "Happy Snowman", "Flash Lightning", "Rain Umbrella", "Swirling Wind"),
            // L14
            listOf("Yummy Burger", "Pizza Slice", "French Fries", "Hot Dog", "Cold Soda Cup"),
            // L15
            listOf("Mighty Oak Tree", "Pine Tree", "Autumn Leaf", "Oak Acorn", "Desert Cactus"),
            // L16
            listOf("Basketball", "Soccer Ball", "Tennis Racket", "Bowling Pin", "Gold Trophy"),
            // L17
            listOf("Party Cone Hat", "Present Box", "Birthday Cake", "Gift Ribbon", "Party Popper"),
            // L18
            listOf("Spotted Ladybug", "Busy Honey Bee", "Caterpillar", "Garden Snail", "Dragonfly"),
            // L19
            listOf("Cool T-Shirt", "Golden Crown", "Magic Wand", "Winter Boot", "Fun Glasses"),
            // L20
            listOf("First Place Medal", "Diamond Ring", "Treasure Chest", "Super Rocket", "Grand Gem")
        )

        val levelNames = names.getOrNull(level - 1) ?: listOf("Image 1", "Image 2", "Image 3", "Image 4", "Image 5")
        val name = levelNames.getOrNull(index - 1) ?: "Image $index"

        // Generate distinctive shapes and colors for each template
        val outlineDrawer: (Canvas, Paint) -> Unit = { canvas, paint ->
            drawTemplateOutline(canvas, paint, level, index)
        }

        val targetDrawer: (Canvas) -> Unit = { canvas ->
            drawTemplateTarget(canvas, level, index)
        }

        return Triple(name, outlineDrawer, targetDrawer)
    }

    private fun drawTemplateOutline(canvas: Canvas, paint: Paint, level: Int, index: Int) {
        val cx = 256f
        val cy = 256f

        when ((level + index) % 6) {
            0 -> {
                // Circular design
                canvas.drawCircle(cx, cy, 140f, paint)
                canvas.drawCircle(cx, cy, 70f, paint)
                for (i in 0 until 4) {
                    val angle = Math.toRadians((i * 90).toDouble())
                    val x1 = cx + 70f * Math.cos(angle).toFloat()
                    val y1 = cy + 70f * Math.sin(angle).toFloat()
                    val x2 = cx + 140f * Math.cos(angle).toFloat()
                    val y2 = cy + 140f * Math.sin(angle).toFloat()
                    canvas.drawLine(x1, y1, x2, y2, paint)
                }
            }
            1 -> {
                // Star / Flower design
                val path = createStarPath(cx, cy, 5 + (index % 3), 160f, 80f)
                canvas.drawPath(path, paint)
                canvas.drawCircle(cx, cy, 40f, paint)
            }
            2 -> {
                // House / Box design
                canvas.drawRect(cx - 120f, cy - 60f, cx + 120f, cy + 140f, paint)
                val roof = Path().apply {
                    moveTo(cx - 140f, cy - 60f)
                    lineTo(cx, cy - 160f)
                    lineTo(cx + 140f, cy - 60f)
                    close()
                }
                canvas.drawPath(roof, paint)
                canvas.drawRect(cx - 30f, cy + 40f, cx + 30f, cy + 140f, paint)
            }
            3 -> {
                // Triple circles
                canvas.drawCircle(cx - 70f, cy, 90f, paint)
                canvas.drawCircle(cx + 70f, cy, 90f, paint)
                canvas.drawCircle(cx, cy - 80f, 70f, paint)
            }
            4 -> {
                // Shield / Heart
                val path = Path().apply {
                    moveTo(cx, cy - 140f)
                    cubicTo(cx + 140f, cy - 180f, cx + 180f, cy, cx, cy + 160f)
                    cubicTo(cx - 180f, cy, cx - 140f, cy - 180f, cx, cy - 140f)
                    close()
                }
                canvas.drawPath(path, paint)
            }
            else -> {
                // Diamond / Starburst
                val path = Path().apply {
                    moveTo(cx, cy - 160f)
                    lineTo(cx + 160f, cy)
                    lineTo(cx, cy + 160f)
                    lineTo(cx - 160f, cy)
                    close()
                }
                canvas.drawPath(path, paint)
                canvas.drawCircle(cx, cy, 80f, paint)
            }
        }
    }

    private fun drawTemplateTarget(canvas: Canvas, level: Int, index: Int) {
        val cx = 256f
        val cy = 256f
        val fillPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        // Distinct bright color themes for each level
        val levelColors = listOf(
            Color.rgb(229, 57, 53),   // Red
            Color.rgb(255, 152, 0),  // Orange
            Color.rgb(255, 235, 59),  // Yellow
            Color.rgb(76, 175, 80),   // Green
            Color.rgb(33, 150, 243),  // Blue
            Color.rgb(156, 39, 176),  // Purple
            Color.rgb(233, 30, 99)    // Pink
        )

        val mainColor = levelColors[(level + index) % levelColors.size]
        val accentColor = levelColors[(level + index + 2) % levelColors.size]

        when ((level + index) % 6) {
            0 -> {
                fillPaint.color = mainColor
                canvas.drawCircle(cx, cy, 140f, fillPaint)
                fillPaint.color = accentColor
                canvas.drawCircle(cx, cy, 70f, fillPaint)
            }
            1 -> {
                fillPaint.color = mainColor
                val path = createStarPath(cx, cy, 5 + (index % 3), 160f, 80f)
                canvas.drawPath(path, fillPaint)
                fillPaint.color = accentColor
                canvas.drawCircle(cx, cy, 40f, fillPaint)
            }
            2 -> {
                fillPaint.color = mainColor
                canvas.drawRect(cx - 120f, cy - 60f, cx + 120f, cy + 140f, fillPaint)
                fillPaint.color = accentColor
                val roof = Path().apply {
                    moveTo(cx - 140f, cy - 60f)
                    lineTo(cx, cy - 160f)
                    lineTo(cx + 140f, cy - 60f)
                    close()
                }
                canvas.drawPath(roof, fillPaint)
                fillPaint.color = Color.rgb(121, 85, 72)
                canvas.drawRect(cx - 30f, cy + 40f, cx + 30f, cy + 140f, fillPaint)
            }
            3 -> {
                fillPaint.color = mainColor
                canvas.drawCircle(cx - 70f, cy, 90f, fillPaint)
                canvas.drawCircle(cx + 70f, cy, 90f, fillPaint)
                fillPaint.color = accentColor
                canvas.drawCircle(cx, cy - 80f, 70f, fillPaint)
            }
            4 -> {
                fillPaint.color = mainColor
                val path = Path().apply {
                    moveTo(cx, cy - 140f)
                    cubicTo(cx + 140f, cy - 180f, cx + 180f, cy, cx, cy + 160f)
                    cubicTo(cx - 180f, cy, cx - 140f, cy - 180f, cx, cy - 140f)
                    close()
                }
                canvas.drawPath(path, fillPaint)
            }
            else -> {
                fillPaint.color = mainColor
                val path = Path().apply {
                    moveTo(cx, cy - 160f)
                    lineTo(cx + 160f, cy)
                    lineTo(cx, cy + 160f)
                    lineTo(cx - 160f, cy)
                    close()
                }
                canvas.drawPath(path, fillPaint)
                fillPaint.color = accentColor
                canvas.drawCircle(cx, cy, 80f, fillPaint)
            }
        }
    }

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
