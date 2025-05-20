package com.example.onetoone

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OnevOneApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}