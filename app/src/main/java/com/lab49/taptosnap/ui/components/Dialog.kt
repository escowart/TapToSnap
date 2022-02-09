package com.lab49.taptosnap.ui.components

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.StyleRes
import androidx.viewbinding.ViewBinding
import com.lab49.taptosnap.R
import com.lab49.taptosnap.databinding.DialogActionBinding
import com.lab49.taptosnap.extensions.getLayoutFromBinding
import com.lab49.taptosnap.infrastructure.ClientError
import com.lab49.taptosnap.infrastructure.ServerError

/**
 * Created by Edwin S. Cowart on 06 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */

data class CreateDialogResult<Binding: ViewBinding>(
    val binding: Binding,
    val dialog: Dialog
)

inline fun <reified Binding: ViewBinding> Context.createDialog(
    bind: (view: View) -> Binding,
    @StyleRes theme: Int = android.R.style.Widget_Material_ButtonBar_AlertDialog
): CreateDialogResult<Binding> {
    val dialog = Dialog(this, theme)
    val window = dialog.window!!
    dialog.setContentView(getLayoutFromBinding<Binding>())
    val root = window.decorView
        .findViewById<FrameLayout>(android.R.id.content)
        .getChildAt(0)
    return CreateDialogResult(bind(root), dialog)
}

fun Context.showErrorDialog(
    error: Any?,
    abandon: (() -> Unit)? = null,
    retry: (() -> Unit)? = null
) {
    val result = createDialog(DialogActionBinding::bind)
    val binding = result.binding
    val dialog = result.dialog
    dialog.setCancelable(true)
    dialog.setCanceledOnTouchOutside(true)
    dialog.setOnCancelListener { abandon?.invoke() }
    dialog.window!!.setBackgroundDrawableResource(R.color.transparent)

    binding.title.setText(R.string.error)
    binding.message.text = when (error) {
        is ClientError<*> -> error.message
        is ServerError<*> -> error.message
        is Error -> error.message
        else -> toString()
    }
    binding.leftButton.setText(R.string.ok)
    binding.leftButton.setOnClickListener {
        abandon?.invoke()
        dialog.dismiss()
    }
    retry?.let {
        binding.rightButton.setText(R.string.retry)
        binding.rightButton.setOnClickListener {
            retry()
            dialog.dismiss()
        }
    } ?: run {
        binding.rightButton.visibility = View.GONE
    }
    dialog.show()
}
