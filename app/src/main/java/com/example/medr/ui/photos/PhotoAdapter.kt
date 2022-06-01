package com.example.medr.ui.photos

import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.medr.R
import com.example.medr.core.Result
import com.example.medr.data.models.ImageEntity
import com.example.medr.databinding.ItemPhotoBinding
import com.example.medr.presentation.ImageViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PhotoAdapter(private val photos: List<ImageEntity>, private val viewModel: ImageViewModel) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount(): Int = photos.size

    inner class PhotoViewHolder(
        private val binding: ItemPhotoBinding,
        private val viewModel: ImageViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: ImageEntity) {
            val scope = CoroutineScope(Dispatchers.Main)
            binding.imgPhoto.setImageURI(photo.image.toUri())
            binding.txtName.text = photo.title
            binding.delete.setOnClickListener {

                scope.launch {
                    viewModel.deleteImage(photo).collect {
                        when (it) {
                            is Result.Success -> {
                                Snackbar.make(
                                    binding.root,
                                    "Eliminated Correctly",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                Navigation.findNavController(binding.root)
                                    .navigate(R.id.action_photosFragment_self)
                            }
                            is Result.Failure -> {
                                Snackbar.make(
                                    binding.root,
                                    "Sorry, there was an error! (${it.exception})",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }
}