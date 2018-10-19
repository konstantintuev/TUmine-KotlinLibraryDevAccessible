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

import android.annotation.TargetApi
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import java.io.*


/**
 * **IMPORTANT**: Make sure the user has internet
 * to initialize some files if you use the
 * *nonative* version in your app.
 */
public class InfoPassing(@Transient public var context: Context?) : Parcelable, Serializable {

    public val availableCores: Int
        get() = Runtime.getRuntime().availableProcessors()


    public class QuestionableUsefulness() : Parcelable, Serializable {
        /**
         * Cpu affinity set which cores will be used
         *
         * *Default*: "0x-1" - Auto
         */
        public var cpuAffinity: String = "0x-1"

        /**
         * Between 0 and 5
         *
         * *Default*: -1 - Auto
         */
        public var cpuPriority: Int = -1

        constructor(parcel: Parcel) : this() {
            cpuAffinity = parcel.readString()!!
            cpuPriority = parcel.readInt()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(cpuAffinity)
            parcel.writeInt(cpuPriority)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<QuestionableUsefulness> {
            override fun createFromParcel(parcel: Parcel): QuestionableUsefulness {
                return QuestionableUsefulness(parcel)
            }

            override fun newArray(size: Int): Array<QuestionableUsefulness?> {
                return arrayOfNulls(size)
            }
        }
    }

    //what output do you want to receive
    public class MinerOutput() : Parcelable, Serializable {

        //get updates about what is happening with the service using the ReceiveInfo connector
        /** These are the keys which will be used in the ReceiveInfo connector:
            - info, without error can be: lowMemory, stopped, started
            - info, with error: contains the error message
            - Logcat messages about some checks before function execution
        */
        public var isBasicLogging = true

        //review start params as received by the miner
        public var debugParams: Boolean = false

        //report ONLY simple hashrate, accepted and errors
        public var reportOnlyBasics: Boolean = false

        //if true the miner won't create a file used for communication between it and the kotlin library
        //might improve performance
        public var silent: Boolean = false

        constructor(parcel: Parcel) : this() {
            isBasicLogging = parcel.readByte() != 0.toByte()
            debugParams = parcel.readByte() != 0.toByte()
            reportOnlyBasics = parcel.readByte() != 0.toByte()
            silent = parcel.readByte() != 0.toByte()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeByte(if (isBasicLogging) 1 else 0)
            parcel.writeByte(if (debugParams) 1 else 0)
            parcel.writeByte(if (reportOnlyBasics) 1 else 0)
            parcel.writeByte(if (silent) 1 else 0)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<MinerOutput> {
            override fun createFromParcel(parcel: Parcel): MinerOutput {
                return MinerOutput(parcel)
            }

            override fun newArray(size: Int): Array<MinerOutput?> {
                return arrayOfNulls(size)
            }
        }
    }

    //the essential fields for the miner
    public class MinerConfig() : Parcelable, Serializable {

        /**
         * If enabled update the mining part over the internet.
         *
         * *INFO*: Checks for update on every start.
         */
        public var updateOverInternet = false

        /**
         * ANDROID_VERSION >= OREO
         *
         * Newer android versions don't let background apps
         * use high performance cores.
         * This makes the phone laggy when mining is started
         * from JobScheduler.
         *
         * It's recommended to use **1/4** of all cores here.
         * @see availableCores
         */
        public var coresWhenInAJob: Int = 0

        /**
         * *NOTE*: check my website for benchmarks
         *
         * **Important**: use the [availableCores] to know the hardware that you're dealing with
         */
        public var coresToUse: Int = 0

        /**
         * Download larger binary with SSL support (openssl included) if updateOverInternet is activated
         *
         * Because they are bigger I recommend to use ONLY the .jar file and let the miner download the binary when needed
         *
         * **Important**: If any of the pools uses ssl this will be enabled automatically
         */
        public var useSSL: Boolean = false

        /**
         * Which mining algorithm to use.
         *
         * This affects which coins you can mine.
         *
         * *Default*: [MiningAlgorithm.CRYPTONIGHT]
         */
        public var algorithm: MiningAlgorithm? = MiningAlgorithm.CRYPTONIGHT

        /**
         * Which mining variant to use.
         *
         * This affects which coins you can mine.
         *
         * *Default* & **RECOMMENDED**: [MiningVariant.VARIANT_AUTO]
         */
        public var variant: MiningVariant? = MiningVariant.VARIANT_AUTO

