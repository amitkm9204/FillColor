package com.example.fillcolor.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ColoringTool {
    FILL, BRUSH, ERASER
}

data class PaletteColor(val color: Color, val name: String)

val kidPalette = listOf(
    PaletteColor(Color(0xFFE53935), "Red"),
    PaletteColor(Color(0xFFFF9800), "Orange"),
    PaletteColor(Color(0xFFFFEB3B), "Yellow"),
    PaletteColor(Color(0xFF4CAF50), "Green"),
    PaletteColor(Color(0xFF81D4FA), "Light Blue"),
    PaletteColor(Color(0xFF1E88E5), "Dark Blue"),
    PaletteColor(Color(0xFF8E24AA), "Purple"),
    PaletteColor(Color(0xFFEC407A), "Pink"),
    PaletteColor(Color(0xFF8D6E63), "Brown"),
    PaletteColor(Color(0xFF212121), "Black"),
    PaletteColor(Color(0xFFFFFFFF), "White")
)

@Composable
fun ColorPaletteBar(
    selectedTool: ColoringTool,
    onToolSelected: (ColoringTool) -> Unit,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    selectedBrushWidth: Float,
    onBrushWidthSelected: (Float) -> Unit,
    onUndoClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        shadowElevation = 12.dp,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Control Row: Tools (Fill, Brush, Eraser) & Actions (Undo, Clear)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tool Toggle Buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ToolButton(
                        label = "🪣 Fill",
                        isSelected = selectedTool == ColoringTool.FILL,
                        onClick = { onToolSelected(ColoringTool.FILL) }
                    )
                    ToolButton(
                        label = "🖌️ Brush",
                        isSelected = selectedTool == ColoringTool.BRUSH,
                        onClick = { onToolSelected(ColoringTool.BRUSH) }
                    )
                    ToolButton(
                        label = "🧹 Eraser",
                        isSelected = selectedTool == ColoringTool.ERASER,
                        onClick = { onToolSelected(ColoringTool.ERASER) }
                    )
                }

                // Undo & Clear Buttons
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ActionChip(
                        label = "↩️ Undo",
                        onClick = onUndoClick
                    )
                    ActionChip(
                        label = "🗑️ Clear",
                        onClick = onClearClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Brush Size Selector (Only visible if Brush or Eraser mode)
            if (selectedTool == ColoringTool.BRUSH || selectedTool == ColoringTool.ERASER) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Size:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    listOf(12f to "Small", 28f to "Medium", 48f to "Large").forEach { (width, name) ->
                        val isSelected = selectedBrushWidth == width
                        val sizeDp = when (name) {
                            "Small" -> 16.dp
                            "Medium" -> 24.dp
                            else -> 32.dp
                        }
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                                .clickable { onBrushWidthSelected(width) },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(sizeDp / 1.5f)
                                    .clip(CircleShape)
                                    .background(if (selectedTool == ColoringTool.ERASER) Color.Gray else selectedColor)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Color Swatches Row
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(kidPalette) { paletteColor ->
                    val isSelected = selectedColor == paletteColor.color && selectedTool != ColoringTool.ERASER
                    val colorSize by animateDpAsState(targetValue = if (isSelected) 44.dp else 36.dp, label = "size")

                    Box(
                        modifier = Modifier
                            .size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(colorSize)
                                .clip(CircleShape)
                                .background(paletteColor.color)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                                    shape = CircleShape
                                )
                                .clickable {
                                    onColorSelected(paletteColor.color)
                                    if (selectedTool == ColoringTool.ERASER) {
                                        onToolSelected(ColoringTool.BRUSH)
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ToolButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        label = "bg"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        label = "text"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        shadowElevation = if (isSelected) 4.dp else 1.dp
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = textColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun ActionChip(
    label: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
