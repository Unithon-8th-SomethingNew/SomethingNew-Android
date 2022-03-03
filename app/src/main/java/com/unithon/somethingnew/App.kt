package com.unithon.somethingnew

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.unithon.somethingnew.di.modules.networkModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {

    val context: Context
        get() = this

    override fun onCreate() {
        super.onCreate()
        instance = this
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                    networkModules,
                )
            )

        }
    }


    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var instance: App
            private set
    }

}
