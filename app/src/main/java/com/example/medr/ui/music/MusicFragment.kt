package com.example.medr.ui.music

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medr.R
import com.example.medr.core.Result
import com.example.medr.data.AppDatabase
import com.example.medr.databinding.FragmentMusicBinding
import com.example.medr.presentation.ImageViewModel
import com.example.medr.presentation.ImageViewModelFactory
import com.example.medr.presentation.MusicViewModel
import com.example.medr.presentation.MusicViewModelFactory
import com.google.android.material.snackbar.Snackbar


class MusicFragment : Fragment(R.layout.fragment_music) {
    private lateinit var binding: FragmentMusicBinding
    private val viewModel by viewModels<MusicViewModel> {
        MusicViewModelFactory(
            AppDatabase.getDB(
                requireContext()
            ).MusicDao()
        )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMusicBinding.bind(view)
        obtainNotes()
        clicks()
    }
    private fun obtainNotes() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchMusic().collect {
                    when (it) {
                        is Result.Success ->{
                            val adapter = MusicAdapter(it.data)
                            binding.rvPhotos.adapter = adapter
                            binding.rvPhotos.layoutManager = LinearLayoutManager(requireContext())
                        }
                        is Result.Failure ->{
                            Snackbar.make(binding.root,"Error! : ${it.exception}", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun clicks() {
        binding.imgAdd.setOnClickListener { findNavController().navigate(R.id.action_musicFragment_to_musicCreateFragment) }
    }
}