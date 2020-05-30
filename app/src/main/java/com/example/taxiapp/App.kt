package com.example.taxiapp

import android.app.Application
import com.parse.Parse

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Parse.initialize(
            Parse.Configuration.Builder(this)
            .applicationId("nC3GuLTsCoKXBvFCHblY9QiOJ0JdDQf1POXv8uzr")
            .clientKey("J4IEkcieDM6q1XpbdMXa4lHsmrgRiKhYlMBZEaBj")
            .server("https://parseapi.back4app.com/")
            .build()
        )

    }
}