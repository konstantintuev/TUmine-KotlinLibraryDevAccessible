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

import android.os.Parcel
import android.os.Parcelable

//the pool where you want to mine monero (XMR) - look at this: http://moneropools.com/
data class Pool(val url: String,
        //specify appropriate username based on the mining pool you are using
                val user: String,
        //specify appropriate password based on the mining pool you are using
                val password: String,// - this is important for earnings tacking
                val rigID: String = "null",
        //send keepAlive message to the server every 'keepAlive' seconds
        //1 = default, 0 = off, custom = seconds
                val keepAlive: Int = 1,
                val nicehash: Boolean = false,
                var ip: String? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(user)
        parcel.writeString(password)
        parcel.writeString(rigID)
        parcel.writeInt(keepAlive)
        parcel.writeByte(if (nicehash) 1 else 0)
        parcel.writeString(ip)
    }

    override fun describeContents(): Int {
        return 0
    }

    public companion object CREATOR : Parcelable.Creator<Pool> {
        override fun createFromParcel(parcel: Parcel): Pool {
            return Pool(parcel)
        }

        override fun newArray(size: Int): Array<Pool?> {
            return arrayOfNulls(size)
        }
    }
}