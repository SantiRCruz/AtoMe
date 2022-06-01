package com.example.medr.ui.music

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.medr.R
import com.example.medr.core.Result
import com.example.medr.data.AppDatabase
import com.example.medr.data.models.MusicEntity
import com.example.medr.databinding.FragmentMusicBinding
import com.example.medr.databinding.FragmentMusicCreateBinding
import com.example.medr.presentation.MusicViewModel
import com.example.medr.presentation.MusicViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class MusicCreateFragment : Fragment(R.layout.fragment_music_create) {
    private lateinit var binding: FragmentMusicCreateBinding
    private var mediaPlayer: MediaPlayer? = null
    private var firstTimeSound = 0
    private var uriResult: Uri? = null
    private val viewModel by viewModels<MusicViewModel> {
        MusicViewModelFactory(
            AppDatabase.getDB(
                requireContext()
            ).MusicDao()
        )
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                pickAudio()
            } else {
                Snackbar.make(binding.root, "You need enable the permission", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    private val audioResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                mediaPlayer = MediaPlayer()
                firstTimeSound = 0
                binding.imgPlay.visibility = View.VISIBLE
                binding.imgStop.visibility = View.GONE
                val data = it.data?.data
                uriResult = null
                uriResult = data
                requireActivity().contentResolver.takePersistableUriPermission(
                    data!!,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMusicCreateBinding.bind(view)

        clicks()
    }

    private fun clicks() {
        binding.imgBack.setOnClickListener {
            if (mediaPlayer != null)
                mediaPlayer!!.stop()

            findNavController().popBackStack()
        }
        binding.imgPlay.setOnClickListener {
            if (firstTimeSound == 0) {
                mediaPlayer?.setDataSource(requireContext(), uriResult!!)
                firstTimeSound = 1
            }
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            binding.imgPlay.visibility = View.GONE
            binding.imgStop.visibility = View.VISIBLE
        }
        binding.imgStop.setOnClickListener {
            mediaPlayer?.stop()
            binding.imgPlay.visibility = View.VISIBLE
            binding.imgStop.visibility = View.GONE
        }
        binding.imgTakeSong.setOnClickListener { requestPermission() }
        binding.imgAdd.setOnClickListener { validateData() }
    }

    private fun validateData() {
        if (!validateAudio())
            return

        saveData()
    }

    private fun saveData() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.saveMusic(MusicEntity(0,uriResult.toString(),binding.EdtTitle.text.toString())).collect {
                    when(it){
                        is Result.Success ->{findNavController().popBackStack()}
                        is Result.Failure ->{Snackbar.make(binding.root, "Error! ${it.exception}", Snackbar.LENGTH_SHORT)
                            .show()}
                    }
                }
            }
        }
    }

    private fun validateAudio(): Boolean {
        return if (uriResult == null) {
            Snackbar.make(binding.root, "You don't select any song", Snackbar.LENGTH_SHORT)
                .show()
            false
        } else {
            true
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    pickAudio()
                }
                else -> requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun pickAudio() {
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT)
        i.type = "audio/*"
        audioResult.launch(i)
    }
}