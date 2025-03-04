package com.hfad.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private lateinit var viewModel: SettingsViewModel

    private lateinit var themeSwitchListener: CompoundButton.OnCheckedChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.let {
            it.setNavigationIcon(R.drawable.ic_arrow_back_24)
            it.setNavigationIconTint(getColor(R.color.ic_color))
            it.setNavigationOnClickListener { this.finish() }
        }

        viewModel.observeState().observe(this) { handleUiState(it) }
        viewModel.observeCommand().observe(this) { handleCommand(it) }

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
        when (command) {
            is SettingsCommand.NavigateToShare -> share()
            is SettingsCommand.NavigateToSupport -> support()
            is SettingsCommand.NavigateToAgreement -> agreement()
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

    private fun agreement() {
        val agreementIntent = Intent(this, AgreeActivity::class.java)
        startActivity(agreementIntent)
    }
}