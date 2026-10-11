package com.example.fillcolor.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fillcolor.model.ColoringTemplate
import com.example.fillcolor.ui.components.ColorPaletteBar
import com.example.fillcolor.ui.components.ColoringTool
import com.example.fillcolor.ui.components.ReferenceImageCard
import com.example.fillcolor.ui.components.ScoreDialog
import com.example.fillcolor.utils.ColorComparison
import com.example.fillcolor.utils.ColorMatchResult
import com.example.fillcolor.utils.FloodFill

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColoringScreen(
    template: ColoringTemplate,
    onBackClick: () -> Unit,
    onChooseAnotherPicture: () -> Unit
) {
    // Initializing bitmaps
    val outlineBitmap = remember(template) { template.createOutlineBitmap() }
    val targetBitmap = remember(template) { template.createTargetReferenceBitmap() }

    val userBitmap = remember(template) {
        val bitmap = Bitmap.createBitmap(template.width, template.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(AndroidColor.WHITE)
        bitmap
    }

    // State
    var canvasVersion by remember { mutableIntStateOf(0) }
    var selectedTool by remember { mutableStateOf(ColoringTool.FILL) }
    var selectedColor by remember { mutableStateOf(Color(0xFFE53935)) } // Default Red
    var brushWidth by remember { mutableFloatStateOf(28f) }
    var scoreResult by remember { mutableStateOf<ColorMatchResult?>(null) }

    // History stack for Undo
    val historyStack = remember { mutableStateListOf<Bitmap>() }

    fun saveHistoryState() {
        if (historyStack.size >= 10) {
            historyStack.removeAt(0)
        }
        historyStack.add(userBitmap.copy(userBitmap.config ?: Bitmap.Config.ARGB_8888, true))
    }

    fun undo() {
        if (historyStack.isNotEmpty()) {
            val previousState = historyStack.removeAt(historyStack.lastIndex)
            val canvas = Canvas(userBitmap)
            canvas.drawColor(AndroidColor.WHITE)
            canvas.drawBitmap(previousState, 0f, 0f, null)
            canvasVersion++
        }
    }

    fun clearCanvas() {
        saveHistoryState()
        val canvas = Canvas(userBitmap)
        canvas.drawColor(AndroidColor.WHITE)
        canvasVersion++
    }

    var canvasSizePx by remember { mutableStateOf(IntSize.Zero) }
    var currentDrawPath by remember { mutableStateOf<Path?>(null) }
    var currentDrawPaint by remember { mutableStateOf<Paint?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = template.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                navigationIcon = {
                    Button(
                        onClick = onBackClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        modifier = Modifier.padding(start = 8.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("← Back")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            scoreResult = ColorComparison.calculateScore(userBitmap, targetBitmap)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text("Check Score 🏆", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            ColorPaletteBar(
                selectedTool = selectedTool,
                onToolSelected = { selectedTool = it },
                selectedColor = selectedColor,
                onColorSelected = { selectedColor = it },
                selectedBrushWidth = brushWidth,
                onBrushWidthSelected = { brushWidth = it },
                onUndoClick = { undo() },
                onClearClick = { clearCanvas() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            // Main Coloring Canvas Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .aspectRatio(1f)
                    .shadow(8.dp, shape = RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .onSizeChanged { canvasSizePx = it },
                color = Color.White
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Compose Canvas Rendering user bitmap + outline
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(selectedTool, selectedColor, brushWidth) {
                                detectTapGestures { offset ->
                                    if (canvasSizePx.width > 0 && canvasSizePx.height > 0) {
                                        val scaleX = template.width.toFloat() / canvasSizePx.width
                                        val scaleY = template.height.toFloat() / canvasSizePx.height

                                        val bitmapX = (offset.x * scaleX).toInt().coerceIn(0, template.width - 1)
                                        val bitmapY = (offset.y * scaleY).toInt().coerceIn(0, template.height - 1)

                                        if (selectedTool == ColoringTool.FILL) {
                                            saveHistoryState()
                                            val filled = FloodFill.fill(
                                                bitmap = userBitmap,
                                                startX = bitmapX,
                                                startY = bitmapY,
                                                fillColor = selectedColor.toArgb()
                                            )
                                            if (filled) {
                                                canvasVersion++
                                            }
                                        }
                                    }
                                }
                            }
                            .pointerInput(selectedTool, selectedColor, brushWidth) {
                                if (selectedTool == ColoringTool.BRUSH || selectedTool == ColoringTool.ERASER) {
                                    detectDragGestures(
                                        onDragStart = { offset ->
                                            if (canvasSizePx.width > 0 && canvasSizePx.height > 0) {
                                                saveHistoryState()
                                                val scaleX = template.width.toFloat() / canvasSizePx.width
                                                val scaleY = template.height.toFloat() / canvasSizePx.height

                                                val bitmapX = offset.x * scaleX
                                                val bitmapY = offset.y * scaleY

                                                val path = Path().apply { moveTo(bitmapX, bitmapY) }
                                                val paint = Paint().apply {
                                                    color = if (selectedTool == ColoringTool.ERASER) AndroidColor.WHITE else selectedColor.toArgb()
                                                    style = Paint.Style.STROKE
                                                    strokeWidth = brushWidth
                                                    strokeCap = Paint.Cap.ROUND
                                                    strokeJoin = Paint.Join.ROUND
                                                    isAntiAlias = true
                                                }
                                                currentDrawPath = path
                                                currentDrawPaint = paint
                                            }
                                        },
                                        onDrag = { change, _ ->
                                            if (canvasSizePx.width > 0 && canvasSizePx.height > 0) {
                                                val scaleX = template.width.toFloat() / canvasSizePx.width
                                                val scaleY = template.height.toFloat() / canvasSizePx.height

                                                val bitmapX = change.position.x * scaleX
                                                val bitmapY = change.position.y * scaleY

                                                currentDrawPath?.lineTo(bitmapX, bitmapY)
                                                currentDrawPaint?.let { paint ->
                                                    currentDrawPath?.let { path ->
                                                        val canvas = Canvas(userBitmap)
                                                        canvas.drawPath(path, paint)
                                                    }
                                                }
                                                canvasVersion++
                                            }
                                        },
                                        onDragEnd = {
                                            currentDrawPath = null
                                            currentDrawPaint = null
                                        }
                                    )
                                }
                            }
                    ) {
                        // Trigger redraw when canvasVersion changes
                        @Suppress("UNUSED_VARIABLE")
                        val dummy = canvasVersion

                        // 1. Draw Kid's Colored Bitmap
                        drawImage(userBitmap.asImageBitmap())

                        // 2. Draw Black Line Outline overlay on top
                        drawImage(outlineBitmap.asImageBitmap())
                    }
                }
            }

            // Floating Reference Image Card in top-right corner
            ReferenceImageCard(
                targetBitmap = targetBitmap,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
            )
        }
    }

    // Score Modal Dialog
    scoreResult?.let { result ->
        ScoreDialog(
            result = result,
            onTryAgain = {
                scoreResult = null
                clearCanvas()
            },
            onChooseAnother = {
                scoreResult = null
                onChooseAnotherPicture()
            },
            onDismiss = { scoreResult = null }
        )
    }
}
