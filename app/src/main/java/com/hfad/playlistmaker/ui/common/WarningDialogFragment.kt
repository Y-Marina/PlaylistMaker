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

sealed class WarningDialogResult : Parcelable {
    @Parcelize
    data object Success : WarningDialogResult()

    @Parcelize
    data object Cancel : WarningDialogResult()

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

class WarningDialogFragment : DialogFragment() {
    private val args by navArgs<WarningDialogFragmentArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), R.style.MyThemeOverlay_MaterialAlertDialog)
            .setTitle(args.title)
            .setMessage(args.message)
            .setNeutralButton(args.neutralButton) { _, _ ->
                setFragmentResult(
                    args.resultKey,
                    WarningDialogResult.Cancel.toBundle()
                )
                findNavController().popBackStack()
            }.setPositiveButton(args.positiveButton) { _, _ ->
                setFragmentResult(
                    args.resultKey,
                    WarningDialogResult.Success.toBundle()
                )
                findNavController().popBackStack()
            }.create()
    }
}