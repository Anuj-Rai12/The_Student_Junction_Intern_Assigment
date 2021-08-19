package com.example.cargo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class ApplicationClassFile  : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}