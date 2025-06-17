package com.hfad.playlistmaker.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.databinding.ActivityMainBinding
import com.hfad.playlistmaker.ui.common.WarningDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        val navHostFragment = binding.containerView.getFragment<NavHostFragment>()
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        viewModel

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.menu_dialog_fragment,
                R.id.playlist_fragment,
                R.id.addToPlaylistDialogFragment,
                R.id.create_playlist_fragment,
                R.id.warning_dialog,
                R.id.play_fragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.bottomNavView.visibility = View.GONE
                }

                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.bottomNavView.visibility = View.VISIBLE
                }
            }
        }
    }
}
