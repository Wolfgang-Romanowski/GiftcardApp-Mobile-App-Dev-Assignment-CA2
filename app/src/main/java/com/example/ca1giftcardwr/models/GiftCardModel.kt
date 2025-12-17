package com.example.ca1giftcardwr.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GiftCardModel(
    var id: Long = 0,
    var storeName: String = "",
    var balance: Double = 0.0,
    var cardNumber: String = "",
    var expiryDate: String = "",
    var notes: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f,
    var image: String = ""
) : Parcelable
