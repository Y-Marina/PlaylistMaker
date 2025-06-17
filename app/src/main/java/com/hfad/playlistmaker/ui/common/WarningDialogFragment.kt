package com.hfad.playlistmaker.ui.common

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hfad.playlistmaker.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class WarningDialogResult(
    val result: DialogResult,
    val extra: WarningDialogExtra? = null
) : Parcelable {
    companion object {
        private val bundleKey = "${WarningDialogResult::class.qualifiedName}.bundle"

        fun fromBundle(bundle: Bundle): WarningDialogResult {
            val dialogResult = bundle.getParcelableCompatWrapper<WarningDialogResult>(bundleKey)
            checkNotNull(dialogResult) { "Отсутствует значение для $bundleKey." }
            return dialogResult
        }
    }

    fun toBundle(): Bundle {
        return bundleOf(bundleKey to this)
    }
}

enum class DialogResult {
    Ok, Cancel, Negative
}

interface WarningDialogExtra: Parcelable

class WarningDialogFragment : DialogFragment() {
    private val args by navArgs<WarningDialogFragmentArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), R.style.MyThemeOverlay_MaterialAlertDialog)
            .setTitle(args.title)
            .setMessage(args.message)
            .setNeutralButton(args.neutralButton) { _, _ ->
                setFragmentResult(
                    args.resultKey,
                    WarningDialogResult(DialogResult.Negative).toBundle()
                )
                findNavController().popBackStack()
            }.setPositiveButton(args.positiveButton) { _, _ ->
                setFragmentResult(
                    args.resultKey,
                    WarningDialogResult(DialogResult.Ok, extra = args.extra).toBundle()
                )
                findNavController().popBackStack()
            }.create()
    }
}