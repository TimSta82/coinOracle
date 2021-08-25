package de.timbo.coinOracle.extensions

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun Fragment.hideKeyboard() = view?.let { view -> activity?.hideKeyboard(view) }
fun Activity.hideKeyboard() = hideKeyboard(currentFocus ?: View(this))

fun TextInputEditText.focusAndShowKeyboard() {
    requestFocus()
    setSelection(length())
    val inputMethodManager: InputMethodManager? = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.showSnackBar(message: String, length: Int = Snackbar.LENGTH_SHORT) {
    require(length in -2..0) { "No valid Snackbar show length" }
    Snackbar.make(requireView(), message, length).show()
}

fun Fragment.showSnackBar(@StringRes messageStringRes: Int, length: Int = Snackbar.LENGTH_SHORT) {
    require(length in -2..0) { "No valid Snackbar show length" }
    Snackbar.make(requireView(), messageStringRes, length).show()
}

fun Snackbar.setMaxLines(lines: Int): Snackbar {
    this.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = lines
    return this
}

fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit): Job = viewModelScope.launch { block(this) }

fun Fragment.navigate(destination: NavDirections) = findNavController().navigate(destination)
fun Fragment.navigateUp() = findNavController().navigateUp()

fun Fragment.getColorByRes(@ColorRes colorRes: Int) = ContextCompat.getColor(requireContext(), colorRes)
fun Activity.getColorByRes(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Fragment.doOnBackPressed(onBackPressed: () -> Unit) {
    activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPressed()
        }
    })
}

fun Context.isNetworkConnected(): Boolean {
    (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
        return getNetworkCapabilities(activeNetwork)?.run {
            when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } ?: false
    }
}

inline fun <reified T : ViewModel> AppCompatActivity.viewModelsFactory(crossinline viewModelInitialization: () -> T): Lazy<T> = viewModels {
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = viewModelInitialization.invoke() as T
    }
}

inline fun <reified T : ViewModel> Fragment.viewModelsFactory(crossinline viewModelInitialization: () -> T): Lazy<T> = viewModels {
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = viewModelInitialization.invoke() as T
    }
}

fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    block: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block(this.value, liveData.value)
    }
    return result
}

fun Context.getColorStateListOneColor(@ColorRes colorRes: Int) = ColorStateList.valueOf(ContextCompat.getColor(this, colorRes))
