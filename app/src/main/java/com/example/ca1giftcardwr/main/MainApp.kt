package com.example.ca1giftcardwr.main

import android.app.Application
import com.example.ca1giftcardwr.models.GiftCardModel
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application() {

    val giftCards = ArrayList<GiftCardModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Giftcard manager started")
    }

    fun add(giftCard: GiftCardModel) {
        giftCard.id = giftCards.size.toLong()
        giftCards.add(giftCard)
        logAll()
    }

    fun findAll(): List<GiftCardModel> {
        return giftCards
    }

    private fun logAll() {
        giftCards.forEach { i("Gift Card: $it") }
    }
}