package de.bornholdtlee.defaultprojectkotlin.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Can be used in every 'onCreateViewHolder' function to avoid some boilerplate
 */
fun inflate(parent: ViewGroup, @LayoutRes layoutRes: Int): View = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)

/**
 *Can be used to access string resources inside the typical 'ViewHolder.bind' function
 */
fun RecyclerView.ViewHolder.getString(@StringRes stringId: Int): String = itemView.context.resources.getString(stringId)

/**
 * Can be used to access string resources with additional arguments inside the typical 'ViewHolder.bind' function
 */
fun RecyclerView.ViewHolder.getString(@StringRes resId: Int, vararg formatArgs: Any?): String = itemView.context.resources.getString(resId, *formatArgs)
