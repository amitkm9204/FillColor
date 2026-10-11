package com.example.fillcolor.utils

import android.content.Context
import android.content.SharedPreferences

object LevelManager {
    private const val PREF_NAME = "fill_color_level_prefs"
    private const val KEY_COMPLETED_PREFIX = "completed_img_"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isImageCompleted(templateId: String): Boolean {
        if (!::prefs.isInitialized) return false
        return prefs.getBoolean(KEY_COMPLETED_PREFIX + templateId, false)
    }

    fun markImageCompleted(templateId: String) {
        if (!::prefs.isInitialized) return
        prefs.edit().putBoolean(KEY_COMPLETED_PREFIX + templateId, true).apply()
    }

    /**
     * Checks if a level (1 to 20) is unlocked.
     * Level 1 is always unlocked.
     * Level N is unlocked only if all 5 images of Level N-1 are completed.
     */
    fun isLevelUnlocked(levelNumber: Int, levelTemplatesMap: Map<Int, List<String>>): Boolean {
        if (levelNumber <= 1) return true
        val previousLevelNumber = levelNumber - 1
        val previousTemplates = levelTemplatesMap[previousLevelNumber] ?: return false

        // Previous level is complete if all its images are completed
        return previousTemplates.all { isImageCompleted(it) }
    }

    /**
     * Returns number of completed images in a level (0 to 5)
     */
    fun getCompletedCountInLevel(levelNumber: Int, levelTemplatesMap: Map<Int, List<String>>): Int {
        val templates = levelTemplatesMap[levelNumber] ?: return 0
        return templates.count { isImageCompleted(it) }
    }

    fun isLevelFullyCompleted(levelNumber: Int, levelTemplatesMap: Map<Int, List<String>>): Boolean {
        val templates = levelTemplatesMap[levelNumber] ?: return false
        return templates.all { isImageCompleted(it) }
    }
}