        /**
         * Mining pools to use.
         *
         * If one of them fails the miner will
         * switch to the next one.
         */
        public var pools: ArrayList<Pool> = ArrayList()

        constructor(parcel: Parcel) : this() {
            updateOverInternet = parcel.readByte() != 0.toByte()
            coresWhenInAJob = parcel.readInt()
            coresToUse = parcel.readInt()
            useSSL = parcel.readByte() != 0.toByte()
            algorithm = MiningAlgorithm.fromString(parcel.readString()!!)
            variant = MiningVariant.fromString(parcel.readString()!!)
            pools = parcel.readArrayList(Pool::class.java.classLoader) as ArrayList<Pool>
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeByte(if (updateOverInternet) 1 else 0)
            parcel.writeInt(coresWhenInAJob)
            parcel.writeInt(coresToUse)
            parcel.writeByte(if (useSSL) 1 else 0)
            parcel.writeString(algorithm!!.toString())
            parcel.writeString(variant!!.toString())
            parcel.writeList(pools)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<MinerConfig> {
            override fun createFromParcel(parcel: Parcel): MinerConfig {
                return MinerConfig(parcel)
            }

            override fun newArray(size: Int): Array<MinerConfig?> {
                return arrayOfNulls(size)
            }
        }
    }

    //all Android specific stuff needed for the mining to work
    public class MiningInAndroid() : Parcelable, Serializable {
        /**
         * The id which will be used in startForeground(int, Notification)
         */
        public var foregroundNotificationId = 6345

        /**
         * Keep CPU from sleeping
         *
         * **Important**: add '<uses-permission android:name="android.permission.WAKE_LOCK" />' to your manifest
         */
        public var keepCPUawake: Boolean = false

        /**
         * Extend this class and return your notfication, then set it here
         * **Important**: if not null will be used to start the service as foreground service
         */
        public var notificationLoaderClass: Class<*>? = null

        /**
         * Check cpu usage for 20 secs on start of the miner:
         * 75% or more - don't start mining, the OS is hard enough on the device
         * 50% to 75% - mine with only 1 thread
         */
        public var smartStart: Boolean = false

        /**
         * ANDROID_VERSION < OREO || Foreground Service
         *
         * check if service is running every 5 minutes
         * true -> enable
         * **Read**: false -> disable
         * This enables autosave of the passed [InfoPassing] object on the start of the miner
         */
        public var checkIfRunningEvery5mins: Boolean = false

        constructor(parcel: Parcel) : this() {
            foregroundNotificationId = parcel.readInt()
            keepCPUawake = parcel.readByte() != 0.toByte()
            val notifClass = parcel.readString()
            if (notifClass != null) {
                val rawClass = Class.forName(notifClass)
                notificationLoaderClass = rawClass
            }
            smartStart = parcel.readByte() != 0.toByte()
            checkIfRunningEvery5mins = parcel.readByte() != 0.toByte()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(foregroundNotificationId)
            parcel.writeByte(if (keepCPUawake) 1 else 0)
            parcel.writeString(notificationLoaderClass?.name)
            parcel.writeByte(if (smartStart) 1 else 0)
            parcel.writeByte(if (checkIfRunningEvery5mins) 1 else 0)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<MiningInAndroid> {
            override fun createFromParcel(parcel: Parcel): MiningInAndroid {
                return MiningInAndroid(parcel)
            }

            override fun newArray(size: Int): Array<MiningInAndroid?> {
                return arrayOfNulls(size)
            }
        }
    }

    public var miningInAndroid = MiningInAndroid()
    public var minerConfig = MinerConfig()
    public var minerOutput = MinerOutput()
    public var questionableUsefulness = QuestionableUsefulness()


    private constructor() : this(null)

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(miningInAndroid, 0)
        dest.writeParcelable(minerConfig, 0)
        dest.writeParcelable(minerOutput, 0)
        dest.writeParcelable(questionableUsefulness, 0)

    }

    constructor(orig: Parcel) : this() {
        val classLoader = javaClass.classLoader

        miningInAndroid = orig.readParcelable(classLoader) ?: return
        minerConfig = orig.readParcelable(classLoader) ?: return
        minerOutput = orig.readParcelable(classLoader) ?: return
        questionableUsefulness = orig.readParcelable(classLoader) ?: return
    }

