package com.hfad.playlistmaker.ui.settings

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.hfad.playlistmaker.creator.Creator
import com.hfad.playlistmaker.R

const val PREFERENCES = "preferences"

class SettingsActivity : AppCompatActivity() {

    private val settingsInteractor by lazy { Creator.provideSettingsInteractor(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar?.let {
            it.setNavigationIcon(R.drawable.ic_arrow_back_24)
            it.setNavigationIconTint(getColor(R.color.ic_color))
            it.setNavigationOnClickListener { this.finish() }
        }

        val switchTheme = findViewById<SwitchCompat>(R.id.switch_theme)
        val shareTextView = findViewById<TextView>(R.id.share_tv)
        val supportTextView = findViewById<TextView>(R.id.support_tv)
        val agreeTextView = findViewById<TextView>(R.id.agree_tv)

        switchTheme.isChecked = (resources.configuration.uiMode
                and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            settingsInteractor.saveTheme(isChecked)
        }

        shareTextView.setOnClickListener {
            share()
        }

        supportTextView.setOnClickListener {
            support()
        }

        agreeTextView.setOnClickListener {
            val agreementIntent = Intent(this, AgreeActivity::class.java)
            startActivity(agreementIntent)
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