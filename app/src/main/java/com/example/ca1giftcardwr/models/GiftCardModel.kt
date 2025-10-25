package com.example.ca1giftcardwr.models

data class GiftCardModel(
    var id: Long = 0,
    var storeName: String = "",
    var balance: Double = 0.0,
    var cardNumber: String = "",
    var expiryDate: String = "",
    var notes: String = ""
)