    /**save this InfoPassing's state (fields) to storage
     * @see readState(Context) for retrieval of the saved InfoPassing's state
     */
    public fun saveState() {
        if (context == null) {
            return
        }
        val file = File(getDefaultTuminePrivateDir(context!!), "TUmine.info")
        if (file.exists()) {
            file.delete()
        }
        if (!file.createNewFile()) {
            return
        }
        val fos: FileOutputStream?
        val out: ObjectOutputStream?
        try {
            fos = FileOutputStream(file)
            out = ObjectOutputStream(fos)
            out.writeObject(this)
            out.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        val fileNotif = File(getDefaultTuminePrivateDir(context!!), "useNotification")
        if (miningInAndroid.notificationLoaderClass != null) {
            if (!fileNotif.exists()) {
                fileNotif.createNewFile()
            }
        } else {
            if (fileNotif.exists()) {
                fileNotif.delete()
            }
        }

    }

    //Each time the service starts, it checks for this file and if it is not present, it recreates it from the newest source available
    public fun updateLibrary() {
        if (context == null) {
            return
        }
        File("${getDefaultTuminePrivateDir(context!!).absolutePath}/tumine").delete()
    }

    public fun startMiningService() {
        if (context == null) {
            return
        }
        val startMine = Intent(context, MiningService::class.java)
        startMine.putExtra("data", this as Parcelable)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (miningInAndroid.notificationLoaderClass != null) {
                context!!.startForegroundService(startMine)
            } else {
                scheduleJob(context!!)
            }
        } else {
            context!!.startService(startMine)
        }
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

        @JvmField @Transient
        val CREATOR = object : Parcelable.Creator<InfoPassing> {
            override fun createFromParcel(source: Parcel): InfoPassing {
                return InfoPassing(source)
            }

            override fun newArray(size: Int) = arrayOfNulls<InfoPassing>(size)
        }

        //local miner version
        @Transient public val version = 6

        /**
         * May be null.
         * @param context passed as context for the restored 'InfoPassing'
         */
        fun readState(context: Context): InfoPassing? {
            val fis: FileInputStream?
            val `in`: ObjectInputStream?
            var outInfo: InfoPassing? = null
            val file = File(getDefaultTuminePrivateDir(context), "TUmine.info")
            if (!file.exists()) {
                return null
            }
            try {
                fis = FileInputStream(file)
                `in` = ObjectInputStream(fis)
                outInfo = `in`.readObject() as InfoPassing
                `in`.close()
                outInfo.context = context
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return outInfo
        }

        /**
         * start the mine service without passing a 'Info Passing' instance
         * @see readState(Context) is used to start the miner
         */
        public fun startMiningServiceRestoreState(context: Context) {
            val startMine = Intent(context, MiningService::class.java)
            startMine.putExtra("action", "restore")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (File(getDefaultTuminePrivateDir(context), "useNotification").exists()) {
                    context.startForegroundService(startMine)
                } else {
                    scheduleJob(context)
                }
            } else {
                context.startService(startMine)
            }
        }

        /**Our best case scenario for newer devices.
         * The new android versions forbid background service but
         * using JobScheduler we get around 7 mins (tested on Pie)
         * every 15 to 20 minutes of mining.
         *
         * The native miner first mines for you for 4 minutes
         * and then for me - your risk nothing:
         * The initialisation, connection, update and
         * all other task eat up from the mining time for me.
         */
        @TargetApi(Build.VERSION_CODES.O)
        private fun scheduleJob(context: Context) {
            val componentName = ComponentName(context, MineJobService::class.java)
            val jobInfo = JobInfo.Builder(2521, componentName)
                    .setRequiresCharging(false)
                    .setPeriodic(15 * 60 * 1000)
                    .build()

            val jobScheduler = context.applicationContext.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            val resultCode = jobScheduler.schedule(jobInfo)
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d("TUmine", "Job scheduled!")
            } else {
                Log.d("TUmine", "Job not scheduled")
            }
        }

        /**
         * @return the location where all of the miner's files are saved
         */
        fun getDefaultTuminePrivateDir(context: Context): File {
            val dir = File(context.filesDir.absolutePath, "tumineFiles")
            if (dir.exists()){
                return dir
            }
            dir.mkdir()
            return dir
        }
    }


}
