/* Tuev, Co
 * Copyright 2018 Tuev, Co       <https://tuev-co.eu>, <support@tuev-co.eu>
 *
 * This file contains Original Code as defined in and that are subject to
 * the License provided in the 'License.pdf' in the file tree or
 * available in 'https://tuev-co.eu'. You may not use this file except in
 * compliance with the License. The rights granted to you under the License
 * may not be used to distribute, or enable the distribution of,
 * unlawful or unlicensed copies of the 'Tumine Monero Software'
 * or any binaries or libraries built using the source code provided.
 *
 * The Original Code and all software distributed under the License are
 * distributed on an 'AS IS' basis, WITHOUT WARRANTY OF ANY KIND.
 *
 *
 * Please see the License for the specific governing rights and
 * limitations under the License.
 *
 */

package tuev.co.tumine

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import tuev.co.tumine.InfoPassing.Companion.getDefaultTuminePrivateDir
import java.io.File

public class CheckerAndBootService: BroadcastReceiver() {

    /*
    **IMPORTANT**: call 'checkService' from your BOOT_COMPLETED receiver to start the miner
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) {
            return
        }

        Log.d("CheckerAndBootService", "Called at "+System.currentTimeMillis())

        val intentForService = Intent(context.applicationContext, CheckerAndBootService::class.java)
        val alarmManager = context.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val broadcast = PendingIntent.getBroadcast(context.applicationContext, 5362,
                intentForService, PendingIntent.FLAG_CANCEL_CURRENT)

        when {
            Build.VERSION.SDK_INT >= 19 -> alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + MiningService.nudgeInterval, broadcast)
            else -> alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + MiningService.nudgeInterval, broadcast)
        }

        checkService(context)
    }

    companion object {
        public fun checkService(context: Context) {
            val lockFile = File(getDefaultTuminePrivateDir(context), "running.lock")
            if (lockFile.exists()) {
                Log.d("CheckerAndBootService", "Starting TUmine")
                InfoPassing.startMiningServiceRestoreState(context)
            }
        }
    }
}
