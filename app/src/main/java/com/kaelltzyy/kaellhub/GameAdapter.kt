package com.kaelltzyy.kaellhub

import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kaelltzyy.kaellhub.databinding.ItemGameBinding
import com.kaelltzyy.kaellhub.model.GameApp

class GameAdapter(
    private val onLaunch: (GameApp) -> Unit,
    private val onFavorite: (GameApp) -> Unit
) : ListAdapter<GameApp, GameAdapter.VH>(DIFF) {
    inner class VH(val b: ItemGameBinding) : RecyclerView.ViewHolder(b.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        val game = getItem(position)
        val ctx: Context = holder.b.root.context
        holder.b.tvGameName.text = game.label
        holder.b.btnFavorite.isSelected = game.isFavorite
        try {
            val icon = ctx.packageManager.getApplicationIcon(game.packageName)
            Glide.with(ctx).load(icon).into(holder.b.ivGameIcon)
        } catch (e: PackageManager.NameNotFoundException) {
            holder.b.ivGameIcon.setImageResource(android.R.drawable.sym_def_app_icon)
        }
        holder.b.root.setOnClickListener { onLaunch(game) }
        holder.b.btnFavorite.setOnClickListener { onFavorite(game) }
    }
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<GameApp>() {
            override fun areItemsTheSame(a: GameApp, b: GameApp) = a.packageName == b.packageName
            override fun areContentsTheSame(a: GameApp, b: GameApp) = a == b
        }
    }
}
