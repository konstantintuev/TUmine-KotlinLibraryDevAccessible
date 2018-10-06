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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler

class MineConnector(messageReciever: OnMessageReceived, val context: Context, infoPassing: InfoPassing?) {
    private var sockListener = SockClient(messageReciever, context, infoPassing)
    private var receiver: BroadcastReceiver

    init {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent?.extras?.getBoolean("started") == true) {
                    sockListener.run()
                    messageReciever.connected()
                } else if (intent?.extras?.getBoolean("stopped") == true) {
                    sockListener.stopClient()
                }
            }
        }
        val filter = IntentFilter("tuev.co.tumine.MineServiceUpdate")
        context.registerReceiver(receiver, filter)
        Handler().postDelayed({
            val isRunning = Intent("tuev.co.tumine.MineServiceStatus")
            isRunning.putExtra("action", "report")
            context.sendBroadcast(isRunning)
        }, 2500)
    }
    public fun pause() {
        sockListener.sendMessage("pause")
    }
    public fun resume() {
        sockListener.sendMessage("resume")
    }
    public fun requestHashratePerThread() {
        sockListener.sendMessage("advhash")
    }

    public fun detach() {
        context.unregisterReceiver(receiver)
        sockListener.stopClient()
    }
}