package com.hfad.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.navigation.fragment.findNavController
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()

    private lateinit var themeSwitchListener: CompoundButton.OnCheckedChangeListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) { handleUiState(it) }
        viewModel.observeCommand().observe(viewLifecycleOwner) { handleCommand(it) }

        themeSwitchListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            viewModel.onChangeTheme(isNight = isChecked)
        }

        binding.switchTheme.setOnCheckedChangeListener(themeSwitchListener)

        binding.shareTv.setOnClickListener {
            viewModel.onShareClick()
        }

        binding.supportTv.setOnClickListener {
            viewModel.onSupportClick()
        }

        binding.agreeTv.setOnClickListener {
            viewModel.onAgreementClick()
        }
    }

    private fun handleUiState(state: SettingsUiState) {
        println("myTag n = $state")
        with(binding.switchTheme) {
            setOnCheckedChangeListener(null)
            isChecked = state.isNightMode
            setOnCheckedChangeListener(themeSwitchListener)
        }
    }

    private fun handleCommand(command: SettingsCommand) {
        val navController = findNavController()
        when (command) {
            is SettingsCommand.NavigateToShare -> share()
            is SettingsCommand.NavigateToSupport -> support()
            is SettingsCommand.NavigateToAgreement -> {
                navController.navigate(
                    R.id.action_settingsFragment_to_agreeFragment
                )
            }
        }
    }

    private fun share() {
        val message = getString(R.string.shareMessage)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "*/*"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(shareIntent)
    }

    private fun support() {
        val messageSubject = getString(R.string.supportMessageSubject)
        val messageText = getString(R.string.supportMessageText)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.studentEmail)))
        intent.putExtra(Intent.EXTRA_SUBJECT, messageSubject)
        intent.putExtra(Intent.EXTRA_TEXT, messageText)
        startActivity(intent)
    }
}
