package com.example.ca1giftcardwr.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.example.ca1giftcardwr.helpers.exists
import com.example.ca1giftcardwr.helpers.read
import com.example.ca1giftcardwr.helpers.write
import timber.log.Timber
import java.util.*

private const val JSON_FILE = "giftcards.json"

class GiftCardJSONStore(private val context: Context) {

    private var giftCards = mutableListOf<GiftCardModel>()
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val listType = object : TypeToken<ArrayList<GiftCardModel>>() {}.type

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    fun findAll(): List<GiftCardModel> {
        return giftCards
    }

    fun findOne(id: Long): GiftCardModel? {
        return giftCards.find { it.id == id }
    }

    fun create(giftCard: GiftCardModel) {
        giftCard.id = generateRandomId()
        giftCards.add(giftCard)
        serialize()
    }

    fun update(giftCard: GiftCardModel) {
        val foundCard = findOne(giftCard.id)
        if (foundCard != null) {
            foundCard.storeName = giftCard.storeName
            foundCard.balance = giftCard.balance
            foundCard.cardNumber = giftCard.cardNumber
            foundCard.expiryDate = giftCard.expiryDate
            foundCard.notes = giftCard.notes
        }
        serialize()
    }

    fun delete(giftCard: GiftCardModel) {
        giftCards.remove(giftCard)
        serialize()
    }

    private fun serialize() {
        val jsonString = gson.toJson(giftCards, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        giftCards = gson.fromJson(jsonString, listType)
    }

    private fun generateRandomId(): Long {
        return Random().nextLong()
    }
}