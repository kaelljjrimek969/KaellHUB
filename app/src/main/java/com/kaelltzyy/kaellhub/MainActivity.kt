package com.kaelltzyy.kaellhub

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kaelltzyy.kaellhub.databinding.ActivityMainBinding
import com.kaelltzyy.kaellhub.model.GameApp
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    private lateinit var adapter: GameAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        adapter = GameAdapter(onLaunch = ::launchGame, onFavorite = ::toggleFavorite)
        b.rvGames.layoutManager = GridLayoutManager(this, 3)
        b.rvGames.adapter = adapter
        b.rvGames.setHasFixedSize(true)
        b.btnBoost.setOnClickListener {
            BoostManager.boost(this)
            val free = BoostManager.freeRamMb(this)
            b.tvRamInfo.text = "RAM bebas: ${free}MB"
            Toast.makeText(this, "Boost done — ${free}MB free", Toast.LENGTH_SHORT).show()
        }
        scanGames()
    }
    private fun scanGames() {
        lifecycleScope.launch {
            b.tvRamInfo.text = "Scanning..."
            val games = GameScanner.scan(this@MainActivity)
            adapter.submitList(games)
            val free = BoostManager.freeRamMb(this@MainActivity)
            b.tvRamInfo.text = "RAM bebas: ${free}MB · ${games.size} game"
        }
    }
    private fun launchGame(game: GameApp) {
        BoostManager.boost(this)
        val intent = packageManager.getLaunchIntentForPackage(game.packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Gagal launch ${game.label}", Toast.LENGTH_SHORT).show()
        }
    }
    private fun toggleFavorite(game: GameApp) {
        game.isFavorite = !game.isFavorite
        adapter.submitList(adapter.currentList.toList())
    }
}
