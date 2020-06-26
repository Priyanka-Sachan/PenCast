package com.example.pencast.ui.friend

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Friend(
    var uid: String,
    var username: String,
    var profileImage: String,
    var status: String
) : Parcelable {
    constructor() : this("", "", "", "")
}
