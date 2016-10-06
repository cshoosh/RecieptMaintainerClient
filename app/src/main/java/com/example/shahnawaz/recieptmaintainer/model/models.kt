package com.example.shahnawaz.recieptmaintainer.model

import java.util.*
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Shahnawaz on 6/13/2016.
 */

data class Data(val _id: Int, val amount: Int, val description: String, val date: String, val credit: Int
                , val user: Int) : Parcelable {
    constructor(source: Parcel) : this(source.readInt(), source.readInt(), source.readString(), source.readString(), source.readInt(), source.readInt())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(_id)
        dest?.writeInt(amount)
        dest?.writeString(description)
        dest?.writeString(date)
        dest?.writeInt(credit)
        dest?.writeInt(user)
    }

    companion object {
        @JvmField final val CREATOR: Parcelable.Creator<Data> = object : Parcelable.Creator<Data> {
            override fun createFromParcel(source: Parcel): Data {
                return Data(source)
            }

            override fun newArray(size: Int): Array<Data?> {
                return arrayOfNulls(size)
            }
        }
    }
}

data class ListData(val list: List<Data>)

data class CalculateModel(val diff: Int, val sum_credit: Int, val sum_paid: Int)