package com.example.fillcolor.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        shadowElevation = 16.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Control Row: Tool Selection Cards & Actions (Undo, Clear)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tool Toggle Buttons: Bucket Fill, Pencil/Brush, Eraser
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ToolButton(
                        icon = "🪣",
                        label = "Fill",
                        isSelected = selectedTool == ColoringTool.FILL,
                        onClick = { onToolSelected(ColoringTool.FILL) }
                    )
                    ToolButton(
                        icon = "✏️",
                        label = "Pencil",
                        isSelected = selectedTool == ColoringTool.BRUSH,
                        onClick = { onToolSelected(ColoringTool.BRUSH) }
                    )
                    ToolButton(
                        icon = "🧽",
                        label = "Eraser",
                        isSelected = selectedTool == ColoringTool.ERASER,
                        onClick = { onToolSelected(ColoringTool.ERASER) }
                    )
                }

                // Action Buttons: Undo & Clear
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ActionChip(
                        icon = "↩️",
                        label = "Undo",
                        onClick = onUndoClick
                    )
                    ActionChip(
                        icon = "🗑️",
                        label = "Clear",
                        onClick = onClearClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Size Selector (Visible for Pencil/Brush or Eraser mode)
            if (selectedTool == ColoringTool.BRUSH || selectedTool == ColoringTool.ERASER) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (selectedTool == ColoringTool.ERASER) "Eraser Size:" else "Pencil Size:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    listOf(12f to "Small", 28f to "Medium", 48f to "Large").forEach { (width, name) ->
                        val isSelected = selectedBrushWidth == width
                        val dotSize = when (name) {
                            "Small" -> 10.dp
                            "Medium" -> 18.dp
                            else -> 26.dp
                        }
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                                .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { onBrushWidthSelected(width) },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(dotSize)
                                    .clip(CircleShape)
                                    .background(if (selectedTool == ColoringTool.ERASER) Color.Gray else selectedColor)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
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
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(colorSize)
                                .clip(CircleShape)
                                .background(paletteColor.color)
                                .border(
                                    width = if (isSelected) 3.5.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
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
    icon: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.05f else 1.0f, label = "scale")
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        label = "bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "border"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = bgColor,
        shadowElevation = if (isSelected) 4.dp else 0.dp,
        modifier = Modifier
            .scale(scale)
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(18.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 18.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ActionChip(
    icon: String,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
