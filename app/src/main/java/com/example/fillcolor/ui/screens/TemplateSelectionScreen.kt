package com.example.fillcolor.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fillcolor.model.ColoringTemplate
import com.example.fillcolor.model.LevelInfo
import com.example.fillcolor.model.TemplateRepository
import com.example.fillcolor.model.levelList
import com.example.fillcolor.utils.LevelManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateSelectionScreen(
    onSelectTemplate: (ColoringTemplate) -> Unit
) {
    var selectedLevelNumber by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (selectedLevelNumber == null) "Select Level 🏆" else "Level $selectedLevelNumber Pictures 🖼️",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                navigationIcon = {
                    if (selectedLevelNumber != null) {
                        Button(
                            onClick = { selectedLevelNumber = null },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            modifier = Modifier.padding(start = 8.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("← Levels")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
        ) {
            val currentLevel = selectedLevelNumber

            if (currentLevel == null) {
                // List of 20 Levels
                LevelListScreen(
                    onSelectLevel = { levelNum ->
                        selectedLevelNumber = levelNum
                    }
                )
            } else {
                // 5 Images in selected level
                LevelDetailGridScreen(
                    levelNumber = currentLevel,
                    onSelectTemplate = onSelectTemplate
                )
            }
        }
    }
}

@Composable
private fun LevelListScreen(
    onSelectLevel: (Int) -> Unit
) {
    val templateIdsMap = remember { TemplateRepository.templateIdsByLevel }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(levelList) { levelInfo ->
            val isUnlocked = LevelManager.isLevelUnlocked(levelInfo.levelNumber, templateIdsMap)
            val completedCount = LevelManager.getCompletedCountInLevel(levelInfo.levelNumber, templateIdsMap)
            val isFullyCompleted = completedCount == 5

            LevelCard(
                levelInfo = levelInfo,
                isUnlocked = isUnlocked,
                completedCount = completedCount,
                isFullyCompleted = isFullyCompleted,
                onClick = {
                    if (isUnlocked) {
                        onSelectLevel(levelInfo.levelNumber)
                    }
                }
            )
        }
    }
}

@Composable
private fun LevelCard(
    levelInfo: LevelInfo,
    isUnlocked: Boolean,
    completedCount: Int,
    isFullyCompleted: Boolean,
    onClick: () -> Unit
) {
    val cardBg = if (isUnlocked) {
        if (isFullyCompleted) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        else MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isUnlocked) { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 4.dp else 1.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Level Icon Badge
            Surface(
                shape = CircleShape,
                color = if (isUnlocked) MaterialTheme.colorScheme.primaryContainer else Color.LightGray.copy(alpha = 0.5f),
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isUnlocked) levelInfo.icon else "🔒",
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Level Text & Progress
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Level ${levelInfo.levelNumber}: ${levelInfo.title}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (isUnlocked) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isFullyCompleted) "Completed! 🌟" else "$completedCount / 5 Pictures",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isFullyCompleted) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    LinearProgressIndicator(
                        progress = { completedCount / 5f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = if (isFullyCompleted) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.outlineVariant
                    )
                } else {
                    Text(
                        text = "Complete Level ${levelInfo.levelNumber - 1} to unlock 🔒",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun LevelDetailGridScreen(
    levelNumber: Int,
    onSelectTemplate: (ColoringTemplate) -> Unit
) {
    val levelTemplates = remember(levelNumber) {
        TemplateRepository.templatesByLevel[levelNumber] ?: emptyList()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(levelTemplates) { template ->
            val isDone = LevelManager.isImageCompleted(template.id)

            TemplateCard(
                template = template,
                isCompleted = isDone,
                onClick = { onSelectTemplate(template) }
            )
        }
    }
}

@Composable
private fun TemplateCard(
    template: ColoringTemplate,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    val targetBitmap = remember(template) { template.createTargetReferenceBitmap() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(
                        width = 2.dp,
                        color = if (isCompleted) Color(0xFF4CAF50) else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = targetBitmap.asImageBitmap(),
                    contentDescription = template.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                if (isCompleted) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp),
                        shape = CircleShape,
                        color = Color(0xFF4CAF50)
                    ) {
                        Text(
                            text = "✓",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = template.name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(vertical = 6.dp)
            ) {
                Text(
                    text = if (isCompleted) "Re-color 🎨" else "Color Me! ✏️",
                    fontSize = 13.sp
                )
            }
        }
    }
}
