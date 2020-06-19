package com.example.pencast.ui.friend

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Friend(var uid: String, var profileImage: String, var username: String) : Parcelable{
    constructor():this("","","")
}
