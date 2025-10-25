package com.example.ca1giftcardwr.main

import android.app.Application
import com.example.ca1giftcardwr.models.GiftCardModel
import com.example.ca1giftcardwr.models.GiftCardStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application() {

    lateinit var store: GiftCardStore
    val giftCards = ArrayList<GiftCardModel>()
    private var nextId = 1L

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        store = GiftCardStore(applicationContext)

        // Load saved cards
        giftCards.addAll(store.load())

        // Set nextId based on existing cards
        if (giftCards.isNotEmpty()) {
            nextId = giftCards.maxOf { it.id } + 1
        }

        i("Giftcard manager started with ${giftCards.size} cards")
    }

    fun add(giftCard: GiftCardModel) {
        giftCard.id = nextId++
        giftCards.add(giftCard)
        store.save(giftCards)
        logAll()
    }

    fun update(giftCard: GiftCardModel) {
        val index = giftCards.indexOfFirst { it.id == giftCard.id }
        if (index != -1) {
            giftCards[index] = giftCard
            store.save(giftCards)
            logAll()
        }
    }

    fun delete(giftCard: GiftCardModel) {
        giftCards.remove(giftCard)
        store.save(giftCards)
        logAll()
    }

    fun findAll(): List<GiftCardModel> {
        return giftCards
    }

    fun findById(id: Long): GiftCardModel? {
        return giftCards.find { it.id == id }
    }

    private fun logAll() {
        giftCards.forEach { i("Gift Card: $it") }
    }
}