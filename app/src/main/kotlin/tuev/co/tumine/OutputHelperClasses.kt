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

public class OutputHelperClasses {
    public enum class ChangedValue {
        initInfo,

        message,

        debugInfo,

        sslVersion,

        startMiningInfo,
        usingPool,
        lastMiningJobResult,
        lastMiningJob,
        hashrate,
        lastError,


        hashratePerThread
    }

    public enum class CpuArchitecture(val value: Int) {
        x86(0),
        x64(1);

        public fun toInt(): Int {
            return value
        }
        companion object {

            public fun fromInt(`in`: Int): CpuArchitecture? {
                for (dow in CpuArchitecture.values()) {
                    // Use equalsIgnoreCase to make the getValue method a little more robust
                    if (dow.toInt() == `in`) {
                        return dow
                    }
                }
                return null
            }
        }
    }

    public data class VersionInfo(public var minerVersion: String,
                                  public var libuvVersion: String,
                                  public var compilerVersion: String)

    public data class BasicStartInfo(public var threads: Int,
                                     public var algorithm: MiningAlgorithm,
                                     public var miningVariant: Int)

    public data class CpuInfo(public var name: String,
                              public var cpuArchitecture: OutputHelperClasses.CpuArchitecture,
                              public var hardwareAES: Boolean,
                              public val l2Cache: Double,
                              public val l3Cache: Double)

    public data class MiningJobResult(public var error: String?,
                                      public var accepted: Int,
                                      public var rejected: Int,
                                      public var difficulty: Int,
                                      public var elapsedInMs: Int)

    public data class MiningJob(public var url: String,
                                public var difficulty: Int,
                                public var algorithm: MiningAlgorithm,
                                public var algoVariant: MiningVariant)

    public data class Hashrate(public var ShortInterval: Float?,
                               public var MediumInterval: Float?,
                               public var LargeInterval: Float?,
                               public var Highest: Float?)

    public data class HashratePerThread(public var index: Int,
                                 public val affinity: String,
                                 public var ShortInterval: Float?,
                                 public var MediumInterval: Float?,
                                 public var LargeInterval: Float?)

    public data class StartMiningInfo(public var threads: Int?,
                                      public var ways: Int?,
                                      public var memoryInMB: Float?)

    public data class InitInfo(public var pkg: String?,
                               public var paid: Boolean = false,
                               public var percentageToMe: Int?)

    public data class Debuginfo(public var basicStartInfo: OutputHelperClasses.BasicStartInfo?,
                                public var versionInfo: OutputHelperClasses.VersionInfo?,
                                public var cpuInfo: OutputHelperClasses.CpuInfo?,
                                public var pools: List<String>?)

    public data class SslInfo(public var tlsVersion: String?,
                              public var tlsFingerprint: String?)
}