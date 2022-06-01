package com.example.medr.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medr.data.models.NoteEntity
import com.example.medr.databinding.ItemNotesBinding

class NoteAdapter(private val notes:List<NoteEntity>):RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemBinding = ItemNotesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    inner class NoteViewHolder(private val binding :ItemNotesBinding ):RecyclerView.ViewHolder(binding.root){
        fun bind(note:NoteEntity){
            binding.txtTitle.text = note.title
            binding.txtDescription.text = note.description
        }
    }
}
