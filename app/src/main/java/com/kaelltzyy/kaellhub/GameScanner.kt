package com.kaelltzyy.kaellhub

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.kaelltzyy.kaellhub.model.GameApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GameScanner {
    suspend fun scan(context: Context): List<GameApp> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory("android.intent.category.GAME")
        }
        val games = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA)
            .mapNotNull { ri ->
                val pkg = ri.activityInfo.packageName
                val label = ri.loadLabel(pm).toString()
                if (pkg == context.packageName) return@mapNotNull null
                GameApp(packageName = pkg, label = label)
            }
            .distinctBy { it.packageName }
            .sortedBy { it.label.lowercase() }
        if (games.isEmpty()) broadFallback(pm, context.packageName) else games
    }

    private fun broadFallback(pm: PackageManager, selfPkg: String): List<GameApp> {
        val intent = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_LAUNCHER) }
        return pm.queryIntentActivities(intent, 0)
            .filter { ri ->
                val pkg = ri.activityInfo.packageName
                pkg != selfPkg && looksLikeGame(pkg, ri.loadLabel(pm).toString())
            }
            .map { ri -> GameApp(packageName = ri.activityInfo.packageName, label = ri.loadLabel(pm).toString()) }
            .distinctBy { it.packageName }
            .sortedBy { it.label.lowercase() }
    }

    private fun looksLikeGame(pkg: String, label: String): Boolean {
        val keys = listOf("game","clash","arena","battle","quest","strike","pubg","mlbb","ff","cod","rpg","survival","koin","domino","higgs")
        val combined = (pkg + label).lowercase()
        return keys.any { combined.contains(it) }
    }
}
