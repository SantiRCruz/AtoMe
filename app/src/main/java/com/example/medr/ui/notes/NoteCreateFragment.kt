package com.example.medr.ui.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.medr.R
import com.example.medr.core.Result
import com.example.medr.data.AppDatabase
import com.example.medr.data.models.NoteEntity
import com.example.medr.databinding.FragmentNoteCreateBinding
import com.example.medr.databinding.FragmentNotesBinding
import com.example.medr.presentation.NoteViewModel
import com.example.medr.presentation.NoteViewModelFactory
import com.google.android.material.snackbar.Snackbar


class NoteCreateFragment : Fragment(R.layout.fragment_note_create) {
    private lateinit var binding: FragmentNoteCreateBinding
    private val viewModel by viewModels<NoteViewModel> { NoteViewModelFactory(AppDatabase.getDB(requireContext()).NoteDao()) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNoteCreateBinding.bind(view)

        clicks()
    }

    private fun clicks() {
        binding.imgBack.setOnClickListener { findNavController().popBackStack() }
        binding.imgAdd.setOnClickListener { validateData() }
    }

    private fun validateData() {
        val results =  arrayListOf(validateTitle(),validateDescription())
        if (false in results)
            return

        saveNote()
    }

    private fun validateTitle(): Boolean {
        return if(binding.edtTitle.text.toString().isNullOrEmpty()){
            binding.edtTitle.error = "This field can't be empty"
            false
        }else{
            binding.edtTitle.error = null
            true
        }
    }

    private fun validateDescription(): Boolean {
        return if(binding.edtDescription.text.toString().isNullOrEmpty()){
            binding.edtDescription.error = "This field can't be empty"
            false
        }else{
            binding.edtDescription.error = null
            true
        }
    }

    private fun saveNote() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.saveNotes(NoteEntity(0,binding.edtTitle.text.toString(),binding.edtDescription.text.toString())).collect{
                    when(it){
                        is Result.Success->{
                            findNavController().popBackStack()
                        }
                        is Result.Failure->{
                            Snackbar.make(binding.root,"Error ${it.exception}",Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

}