package com.example.medr.ui.photos

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
import com.example.medr.R
import com.example.medr.core.Result
import com.example.medr.data.AppDatabase
import com.example.medr.databinding.FragmentMusicBinding
import com.example.medr.databinding.FragmentPhotosBinding
import com.example.medr.presentation.ImageViewModel
import com.example.medr.presentation.ImageViewModelFactory
import com.google.android.material.snackbar.Snackbar

class PhotosFragment : Fragment(R.layout.fragment_photos) {
    private lateinit var binding: FragmentPhotosBinding
    private val viewModel by viewModels<ImageViewModel> {
        ImageViewModelFactory(
            AppDatabase.getDB(
                requireContext()
            ).ImageDao()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPhotosBinding.bind(view)
        obtainPhotos()
        clicks()
    }

    private fun obtainPhotos() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchImages().collect {
                    when (it) {
                        is Result.Success ->{
                            val adapter = PhotoAdapter(it.data,viewModel)
                            binding.rvPhotos.adapter = adapter
                            binding.rvPhotos.layoutManager = GridLayoutManager(requireContext(),2)
                        }
                        is Result.Failure ->{
                            Snackbar.make(binding.root,"Error! : ${it.exception}",Snackbar.LENGTH_SHORT).show()
                            Log.e("obtainPhotos: ", it.exception.toString() )
                        }
                    }
                }
            }
        }
    }

    private fun clicks() {
        binding.imgAdd.setOnClickListener { findNavController().navigate(R.id.action_photosFragment_to_photoCreateFragment)}
    }

}