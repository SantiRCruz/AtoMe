package com.example.medr.ui.photos

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.medr.R
import com.example.medr.core.Result
import com.example.medr.data.AppDatabase
import com.example.medr.data.models.ImageEntity
import com.example.medr.databinding.FragmentPhotoCreateBinding
import com.example.medr.presentation.ImageViewModel
import com.example.medr.presentation.ImageViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect


class PhotoCreateFragment : Fragment(R.layout.fragment_photo_create) {
    private lateinit var binding: FragmentPhotoCreateBinding

    private val viewModel by viewModels<ImageViewModel> {
        ImageViewModelFactory(
            AppDatabase.getDB(
                requireContext()
            ).ImageDao()
        )
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                pickFromGallery()
            } else {
                Snackbar.make(
                    binding.root,
                    "You need to enable the permission",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    private var uriResult: Uri? = null

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.data
                requireActivity().contentResolver.takePersistableUriPermission(
                    data!!,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                uriResult = data
                binding.imgPhoto.setImageURI(data)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPhotoCreateBinding.bind(view)
        clicks()
    }

    private fun clicks() {
        binding.imgBack.setOnClickListener { findNavController().popBackStack() }
        binding.imgTakePhoto.setOnClickListener { requestPermission() }
        binding.imgAdd.setOnClickListener { validateData() }
    }

    private fun validateData() {
        if (!validatePhoto())
            return

        savePhoto()
    }

    private fun validatePhoto(): Boolean {
        return if (uriResult == null) {
            Snackbar.make(
                binding.root,
                "You need to upload a image for continue",
                Snackbar.LENGTH_SHORT
            ).show()
            false
        } else {
            true
        }

    }

    private fun savePhoto() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saveImage(
                    ImageEntity(
                        0,
                        uriResult.toString(),
                        binding.EdtTitle.text.toString()
                    )
                ).collect {
                    when (it) {
                        is Result.Success -> {
                            findNavController().popBackStack()
                        }
                        is Result.Failure -> {
                            Snackbar.make(
                                binding.root,
                                "Its a problem, sorry (${it.exception})",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    pickFromGallery()
                }
                else -> {
                    requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        galleryResult.launch(intent)
    }
}