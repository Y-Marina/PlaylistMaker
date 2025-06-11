package com.hfad.playlistmaker.ui.playlist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.hfad.playlistmaker.ui.common.WarningDialogResult
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class CreatePlaylistFragment : Fragment() {
    companion object {
        private val className = CreatePlaylistFragment::class.qualifiedName
        private val closeDialogKey = "${className}.closeDialogKey"
    }

    private val args by navArgs<CreatePlaylistFragmentArgs>()

    private lateinit var binding: FragmentCreatePlaylistBinding

    private val viewModel: CreatePlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener(closeDialogKey) { _, bundle ->
            val result = WarningDialogResult.fromBundle(bundle)
            when (result) {
                is WarningDialogResult.Success -> {
                    findNavController().popBackStack()
                }

                is WarningDialogResult.Cancel -> {
                    // nothing
                }
            }
        }

        viewModel.setTrack(args.track)

        binding.toolbar.let {
            it.setNavigationIcon(R.drawable.ic_arrow_back_24)
            it.setNavigationOnClickListener { viewModel.onBackClicked() }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.setPlaylistPhoto(uri)
                }
            }

        viewModel.observeState().observe(viewLifecycleOwner) { handleUiState(it) }
        viewModel.observeCommand().observe(viewLifecycleOwner) { handleCommand(it) }

        binding.choosePoster.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//            val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
//            val file = File(filePath, "first_cover.jpg")
//            binding.choosePosterIm.setImageURI(file.toUri())
        }

        val nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setPlaylistName(p0.toString())
            }

            override fun afterTextChanged(text: Editable?) {
                viewModel.setPlaylistName(text.toString())
            }
        }

        val descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setPlaylistDescription(p0.toString())
            }

            override fun afterTextChanged(text: Editable?) {
                viewModel.setPlaylistDescription(text.toString())
            }
        }

        binding.playlistNameEt.addTextChangedListener(nameTextWatcher)
        binding.playlistDescriptionEt.addTextChangedListener(descriptionTextWatcher)

        binding.createBt.setOnClickListener {
            viewModel.onCreateButtonClicked()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackClicked()
            }
        })
    }

    private fun handleUiState(state: CreatePlaylistUiState) {
        binding.choosePosterIm.setImageURI(state.photoUri)
        binding.createBt.isEnabled = state.isCreateButtonEnabled
    }

    private fun handleCommand(command: CreatePlaylistCommand) {
        val navController = findNavController()
        when (command) {
            is CreatePlaylistCommand.NavigateToBackWithSuccess -> {
                setFragmentResult(
                    args.resultKey,
                    command.result.toBundle()
                )
                navController.popBackStack()
            }

            is CreatePlaylistCommand.NavigateToPlay -> {
                setFragmentResult(
                    args.resultKey,
                    command.result.toBundle()
                )
                navController.popBackStack()
            }

            is CreatePlaylistCommand.ShowWarning -> {
                navController.navigate(
                    CreatePlaylistFragmentDirections.toWarningDialog(
                        closeDialogKey,
                        getString(R.string.dialog_title),
                        getString(R.string.dialog_message),
                        getString(R.string.dialog_positive_button),
                        getString(R.string.dialog_neutral_button)
                    )
                )
            }

            is CreatePlaylistCommand.NavigateToBack -> {
                navController.popBackStack()
            }
        }
    }
}