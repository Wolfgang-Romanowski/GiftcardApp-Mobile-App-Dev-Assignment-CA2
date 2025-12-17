package com.example.ca1giftcardwr.main

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.ca1giftcardwr.models.GiftCardModel
import com.example.ca1giftcardwr.models.GiftCardJSONStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application() {

    lateinit var giftCardStore: GiftCardJSONStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        giftCardStore = GiftCardJSONStore(applicationContext)
        i("Gift Card Manager started with ${giftCardStore.findAll().size} cards")

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val nightMode = prefs.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    fun findAll(): List<GiftCardModel> {
        return giftCardStore.findAll()
    }

    fun findById(id: Long): GiftCardModel? {
        return giftCardStore.findOne(id)
    }

    fun add(giftCard: GiftCardModel) {
        giftCardStore.create(giftCard)
        logAll()
    }

    fun update(giftCard: GiftCardModel) {
        giftCardStore.update(giftCard)
        logAll()
    }

    fun delete(giftCard: GiftCardModel) {
        giftCardStore.delete(giftCard)
        logAll()
    }

    private fun logAll() {
        giftCardStore.findAll().forEach { i("Gift Card: $it") }
    }
}