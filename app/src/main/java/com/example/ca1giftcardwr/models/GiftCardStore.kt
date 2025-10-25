package com.example.ca1giftcardwr.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GiftCardStore(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("GiftCards", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun save(giftCards: List<GiftCardModel>) {
        val json = gson.toJson(giftCards)
        sharedPreferences.edit().putString("cards", json).apply()
    }

    fun load(): ArrayList<GiftCardModel> {
        val json = sharedPreferences.getString("cards", null)
        return if (json != null) {
            val type = object : TypeToken<ArrayList<GiftCardModel>>() {}.type
            gson.fromJson(json, type)
        } else {
            ArrayList()
        }
    }
}