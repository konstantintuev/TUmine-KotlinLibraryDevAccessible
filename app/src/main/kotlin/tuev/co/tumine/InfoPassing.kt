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

@file:Suppress("unused", "RedundantVisibilityModifier")

package tuev.co.tumine

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import io.michaelrocks.paranoid.Obfuscate
import java.io.File


@Obfuscate
public class InfoPassing(public val context: Context?) : Parcelable {

    public val availableThreads: Int
        get() = Runtime.getRuntime().availableProcessors()


    //TODO: IMPORTANT: make sure the user has internet to initialize some files if you use only the .jar in your app and not the .aar

    //NOTE: check my website for benchmarks
    //TODO: use the getAvailableCores() cores to know the hardware that you're dealing with and use whole numbers as threads
    public var threadsToUse: Int = 0

    //get updates about what is happening with the service using the ReceiveInfo connector
    /*These are the keys which will be used in the ReceiveInfo connector:
        - info, without error can be: lowMemory, stopped, started
        - info, with error: contains the error message
        - Logcat messages about some checks before function execution
    */
    public var isBasicLogging = true

    //local miner version
    public val version = 5

    //if enabled update the mining part over the internet
    public var updateOverInternet = false

    //which mining algorithm to use - this affects which coins you can mine, CRYPTONIGHT by default
    public var algorithm: MiningAlgorithm? = MiningAlgorithm.CRYPTONIGHT

    //which mining variant to use - this affects which coins you can mine, AUTO by default
    public var variant: MiningVariant? = MiningVariant.VARIANT_AUTO

    //TODO: if not null will be used to start the service as foreground service with id 6353
    public var notification: Notification? = null

    //pools to mine to
    public var pools: ArrayList<Pool> = ArrayList()

    //cpu affinity set which cores will be used, "0x-1" = default
    public var cpuAffinity: String = "0x-1"

    //between 0 and 5, -1 = default
    public var cpuPriority: Int = -1

    //review start params as received by the miner
    public var debugParams: Boolean = false

    //report ONLY simple hashrate, accepted and errors
    public var reportOnlyBasics: Boolean = false

    //if true the miner won't create a file used for communication between it and the kotlin library
    //might improve performance
    public var silent: Boolean = false

    //Check cpu usage on start of the miner:
    // 75% or more - don't start mining, the OS is hard enough on the device
    // 50% to 75% - mine with only 1 thread
    public var smartStart: Boolean = false

    //ID to be used when starting the foreground service, defaults to 6345
    public var foregroundNotificationId = 6345

    private constructor() : this(null)

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeList(pools)
        dest.writeInt(threadsToUse)
        dest.writeByte(if (isBasicLogging) 1 else 0)
        dest.writeString(algorithm!!.toString())
        dest.writeString(variant!!.toString())
        dest.writeString(cpuAffinity)
        dest.writeInt(cpuPriority)
        dest.writeByte(if (debugParams) 1 else 0)
        dest.writeByte(if (silent) 1 else 0)
        dest.writeByte(if (reportOnlyBasics) 1 else 0)
        dest.writeParcelable(notification, 0)
        dest.writeByte(if (smartStart) 1 else 0)
        dest.writeByte(if (updateOverInternet) 1 else 0)
    }

    constructor(orig: Parcel) : this() {
        pools = orig.readArrayList(Pool::class.java.classLoader) as ArrayList<Pool>
        threadsToUse = orig.readInt()
        isBasicLogging = orig.readByte() != 0.toByte()
        algorithm = MiningAlgorithm.fromString(orig.readString()!!)
        variant = MiningVariant.fromString(orig.readString()!!)
        cpuAffinity = orig.readString()!!
        cpuPriority = orig.readInt()
        debugParams = orig.readByte() != 0.toByte()
        silent = orig.readByte() != 0.toByte()
        reportOnlyBasics = orig.readByte() != 0.toByte()
        notification = orig.readParcelable(Notification::class.java.classLoader)
        smartStart = orig.readByte() != 0.toByte()
        updateOverInternet = orig.readByte() != 0.toByte()
    }

    //Each time the service starts, it checks for this file and if it is not present, it recreates it from the newest source available
    public fun updateLibrary() {
        if (context == null) {
            return
        }
        File("${context.filesDir.absolutePath}/tumine").delete()
    }

    public fun startMiningService() {
        val startMine = Intent(context, MiningService::class.java)
        startMine.putExtra("data", this)
        context?.startService(startMine)
    }

    public fun stopMiningService() {
        val startMine = Intent(context, MiningService::class.java)
        startMine.putExtra("action", "stop")
        context?.startService(startMine)
    }

    public fun changeMiningSpeed(threads: Int) {
        val startMine = Intent(context, MiningService::class.java)
        startMine.putExtra("action", "threads")
        startMine.putExtra("threads", threads)
        context?.startService(startMine)
    }

    companion object {

        @JvmField
        val CREATOR = object : Parcelable.Creator<InfoPassing> {
            override fun createFromParcel(source: Parcel): InfoPassing {
                return InfoPassing(source)
            }

            override fun newArray(size: Int) = arrayOfNulls<InfoPassing>(size)
        }
    }


}
