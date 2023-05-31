package com.example.infootball

import android.app.Application
import com.example.infootball.di.ApplicationComponent
import com.example.infootball.di.DaggerApplicationComponent

class InfootballApp : Application() {
    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .application(this)
            .build()
    }
}