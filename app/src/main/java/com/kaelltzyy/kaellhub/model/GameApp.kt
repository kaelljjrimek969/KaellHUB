package com.kaelltzyy.kaellhub.model

data class GameApp(
    val packageName: String,
    val label: String,
    val iconResId: Int = 0,
    var isFavorite: Boolean = false,
    var lastPlayed: Long = 0L
)
