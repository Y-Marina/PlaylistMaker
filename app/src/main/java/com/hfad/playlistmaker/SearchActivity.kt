package com.hfad.playlistmaker

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.android.material.appbar.MaterialToolbar
import kotlin.properties.Delegates

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT_KEY = "searchTextKey"
    }

    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView

    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        searchEditText = findViewById(R.id.search_et)
        clearButton = findViewById(R.id.clear_im)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar?.let {
            it.setNavigationIcon(R.drawable.ic_arrow_back_24)
            it.setNavigationIconTint(getColor(R.color.ic_color))
            it.setNavigationOnClickListener { this.finish() }
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearButton.isVisible = !p0.isNullOrEmpty()
            }

            override fun afterTextChanged(text: Editable?) {
                searchText = text.toString()
            }
        }

        searchEditText.addTextChangedListener(textWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_TEXT_KEY, searchText)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val text = savedInstanceState.getString(SEARCH_TEXT_KEY, "")
        searchEditText.setText(text)
    }
}