package tuev.co.tumine

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build

@TargetApi(Build.VERSION_CODES.O)
public class MineJobService: JobService() {
    lateinit var miningService: MiningService

    override fun onStartJob(params: JobParameters?): Boolean {
        val infoPassing = InfoPassing.readState(this) ?: return true


        miningService = MiningService()
        miningService.context = this
        miningService.infoPassing = infoPassing
        miningService.runningInsideJob = true
        miningService.prepareMine()
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        miningService.stopMining(true)
        return true
    }



}