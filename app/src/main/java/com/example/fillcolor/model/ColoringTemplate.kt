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
            listOf("Juicy Apple", "Sweet Banana", "Fresh Orange", "Ripe Strawberry", "Golden Pear"),
            listOf("Happy Star", "Radiant Sun", "Loving Heart", "Shining Diamond", "Crescent Moon"),
            listOf("Pretty Flower", "Sweet Tulip", "Bright Sunflower", "Fluffy Cloud", "Cute Mushroom"),
            listOf("Kitty Cat", "Puppy Dog", "Little Fish", "Gentle Butterfly", "Rubber Duckie"),
            listOf("Sweet Cupcake", "Ice Cream Cone", "Glazed Donut", "Wrapped Candy", "Swirl Lollipop"),
            listOf("Speedy Car", "Space Rocket", "Sailboat", "Choo-Choo Train", "Flying Airplane"),
            listOf("Cozy House", "Royal Castle", "Tall Lighthouse", "Dutch Windmill", "Snowy Igloo"),
            listOf("Crunchy Carrot", "Juicy Tomato", "Green Broccoli", "Purple Eggplant", "Sweet Corn"),
            listOf("Red Crab", "Ocean Starfish", "Jellyfish", "Sea Turtle", "Friendly Whale"),
            listOf("Ringed Saturn", "Night Sky", "Space Helmet", "Flying Saucer UFO", "Shooting Star"),
            listOf("Teddy Bear", "Party Balloon", "Toy Blocks", "Soaring Kite", "Spinning Top"),
            listOf("Acoustic Guitar", "Marching Drum", "Paint Palette", "Jingle Bell", "Musical Notes"),
            listOf("Magic Rainbow", "Happy Snowman", "Flash Lightning", "Rain Umbrella", "Swirling Wind"),
            listOf("Yummy Burger", "Pizza Slice", "French Fries", "Hot Dog", "Cold Soda Cup"),
            listOf("Mighty Oak Tree", "Pine Tree", "Autumn Leaf", "Oak Acorn", "Desert Cactus"),
            listOf("Basketball", "Soccer Ball", "Tennis Racket", "Bowling Pin", "Gold Trophy"),
            listOf("Party Cone Hat", "Present Box", "Birthday Cake", "Gift Ribbon", "Party Popper"),
            listOf("Spotted Ladybug", "Busy Honey Bee", "Caterpillar", "Garden Snail", "Dragonfly"),
            listOf("Cool T-Shirt", "Golden Crown", "Magic Wand", "Winter Boot", "Fun Glasses"),
            listOf("First Place Medal", "Diamond Ring", "Treasure Chest", "Super Rocket", "Grand Gem")
        )

        val levelNames = names.getOrNull(level - 1) ?: listOf("Image 1", "Image 2", "Image 3", "Image 4", "Image 5")
        val name = levelNames.getOrNull(index - 1) ?: "Image $index"

        val outlineDrawer: (Canvas, Paint) -> Unit = { canvas, paint ->
            drawSpecificTemplateOutline(canvas, paint, level, index)
        }

        val targetDrawer: (Canvas) -> Unit = { canvas ->
            drawSpecificTemplateTarget(canvas, level, index)
        }

        return Triple(name, outlineDrawer, targetDrawer)
    }

    private fun drawSpecificTemplateOutline(canvas: Canvas, paint: Paint, level: Int, index: Int) {

        when (level) {
            1 -> { // Fruits
                when (index) {
                    1 -> drawAppleOutline(canvas, paint)
                    2 -> drawBananaOutline(canvas, paint)
                    3 -> drawOrangeOutline(canvas, paint)
                    4 -> drawStrawberryOutline(canvas, paint)
                    else -> drawPearOutline(canvas, paint)
                }
            }
            2 -> { // Shapes
                when (index) {
                    1 -> drawStarOutline(canvas, paint)
                    2 -> drawSunOutline(canvas, paint)
                    3 -> drawHeartOutline(canvas, paint)
                    4 -> drawDiamondOutline(canvas, paint)
                    else -> drawMoonOutline(canvas, paint)
                }
            }
            3 -> { // Flowers & Nature
                when (index) {
                    1 -> drawFlowerOutline(canvas, paint)
                    2 -> drawTulipOutline(canvas, paint)
                    3 -> drawSunflowerOutline(canvas, paint)
                    4 -> drawCloudOutline(canvas, paint)
                    else -> drawMushroomOutline(canvas, paint)
                }
            }
            7 -> { // Houses
                when (index) {
                    1 -> drawHouseOutline(canvas, paint)
                    2 -> drawCastleOutline(canvas, paint)
                    else -> drawHouseOutline(canvas, paint)
                }
            }
            else -> { // Procedural distinctive shapes
                drawGenericShapeOutline(canvas, paint, level, index)
            }
        }
    }

    private fun drawSpecificTemplateTarget(canvas: Canvas, level: Int, index: Int) {

        when (level) {
            1 -> { // Fruits
                when (index) {
                    1 -> drawAppleTarget(canvas)
                    2 -> drawBananaTarget(canvas)
                    3 -> drawOrangeTarget(canvas)
                    4 -> drawStrawberryTarget(canvas)
                    else -> drawPearTarget(canvas)
                }
            }
            2 -> { // Shapes
                when (index) {
                    1 -> drawStarTarget(canvas)
                    2 -> drawSunTarget(canvas)
                    3 -> drawHeartTarget(canvas)
                    4 -> drawDiamondTarget(canvas)
                    else -> drawMoonTarget(canvas)
                }
            }
            3 -> { // Flowers
                when (index) {
                    1 -> drawFlowerTarget(canvas)
                    2 -> drawTulipTarget(canvas)
                    3 -> drawSunflowerTarget(canvas)
                    4 -> drawCloudTarget(canvas)
                    else -> drawMushroomTarget(canvas)
                }
            }
            7 -> { // Houses
                when (index) {
                    1 -> drawHouseTarget(canvas)
                    else -> drawHouseTarget(canvas)
                }
            }
            else -> {
                drawGenericShapeTarget(canvas, level, index)
            }
        }
    }

    // --- LEVEL 1: FRUITS ---
    private fun drawAppleOutline(canvas: Canvas, paint: Paint) {
        canvas.drawCircle(210f, 280f, 120f, paint)
        canvas.drawCircle(302f, 280f, 120f, paint)
        val bottom = Path().apply {
            moveTo(130f, 320f)
            cubicTo(170f, 440f, 342f, 440f, 382f, 320f)
        }
        canvas.drawPath(bottom, paint)
        val stem = Path().apply {
            moveTo(256f, 170f)
            quadTo(270f, 110f, 290f, 80f)
        }
        canvas.drawPath(stem, paint)
        val leaf = Path().apply {
            moveTo(270f, 120f)
            quadTo(340f, 90f, 370f, 120f)
            quadTo(310f, 160f, 270f, 120f)
        }
        canvas.drawPath(leaf, paint)
    }

    private fun drawAppleTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; isAntiAlias = true }
        fill.color = Color.rgb(229, 57, 53) // Red
        canvas.drawCircle(210f, 280f, 120f, fill)
        canvas.drawCircle(302f, 280f, 120f, fill)
        val bottom = Path().apply {
            moveTo(130f, 320f)
            cubicTo(170f, 440f, 342f, 440f, 382f, 320f)
            lineTo(382f, 280f)
            lineTo(130f, 280f)
            close()
        }
        canvas.drawPath(bottom, fill)
        val stemPaint = Paint().apply {
            color = Color.rgb(121, 85, 72)
            style = Paint.Style.STROKE
            strokeWidth = 14f
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }
        val stem = Path().apply {
            moveTo(256f, 170f)
            quadTo(270f, 110f, 290f, 80f)
        }
        canvas.drawPath(stem, stemPaint)
        fill.color = Color.rgb(76, 175, 80) // Green Leaf
        val leaf = Path().apply {
            moveTo(270f, 120f)
            quadTo(340f, 90f, 370f, 120f)
            quadTo(310f, 160f, 270f, 120f)
            close()
        }
        canvas.drawPath(leaf, fill)
    }

    private fun drawBananaOutline(canvas: Canvas, paint: Paint) {
        val banana = Path().apply {
            moveTo(140f, 160f)
            cubicTo(180f, 400f, 380f, 420f, 420f, 240f)
            cubicTo(340f, 360f, 200f, 340f, 160f, 160f)
            close()
        }
        canvas.drawPath(banana, paint)
    }

    private fun drawBananaTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; color = Color.rgb(255, 235, 59); isAntiAlias = true }
        val banana = Path().apply {
            moveTo(140f, 160f)
            cubicTo(180f, 400f, 380f, 420f, 420f, 240f)
            cubicTo(340f, 360f, 200f, 340f, 160f, 160f)
            close()
        }
        canvas.drawPath(banana, fill)
    }

    private fun drawOrangeOutline(canvas: Canvas, paint: Paint) {
        canvas.drawCircle(256f, 280f, 140f, paint)
        canvas.drawCircle(256f, 130f, 20f, paint) // Stem/Leaf
    }

    private fun drawOrangeTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; color = Color.rgb(255, 152, 0); isAntiAlias = true }
        canvas.drawCircle(256f, 280f, 140f, fill)
        fill.color = Color.rgb(76, 175, 80)
        canvas.drawCircle(256f, 130f, 20f, fill)
    }

    private fun drawStrawberryOutline(canvas: Canvas, paint: Paint) {
        val strawberry = Path().apply {
            moveTo(256f, 420f)
            cubicTo(100f, 320f, 120f, 180f, 256f, 180f)
            cubicTo(392f, 180f, 412f, 320f, 256f, 420f)
            close()
        }
        canvas.drawPath(strawberry, paint)
        // Green top cap
        canvas.drawCircle(256f, 160f, 30f, paint)
    }

    private fun drawStrawberryTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; color = Color.rgb(229, 57, 53); isAntiAlias = true }
        val strawberry = Path().apply {
            moveTo(256f, 420f)
            cubicTo(100f, 320f, 120f, 180f, 256f, 180f)
            cubicTo(392f, 180f, 412f, 320f, 256f, 420f)
            close()
        }
        canvas.drawPath(strawberry, fill)
        fill.color = Color.rgb(76, 175, 80)
        canvas.drawCircle(256f, 160f, 30f, fill)
    }

    private fun drawPearOutline(canvas: Canvas, paint: Paint) {
        canvas.drawCircle(256f, 200f, 70f, paint)
        canvas.drawCircle(256f, 320f, 120f, paint)
        val leaf = Path().apply {
            moveTo(256f, 120f)
            quadTo(300f, 90f, 320f, 120f)
            quadTo(280f, 150f, 256f, 120f)
        }
        canvas.drawPath(leaf, paint)
    }

    private fun drawPearTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; color = Color.rgb(205, 220, 57); isAntiAlias = true }
        canvas.drawCircle(256f, 200f, 70f, fill)
        canvas.drawCircle(256f, 320f, 120f, fill)
        fill.color = Color.rgb(76, 175, 80)
        val leaf = Path().apply {
            moveTo(256f, 120f)
            quadTo(300f, 90f, 320f, 120f)
            quadTo(280f, 150f, 256f, 120f)
            close()
        }
        canvas.drawPath(leaf, fill)
    }

    // --- LEVEL 2: SHAPES ---
    private fun drawStarOutline(canvas: Canvas, paint: Paint) {
        val star = createStarPath(256f, 256f, 5, 180f, 85f)
        canvas.drawPath(star, paint)
    }

    private fun drawStarTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; color = Color.rgb(255, 213, 79); isAntiAlias = true }
        val star = createStarPath(256f, 256f, 5, 180f, 85f)
        canvas.drawPath(star, fill)
    }

    private fun drawSunOutline(canvas: Canvas, paint: Paint) {
        canvas.drawCircle(256f, 256f, 100f, paint)
        for (i in 0 until 8) {
            val angle = Math.toRadians((i * 45).toDouble())
            val sx = (256 + 110 * Math.cos(angle)).toFloat()
            val sy = (256 + 110 * Math.sin(angle)).toFloat()
            val ex = (256 + 150 * Math.cos(angle)).toFloat()
            val ey = (256 + 150 * Math.sin(angle)).toFloat()
            canvas.drawLine(sx, sy, ex, ey, paint)
        }
    }

    private fun drawSunTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; color = Color.rgb(255, 235, 59); isAntiAlias = true }
        canvas.drawCircle(256f, 256f, 100f, fill)
        val stroke = Paint().apply {
            color = Color.rgb(255, 152, 0)
            style = Paint.Style.STROKE
            strokeWidth = 14f
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }
        for (i in 0 until 8) {
            val angle = Math.toRadians((i * 45).toDouble())
            val sx = (256 + 110 * Math.cos(angle)).toFloat()
            val sy = (256 + 110 * Math.sin(angle)).toFloat()
            val ex = (256 + 150 * Math.cos(angle)).toFloat()
            val ey = (256 + 150 * Math.sin(angle)).toFloat()
            canvas.drawLine(sx, sy, ex, ey, stroke)
        }
    }

    private fun drawHeartOutline(canvas: Canvas, paint: Paint) {
        val heart = Path().apply {
            moveTo(256f, 180f)
            cubicTo(256f, 120f, 120f, 100f, 120f, 220f)
            cubicTo(120f, 320f, 256f, 400f, 256f, 420f)
            cubicTo(256f, 400f, 392f, 320f, 392f, 220f)
            cubicTo(392f, 100f, 256f, 120f, 256f, 180f)
            close()
        }
        canvas.drawPath(heart, paint)
    }

    private fun drawHeartTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; color = Color.rgb(233, 30, 99); isAntiAlias = true }
        val heart = Path().apply {
            moveTo(256f, 180f)
            cubicTo(256f, 120f, 120f, 100f, 120f, 220f)
            cubicTo(120f, 320f, 256f, 400f, 256f, 420f)
            cubicTo(256f, 400f, 392f, 320f, 392f, 220f)
            cubicTo(392f, 100f, 256f, 120f, 256f, 180f)
            close()
        }
        canvas.drawPath(heart, fill)
    }

    private fun drawDiamondOutline(canvas: Canvas, paint: Paint) {
        val diamond = Path().apply {
            moveTo(256f, 100f)
            lineTo(412f, 256f)
            lineTo(256f, 412f)
            lineTo(100f, 256f)
            close()
        }
        canvas.drawPath(diamond, paint)
    }

    private fun drawDiamondTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; color = Color.rgb(0, 188, 212); isAntiAlias = true }
        val diamond = Path().apply {
            moveTo(256f, 100f)
            lineTo(412f, 256f)
            lineTo(256f, 412f)
            lineTo(100f, 256f)
            close()
        }
        canvas.drawPath(diamond, fill)
    }

    private fun drawMoonOutline(canvas: Canvas, paint: Paint) {
        val moon = Path().apply {
            moveTo(320f, 100f)
            cubicTo(180f, 140f, 180f, 370f, 320f, 412f)
            cubicTo(220f, 360f, 220f, 150f, 320f, 100f)
            close()
        }
        canvas.drawPath(moon, paint)
    }

    private fun drawMoonTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; color = Color.rgb(255, 235, 59); isAntiAlias = true }
        val moon = Path().apply {
            moveTo(320f, 100f)
            cubicTo(180f, 140f, 180f, 370f, 320f, 412f)
            cubicTo(220f, 360f, 220f, 150f, 320f, 100f)
            close()
        }
        canvas.drawPath(moon, fill)
    }

    // --- LEVEL 3: FLOWERS ---
    private fun drawFlowerOutline(canvas: Canvas, paint: Paint) {
        canvas.drawLine(256f, 256f, 256f, 460f, paint)
        for (i in 0 until 6) {
            val angle = Math.toRadians((i * 60).toDouble())
            val px = (256 + 80 * Math.cos(angle)).toFloat()
            val py = (256 + 80 * Math.sin(angle)).toFloat()
            canvas.drawCircle(px, py, 40f, paint)
        }
        canvas.drawCircle(256f, 256f, 45f, paint)
    }

    private fun drawFlowerTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; isAntiAlias = true }
        val stem = Paint().apply { color = Color.rgb(56, 142, 60); strokeWidth = 14f; style = Paint.Style.STROKE }
        canvas.drawLine(256f, 256f, 256f, 460f, stem)
        fill.color = Color.rgb(236, 64, 122)
        for (i in 0 until 6) {
            val angle = Math.toRadians((i * 60).toDouble())
            val px = (256 + 80 * Math.cos(angle)).toFloat()
            val py = (256 + 80 * Math.sin(angle)).toFloat()
            canvas.drawCircle(px, py, 40f, fill)
        }
        fill.color = Color.rgb(255, 238, 88)
        canvas.drawCircle(256f, 256f, 45f, fill)
    }

    private fun drawTulipOutline(canvas: Canvas, paint: Paint) {
        canvas.drawLine(256f, 280f, 256f, 460f, paint)
        val tulip = Path().apply {
            moveTo(180f, 180f)
            lineTo(220f, 280f)
            lineTo(292f, 280f)
            lineTo(332f, 180f)
            lineTo(280f, 220f)
            lineTo(256f, 180f)
            lineTo(232f, 220f)
            close()
        }
        canvas.drawPath(tulip, paint)
    }

    private fun drawTulipTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; isAntiAlias = true }
        val stem = Paint().apply { color = Color.rgb(56, 142, 60); strokeWidth = 14f; style = Paint.Style.STROKE }
        canvas.drawLine(256f, 280f, 256f, 460f, stem)
        fill.color = Color.rgb(229, 57, 53)
        val tulip = Path().apply {
            moveTo(180f, 180f)
            lineTo(220f, 280f)
            lineTo(292f, 280f)
            lineTo(332f, 180f)
            lineTo(280f, 220f)
            lineTo(256f, 180f)
            lineTo(232f, 220f)
            close()
        }
        canvas.drawPath(tulip, fill)
    }

    private fun drawSunflowerOutline(canvas: Canvas, paint: Paint) {
        for (i in 0 until 12) {
            val angle = Math.toRadians((i * 30).toDouble())
            val px = (256 + 110 * Math.cos(angle)).toFloat()
            val py = (256 + 110 * Math.sin(angle)).toFloat()
            canvas.drawCircle(px, py, 35f, paint)
        }
        canvas.drawCircle(256f, 256f, 85f, paint)
    }

    private fun drawSunflowerTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; isAntiAlias = true }
        fill.color = Color.rgb(255, 193, 7)
        for (i in 0 until 12) {
            val angle = Math.toRadians((i * 30).toDouble())
            val px = (256 + 110 * Math.cos(angle)).toFloat()
            val py = (256 + 110 * Math.sin(angle)).toFloat()
            canvas.drawCircle(px, py, 35f, fill)
        }
        fill.color = Color.rgb(121, 85, 72)
        canvas.drawCircle(256f, 256f, 85f, fill)
    }

    private fun drawCloudOutline(canvas: Canvas, paint: Paint) {
        val cloud = Path().apply {
            moveTo(160f, 320f)
            lineTo(360f, 320f)
            cubicTo(400f, 320f, 400f, 260f, 360f, 250f)
            cubicTo(370f, 190f, 300f, 170f, 270f, 210f)
            cubicTo(240f, 170f, 170f, 200f, 180f, 250f)
            cubicTo(140f, 260f, 130f, 320f, 160f, 320f)
            close()
        }
        canvas.drawPath(cloud, paint)
    }

    private fun drawCloudTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; color = Color.rgb(129, 212, 250); isAntiAlias = true }
        val cloud = Path().apply {
            moveTo(160f, 320f)
            lineTo(360f, 320f)
            cubicTo(400f, 320f, 400f, 260f, 360f, 250f)
            cubicTo(370f, 190f, 300f, 170f, 270f, 210f)
            cubicTo(240f, 170f, 170f, 200f, 180f, 250f)
            cubicTo(140f, 260f, 130f, 320f, 160f, 320f)
            close()
        }
        canvas.drawPath(cloud, fill)
    }

    private fun drawMushroomOutline(canvas: Canvas, paint: Paint) {
        canvas.drawRect(210f, 280f, 302f, 420f, paint) // Stem
        val cap = Path().apply {
            moveTo(130f, 280f)
            cubicTo(130f, 120f, 382f, 120f, 382f, 280f)
            close()
        }
        canvas.drawPath(cap, paint)
        canvas.drawCircle(210f, 200f, 20f, paint)
        canvas.drawCircle(300f, 210f, 24f, paint)
    }

    private fun drawMushroomTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; isAntiAlias = true }
        fill.color = Color.rgb(238, 238, 238)
        canvas.drawRect(210f, 280f, 302f, 420f, fill)
        fill.color = Color.rgb(229, 57, 53)
        val cap = Path().apply {
            moveTo(130f, 280f)
            cubicTo(130f, 120f, 382f, 120f, 382f, 280f)
            close()
        }
        canvas.drawPath(cap, fill)
        fill.color = Color.WHITE
        canvas.drawCircle(210f, 200f, 20f, fill)
        canvas.drawCircle(300f, 210f, 24f, fill)
    }

    // --- LEVEL 7: HOUSE & CASTLE ---
    private fun drawHouseOutline(canvas: Canvas, paint: Paint) {
        canvas.drawRect(120f, 220f, 392f, 440f, paint)
        val roof = Path().apply {
            moveTo(90f, 220f)
            lineTo(256f, 80f)
            lineTo(422f, 220f)
            close()
        }
        canvas.drawPath(roof, paint)
        canvas.drawRect(220f, 320f, 292f, 440f, paint) // Door
        canvas.drawRect(150f, 260f, 200f, 310f, paint) // Window
        canvas.drawRect(312f, 260f, 362f, 310f, paint) // Window
    }

    private fun drawHouseTarget(canvas: Canvas) {
        val fill = Paint().apply { style = Paint.Style.FILL; isAntiAlias = true }
        fill.color = Color.rgb(255, 241, 118)
        canvas.drawRect(120f, 220f, 392f, 440f, fill)
        fill.color = Color.rgb(229, 57, 53)
        val roof = Path().apply {
            moveTo(90f, 220f)
            lineTo(256f, 80f)
            lineTo(422f, 220f)
            close()
        }
        canvas.drawPath(roof, fill)
        fill.color = Color.rgb(141, 110, 99)
        canvas.drawRect(220f, 320f, 292f, 440f, fill)
        fill.color = Color.rgb(77, 208, 225)
        canvas.drawRect(150f, 260f, 200f, 310f, fill)
        canvas.drawRect(312f, 260f, 362f, 310f, fill)
    }

    private fun drawCastleOutline(canvas: Canvas, paint: Paint) {
        canvas.drawRect(120f, 220f, 392f, 440f, paint)
        canvas.drawRect(90f, 140f, 170f, 440f, paint)
        canvas.drawRect(342f, 140f, 422f, 440f, paint)
        canvas.drawRect(210f, 320f, 302f, 440f, paint)
    }

    // --- PROCEDURAL GENERATOR FOR OTHER LEVELS ---
    private fun drawGenericShapeOutline(canvas: Canvas, paint: Paint, level: Int, index: Int) {
        val cx = 256f
        val cy = 256f

        val shapeType = (level * 5 + index) % 7
        when (shapeType) {
            0 -> {
                // Multi-circle flower
                canvas.drawCircle(cx, cy, 120f, paint)
                for (i in 0 until 4) {
                    val angle = Math.toRadians((i * 90).toDouble())
                    val px = (cx + 100 * Math.cos(angle)).toFloat()
                    val py = (cy + 100 * Math.sin(angle)).toFloat()
                    canvas.drawCircle(px, py, 40f, paint)
                }
            }
            1 -> {
                // Star shape
                val star = createStarPath(cx, cy, 6, 160f, 90f)
                canvas.drawPath(star, paint)
                canvas.drawCircle(cx, cy, 45f, paint)
            }
            2 -> {
                // Cube / Box
                canvas.drawRect(cx - 120f, cy - 120f, cx + 120f, cy + 120f, paint)
                canvas.drawCircle(cx, cy, 60f, paint)
            }
            3 -> {
                // Shield / Crest
                val path = Path().apply {
                    moveTo(cx, cy - 150f)
                    lineTo(cx + 140f, cy - 150f)
                    lineTo(cx + 140f, cy + 30f)
                    cubicTo(cx + 140f, cy + 140f, cx, cy + 180f, cx, cy + 180f)
                    cubicTo(cx, cy + 180f, cx - 140f, cy + 140f, cx - 140f, cy + 30f)
                    lineTo(cx - 140f, cy - 150f)
                    close()
                }
                canvas.drawPath(path, paint)
            }
            4 -> {
                // Triangles
                val path = Path().apply {
                    moveTo(cx, cy - 160f)
                    lineTo(cx + 150f, cy + 140f)
                    lineTo(cx - 150f, cy + 140f)
                    close()
                }
                canvas.drawPath(path, paint)
                canvas.drawCircle(cx, cy + 20f, 50f, paint)
            }
            5 -> {
                // Concentric circles
                canvas.drawCircle(cx, cy, 160f, paint)
                canvas.drawCircle(cx, cy, 110f, paint)
                canvas.drawCircle(cx, cy, 60f, paint)
            }
            else -> {
                // Diamond star
                val path = Path().apply {
                    moveTo(cx, cy - 160f)
                    lineTo(cx + 160f, cy)
                    lineTo(cx, cy + 160f)
                    lineTo(cx - 160f, cy)
                    close()
                }
                canvas.drawPath(path, paint)
                canvas.drawCircle(cx, cy, 70f, paint)
            }
        }
    }

    private fun drawGenericShapeTarget(canvas: Canvas, level: Int, index: Int) {
        val cx = 256f
        val cy = 256f
        val fill = Paint().apply { style = Paint.Style.FILL; isAntiAlias = true }

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

        val shapeType = (level * 5 + index) % 7
        when (shapeType) {
            0 -> {
                fill.color = mainColor
                canvas.drawCircle(cx, cy, 120f, fill)
                fill.color = accentColor
                for (i in 0 until 4) {
                    val angle = Math.toRadians((i * 90).toDouble())
                    val px = (cx + 100 * Math.cos(angle)).toFloat()
                    val py = (cy + 100 * Math.sin(angle)).toFloat()
                    canvas.drawCircle(px, py, 40f, fill)
                }
            }
            1 -> {
                fill.color = mainColor
                val star = createStarPath(cx, cy, 6, 160f, 90f)
                canvas.drawPath(star, fill)
                fill.color = accentColor
                canvas.drawCircle(cx, cy, 45f, fill)
            }
            2 -> {
                fill.color = mainColor
                canvas.drawRect(cx - 120f, cy - 120f, cx + 120f, cy + 120f, fill)
                fill.color = accentColor
                canvas.drawCircle(cx, cy, 60f, fill)
            }
            3 -> {
                fill.color = mainColor
                val path = Path().apply {
                    moveTo(cx, cy - 150f)
                    lineTo(cx + 140f, cy - 150f)
                    lineTo(cx + 140f, cy + 30f)
                    cubicTo(cx + 140f, cy + 140f, cx, cy + 180f, cx, cy + 180f)
                    cubicTo(cx, cy + 180f, cx - 140f, cy + 140f, cx - 140f, cy + 30f)
                    lineTo(cx - 140f, cy - 150f)
                    close()
                }
                canvas.drawPath(path, fill)
            }
            4 -> {
                fill.color = mainColor
                val path = Path().apply {
                    moveTo(cx, cy - 160f)
                    lineTo(cx + 150f, cy + 140f)
                    lineTo(cx - 150f, cy + 140f)
                    close()
                }
                canvas.drawPath(path, fill)
                fill.color = accentColor
                canvas.drawCircle(cx, cy + 20f, 50f, fill)
            }
            5 -> {
                fill.color = mainColor
                canvas.drawCircle(cx, cy, 160f, fill)
                fill.color = accentColor
                canvas.drawCircle(cx, cy, 110f, fill)
                fill.color = mainColor
                canvas.drawCircle(cx, cy, 60f, fill)
            }
            else -> {
                fill.color = mainColor
                val path = Path().apply {
                    moveTo(cx, cy - 160f)
                    lineTo(cx + 160f, cy)
                    lineTo(cx, cy + 160f)
                    lineTo(cx - 160f, cy)
                    close()
                }
                canvas.drawPath(path, fill)
                fill.color = accentColor
                canvas.drawCircle(cx, cy, 70f, fill)
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
