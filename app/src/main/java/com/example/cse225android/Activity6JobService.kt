package com.example.cse225android

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import android.widget.Toast

class Activity6JobService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("Activity6JobService", "Job started: performing background task")
        Toast.makeText(this, "Background job executed by JobScheduler", Toast.LENGTH_SHORT).show()

        return false 
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("Activity6JobService", "Job stopped")
        return true
    }
}
