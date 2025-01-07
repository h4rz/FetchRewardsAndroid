package io.github.h4rz.fetchrewardsandroid

import android.app.Application
import io.github.h4rz.fetchrewardsandroid.di.appModule
import io.github.h4rz.fetchrewardsandroid.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FetchRewardsAndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FetchRewardsAndroidApplication)
            modules(appModule, viewModelModule)
        }
    }
}