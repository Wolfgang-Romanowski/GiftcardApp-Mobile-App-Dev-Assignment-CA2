package com.example.ca1giftcardwr.views.giftcardedit

import android.os.Build
import com.example.ca1giftcardwr.main.MainApp
import com.example.ca1giftcardwr.models.GiftCardModel

class GiftCardEditPresenter(private val view: GiftCardEditView) {

    var giftCard = GiftCardModel()
    var app: MainApp = view.application as MainApp

    init {
        giftCard = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            view.intent.getParcelableExtra("gift_card", GiftCardModel::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            view.intent.getParcelableExtra<GiftCardModel>("gift_card")!!
        }
    }

    fun doUpdateGiftCard(
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
            app.update(giftCard)
            true
        } else {
            false
        }
    }

    fun doDelete() {
        app.delete(giftCard)
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
}