package com.example.medr.ui.music

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.medr.data.models.MusicEntity
import com.example.medr.databinding.ItemMusicBinding

class MusicAdapter(private val music:List<MusicEntity>):RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val itemBinding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return MusicViewHolder(itemBinding,parent.context)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bind(music[position])
    }

    override fun getItemCount(): Int = music.size

    inner class MusicViewHolder(private val binding:ItemMusicBinding,private val context: Context):RecyclerView.ViewHolder(binding.root){
        fun bind(song:MusicEntity){
            val mediaPlayer = MediaPlayer()
            var firstSound = 0
            binding.txtName.text = song.title
            binding.imgPlay.setOnClickListener {
                binding.imgPlay.visibility = View.GONE
                binding.imgStop.visibility = View.VISIBLE
                if (firstSound == 0){
                    mediaPlayer.setDataSource(context,song.music.toUri())
                    firstSound = 1
                }
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
            binding.imgStop.setOnClickListener {
                binding.imgPlay.visibility = View.VISIBLE
                binding.imgStop.visibility = View.GONE
                mediaPlayer.stop()
            }
        }
    }
}