package com.appspartan.avxdownloader.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class Presenter(private val onCoroutineScope: OnCoroutineScope?, val delay: Long) : CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job



    // call this method to cancel a coroutine when you don't need it anymore,
    // e.g. when user closes the screen
    fun cancel() {
        job.cancel()
    }


    fun startTask() = launch {
        onCoroutineScope?.onPreExecute()
        val result = doSomeBackgroundWork()
        // runs in background thread without blocking the Main Thread
        doUiStuff(result)
    }


    private suspend fun doSomeBackgroundWork(): String = withContext(Dispatchers.IO) { // to extractVideo code in Background Thread
        // do async work
        onCoroutineScope?.doInBackground()
        delay(delay) // simulate async work
        return@withContext "SomeResult"
    }

    // Runs in Main(UI) Thread
    private fun doUiStuff(result: String) {
        // hide progress
        onCoroutineScope?.onPostExecute()
    }

    interface OnCoroutineScope{
        fun doInBackground()
        fun onPreExecute()
        fun onPostExecute()
    }
}