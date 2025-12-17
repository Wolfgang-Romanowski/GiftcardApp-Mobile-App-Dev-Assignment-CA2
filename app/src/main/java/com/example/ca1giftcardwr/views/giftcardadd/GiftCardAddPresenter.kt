package com.example.ca1giftcardwr.views.giftcardadd

import com.example.ca1giftcardwr.main.MainApp
import com.example.ca1giftcardwr.models.GiftCardModel

class GiftCardAddPresenter(private val view: GiftCardAddView) {

    var giftCard = GiftCardModel()
    var app: MainApp = view.application as MainApp

    fun doAddGiftCard(
        storeName: String,
        balance: String,
        cardNumber: String,
        expiryDate: String,
        notes: String,
        lat: Double,
        lng: Double,
        zoom: Float,
        image: String
    ): Boolean {
        giftCard.storeName = storeName
        giftCard.balance = parseBalance(balance)
        giftCard.cardNumber = cardNumber
        giftCard.expiryDate = expiryDate
        giftCard.notes = notes
        giftCard.lat = lat
        giftCard.lng = lng
        giftCard.zoom = zoom
        giftCard.image = image

        return if (giftCard.storeName.isNotEmpty() && giftCard.balance > 0) {
            app.add(giftCard.copy())
            true
        } else {
            false
        }
    }

    private fun parseBalance(balanceText: String): Double {
        return if (balanceText.isNotEmpty()) {
            try {
                balanceText.toDouble()
            } catch (e: NumberFormatException) {
                0.0
            }
        } else {
            0.0
        }
    }

    fun doCancel() {
        view.finish()
    }
}