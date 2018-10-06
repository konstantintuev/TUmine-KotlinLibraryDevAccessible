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

public class OutputInfo {
    public var lastChangedValue: OutputHelperClasses.ChangedValue? = null

    public var initInfo: OutputHelperClasses.InitInfo? = null

    public var message: String? = null

    public var debugInfo: OutputHelperClasses.Debuginfo? = null

    public var startMiningInfo: OutputHelperClasses.StartMiningInfo? = null
    public var usingPool: Pool? = null
    public var lastMiningJobResult: OutputHelperClasses.MiningJobResult? = null
    public var lastMiningJob: OutputHelperClasses.MiningJob? = null
    public var hashrate: OutputHelperClasses.Hashrate? = null
    public var lastError: String? = null


    public var hashratePerThread: ArrayList<OutputHelperClasses.HashratePerThread>? = null
}