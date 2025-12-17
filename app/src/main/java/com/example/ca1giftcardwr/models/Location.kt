package com.example.ca1giftcardwr.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    var lat: Double = 53.3498,
    var lng: Double = -6.2603,
    var zoom: Float = 15f
) : Parcelable