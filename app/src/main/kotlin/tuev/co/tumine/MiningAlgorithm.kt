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

public enum class MiningAlgorithm(public val algo: String) {

    /*
     Long name	        Short name	  Base algorithm	Variant	                    Notes
    cryptonight	            cn	           cn	           -1	        Autodetect works only for Monero.
    cryptonight/0	        cn/0	       cn	           0	        Original/old CryptoNight.
    cryptonight/1	        cn/1	       cn	           1	        Also known as monero7 and CryptoNightV7.
    cryptonight/2	        cn/2	       cn	           2	        Also known as CryptoNightV8 - 18.10.18 Hard Fork
    cryptonight/xtl	        cn/xtl	       cn	           "xtl"        Stellite (XTL).
    cryptonight/msr	        cn/msr	       cn	           "msr"        Masari (MSR), also known as cryptonight-fast
    cryptonight/xao	        cn/xao	       cn	           "xao"        Alloy (XAO)
    cryptonight/rto	        cn/rto	       cn	           "rto"        Arto (RTO)
    cryptonight-lite	    cn-lite	       cn-lite	       -1	        Autodetect works only for Aeon.
    cryptonight-lite/0	    cn-lite/0	   cn-lite	       0	        Original/old CryptoNight-Lite.
    cryptonight-lite/1	    cn-lite/1	   cn-lite	       1	        Also known as aeon7
    cryptonight-lite/ipbc	cn-lite/ipbc   cn-lite	       "ipbc"	    IPBC variant, obsolete
    cryptonight-heavy	    cn-heavy	   cn-heavy	       0	        Ryo and Loki
    cryptonight-heavy/xhv	cn-heavy/xhv   cn-heavy	       "xhv"	    Haven Protocol
    cryptonight-heavy/tube	cn-heavy/tube  cn-heavy	       "tube"	    BitTube (TUBE)
     */

    CRYPTONIGHT("cryptonight"),
    CRYPTONIGHT_LITE("cryptonight-lite"),
    CRYPTONIGHT_HEAVY("cryptonight-heavy");

    public override fun toString(): String {
        return algo
    }

    companion object {

        fun fromString(`in`: String): MiningAlgorithm? {
            for (dow in MiningAlgorithm.values()) {
                // Use equalsIgnoreCase to make the getValue method a little more robust
                if (dow.toString().equals(`in`, ignoreCase = true)) {
                    return dow
                }
            }
            return null
        }
    }
}
