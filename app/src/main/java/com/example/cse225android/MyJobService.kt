package com.example.cse225android

import android.app.job.JobParameters
import android.app.job.JobService
import android.widget.Toast

class MyJobService: JobService() {
    override fun onStartJob(params: JobParameters?):Boolean {
        Toast.makeText(this, "Job Started", Toast.LENGTH_LONG).show()
        jobFinished(params!!, false)
        return true
    }
    override fun onStopJob(params:JobParameters?):Boolean {
        return true
    }
}