package com.example.ca1giftcardwr.main

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.ca1giftcardwr.models.GiftCardModel
import com.example.ca1giftcardwr.models.GiftCardJSONStore
import com.example.ca1giftcardwr.models.GiftCardFirebaseStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application() {

    //json
    //lateinit var giftCardStore: GiftCardJSONStore

    // firebase Storage
    lateinit var giftCardStore: GiftCardFirebaseStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        //json
        //giftCardStore = GiftCardJSONStore(applicationContext)

        // firebase
        giftCardStore = GiftCardFirebaseStore()

        i("Gift Card Manager started")

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

    fun setOnDataChangedListener(listener: () -> Unit) {
        giftCardStore.setOnDataChangedListener(listener)
    }

    private fun logAll() {
        giftCardStore.findAll().forEach { i("Gift Card: $it") }
    }
}