package com.example.medr.ui.notes

import android.icu.lang.UCharacter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.medr.R
import com.example.medr.core.Result
import com.example.medr.data.AppDatabase
import com.example.medr.databinding.FragmentMusicBinding
import com.example.medr.databinding.FragmentNotesBinding
import com.example.medr.presentation.ImageViewModel
import com.example.medr.presentation.ImageViewModelFactory
import com.example.medr.presentation.NoteViewModel
import com.example.medr.presentation.NoteViewModelFactory
import com.google.android.material.snackbar.Snackbar

class NotesFragment : Fragment(R.layout.fragment_notes) {
    private lateinit var binding: FragmentNotesBinding
    private val viewModel by viewModels<NoteViewModel> {
        NoteViewModelFactory(
            AppDatabase.getDB(
                requireContext()
            ).NoteDao()
        )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotesBinding.bind(view)
        obtainNotes()
        clicks()
    }
    private fun obtainNotes() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchNotes().collect {
                    when (it) {
                        is Result.Success ->{
                            val adapter = NoteAdapter(it.data)
                            binding.rvPhotos.adapter = adapter
                            binding.rvPhotos.layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
                        }
                        is Result.Failure ->{
                            Snackbar.make(binding.root,"Error! : ${it.exception}", Snackbar.LENGTH_SHORT).show()
                            Log.e("obtainNotes: ",it.exception.toString() )
                        }
                    }
                }
            }
        }
    }

    private fun clicks() {
        binding.imgAdd.setOnClickListener { findNavController().navigate(R.id.action_notesFragment_to_noteCreateFragment)}
    }
}