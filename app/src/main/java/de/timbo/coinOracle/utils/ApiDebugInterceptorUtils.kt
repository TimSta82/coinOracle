package de.timbo.coinOracle.utils

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor

object ApiDebugInterceptorUtils {

    fun createChuckerInterceptor(context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(context = context, showNotification = false)
        return ChuckerInterceptor.Builder(context).collector(collector).build()
    }
}
