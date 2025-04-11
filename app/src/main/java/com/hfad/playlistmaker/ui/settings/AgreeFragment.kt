package com.hfad.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.hfad.playlistmaker.R
import com.hfad.playlistmaker.databinding.FragmentAgreeBinding

class AgreeFragment : Fragment() {
    private lateinit var binding: FragmentAgreeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAgreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.agreementWv.webViewClient = WebViewClient()
        binding.agreementWv.loadUrl(getString(R.string.agree_url))
    }
}
