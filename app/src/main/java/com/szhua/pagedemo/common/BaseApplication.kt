package com.szhua.pagedemo.common

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*
import kotlin.jvm.Throws

open class BaseApplication :Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var context: Context
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


}


