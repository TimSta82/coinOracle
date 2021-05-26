package de.bornholdtlee.defaultprojectkotlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import de.bornholdtlee.defaultprojectkotlin.R

open class BaseDialogFragment(
    @LayoutRes private val layoutResId: Int,
    @DrawableRes private val backgroundRes: Int = R.drawable.background_dialog_opaque,
    private val dimensionMode: DialogDimensionMode = DialogDimensionMode.WRAP_CONTENT_WIDTH_AND_HEIGHT,
    private val cancelable: Boolean = true
) : DialogFragment(layoutResId) {

    private companion object {
        const val NO_BACKGROUND = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = cancelable
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        requireDialog().requestWindowFeature(Window.FEATURE_NO_TITLE)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        setDialogBounds()
        setBackground()
    }

    private fun setDialogBounds() {
        requireWindow().attributes.height = when (dimensionMode) {
            DialogDimensionMode.MATCH_PARENT_WIDTH_WRAP_CONTENT_HEIGHT -> ConstraintLayout.LayoutParams.WRAP_CONTENT
            DialogDimensionMode.MATCH_PARENT_WIDTH_AND_HEIGHT -> ConstraintLayout.LayoutParams.MATCH_PARENT
            DialogDimensionMode.WRAP_CONTENT_WIDTH_MATCH_PARENT_HEIGHT -> ConstraintLayout.LayoutParams.MATCH_PARENT
            DialogDimensionMode.WRAP_CONTENT_WIDTH_AND_HEIGHT -> ConstraintLayout.LayoutParams.WRAP_CONTENT
        }
        requireWindow().attributes.width = when (dimensionMode) {
            DialogDimensionMode.MATCH_PARENT_WIDTH_WRAP_CONTENT_HEIGHT -> ConstraintLayout.LayoutParams.MATCH_PARENT
            DialogDimensionMode.MATCH_PARENT_WIDTH_AND_HEIGHT -> ConstraintLayout.LayoutParams.MATCH_PARENT
            DialogDimensionMode.WRAP_CONTENT_WIDTH_MATCH_PARENT_HEIGHT -> ConstraintLayout.LayoutParams.WRAP_CONTENT
            DialogDimensionMode.WRAP_CONTENT_WIDTH_AND_HEIGHT -> ConstraintLayout.LayoutParams.WRAP_CONTENT
        }
    }

    private fun setBackground() {
        if (backgroundRes != NO_BACKGROUND) {
            requireWindow().setBackgroundDrawableResource(backgroundRes)
        }
    }

    private fun requireWindow() = dialog?.window ?: throw IllegalStateException("Window was not yet created")

    enum class DialogDimensionMode {
        MATCH_PARENT_WIDTH_WRAP_CONTENT_HEIGHT,
        WRAP_CONTENT_WIDTH_MATCH_PARENT_HEIGHT,
        MATCH_PARENT_WIDTH_AND_HEIGHT,
        WRAP_CONTENT_WIDTH_AND_HEIGHT,
    }
}
