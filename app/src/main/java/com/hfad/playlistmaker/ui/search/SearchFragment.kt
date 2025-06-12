package com.hfad.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hfad.playlistmaker.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private val viewModel by viewModel<SearchViewModel>()

    private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) { handleUiState(it) }
        viewModel.observeCommand().observe(viewLifecycleOwner) { handleCommand(it) }

        adapter = SearchAdapter(viewModel)
        binding.contentList.adapter = adapter

        binding.searchEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.search()
                true
            }
            false
        }

        binding.clearIm.setOnClickListener {
            binding.searchEt.setText("")
            viewModel.onClearClicked()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.clearIm.isVisible = !p0.isNullOrEmpty()
                viewModel.searchDebounce(p0.toString())
            }

            override fun afterTextChanged(text: Editable?) {
                viewModel.searchDebounce(text.toString())
            }
        }

        binding.searchEt.addTextChangedListener(textWatcher)
    }

    private fun handleUiState(state: SearchUiState) {
        binding.progressBar.isVisible = state.isLoading
        binding.emptyMessage.root.isVisible = state.isEmptyResult
        binding.noInternetMessage.root.isVisible = state.isError
        binding.contentList.isVisible = state.isContentVisible
        adapter.data = state.items
        adapter.notifyDataSetChanged()
    }

    private fun handleCommand(command: SearchCommand) {
        val navController = findNavController()
        when (command) {
            is SearchCommand.NavigateToPlayer -> navController.navigate(
                SearchFragmentDirections.toPlayFragment(command.track)
            )
        }
    }
}
