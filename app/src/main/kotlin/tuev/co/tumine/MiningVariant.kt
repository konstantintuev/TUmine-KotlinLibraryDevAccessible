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

public enum class MiningVariant(private val variant: String) {

    VARIANT_AUTO("auto"),
    VARIANT_0   ("0"),
    VARIANT_1   ("1"),
    VARIANT_TUBE("tube"),
    VARIANT_XTL ("xtl"),
    VARIANT_MSR ("msr"),
    VARIANT_XHV ("xhv");

    public override fun toString(): String {
        return variant
    }

    companion object {

        public fun fromString(`in`: String): MiningVariant? {
            for (dow in MiningVariant.values()) {
                // Use equalsIgnoreCase to make the getValue method a little more robust
                if (dow.toString().equals(`in`, ignoreCase = true)) {
                    return dow
                }
            }
            return null
        }
    }
}
