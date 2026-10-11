package com.appspartan.avxdownloader.utils

import android.app.Application

open class BaseCoreApplication:Application() {

    companion object {
        private lateinit var _instance: BaseCoreApplication
        fun getInstance() = _instance
    }

    override fun onCreate() {
        super.onCreate()
        _instance = this
        initFirebase()
//        if(BuildConfig.DEBUG || BuildConfig.LOGS_ENABLED) {
//        if(BuildConfig.DEBUG) {
//            //Timber.plant(Timber.DebugTree())
//        }
    }

    private fun initFirebase(){
        //    FirebaseApp.initializeApp(this)
        // val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    }
}