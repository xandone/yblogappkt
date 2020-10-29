package com.app.xandone.yblogapp.model.bean

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * author: Admin
 * created on: 2020/9/9 17:36
 * description:
 */
class CodeTypeBean : Parcelable, Serializable {
    /**
     * count : 2
     * typeName : 编程
     * type : 0
     */
    var count = 0
    var typeName: String?
    var type: Int

    constructor(typeName: String?, type: Int) {
        this.typeName = typeName
        this.type = type
    }

    protected constructor(`in`: Parcel?) {
        count = `in`!!.readInt()
        typeName = `in`.readString()
        type = `in`.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeInt(count)
        dest.writeString(typeName)
        dest.writeInt(type)
    }

    companion object {
        val CREATOR: Parcelable.Creator<CodeTypeBean?>? =
            object : Parcelable.Creator<CodeTypeBean?> {
                override fun createFromParcel(`in`: Parcel?): CodeTypeBean? {
                    return CodeTypeBean(`in`)
                }

                override fun newArray(size: Int): Array<CodeTypeBean?>? {
                    return arrayOfNulls<CodeTypeBean?>(size)
                }
            }
    }
}