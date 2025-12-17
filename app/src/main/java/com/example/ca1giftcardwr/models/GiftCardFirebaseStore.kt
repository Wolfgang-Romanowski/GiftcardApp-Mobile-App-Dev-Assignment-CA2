package com.example.ca1giftcardwr.models

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class GiftCardFirebaseStore {

    private val db: FirebaseFirestore = Firebase.firestore
    private val collection = db.collection("giftcards")
    private var giftCards = mutableListOf<GiftCardModel>()

    private var onDataChangedListener: (() -> Unit)? = null

    init {
        collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Timber.e("Firebase listen failed: $error")
                return@addSnapshotListener
            }

            giftCards.clear()
            snapshot?.documents?.forEach { doc ->
                val card = GiftCardModel(
                    id = doc.id.hashCode().toLong(),
                    firebaseId = doc.id,
                    storeName = doc.getString("storeName") ?: "",
                    balance = doc.getDouble("balance") ?: 0.0,
                    cardNumber = doc.getString("cardNumber") ?: "",
                    expiryDate = doc.getString("expiryDate") ?: "",
                    notes = doc.getString("notes") ?: "",
                    lat = doc.getDouble("lat") ?: 0.0,
                    lng = doc.getDouble("lng") ?: 0.0,
                    zoom = (doc.getDouble("zoom") ?: 0.0).toFloat(),
                    image = doc.getString("image") ?: ""
                )
                giftCards.add(card)
            }
            Timber.i("Firebase: Loaded ${giftCards.size} cards")
            onDataChangedListener?.invoke()
        }
    }

    fun setOnDataChangedListener(listener: () -> Unit) {
        onDataChangedListener = listener
    }

    fun findAll(): List<GiftCardModel> {
        return giftCards.toList()
    }

    fun findOne(id: Long): GiftCardModel? {
        return giftCards.find { it.id == id }
    }

    fun create(giftCard: GiftCardModel) {
        val data = hashMapOf(
            "storeName" to giftCard.storeName,
            "balance" to giftCard.balance,
            "cardNumber" to giftCard.cardNumber,
            "expiryDate" to giftCard.expiryDate,
            "notes" to giftCard.notes,
            "lat" to giftCard.lat,
            "lng" to giftCard.lng,
            "zoom" to giftCard.zoom.toDouble(),
            "image" to giftCard.image
        )

        collection.add(data)
            .addOnSuccessListener { docRef ->
                Timber.i("Card added with ID: ${docRef.id}")
            }
            .addOnFailureListener { e ->
                Timber.e("Error adding card: $e")
            }
    }

    fun update(giftCard: GiftCardModel) {
        val data = hashMapOf(
            "storeName" to giftCard.storeName,
            "balance" to giftCard.balance,
            "cardNumber" to giftCard.cardNumber,
            "expiryDate" to giftCard.expiryDate,
            "notes" to giftCard.notes,
            "lat" to giftCard.lat,
            "lng" to giftCard.lng,
            "zoom" to giftCard.zoom.toDouble(),
            "image" to giftCard.image
        )

        giftCard.firebaseId?.let { id ->
            collection.document(id).set(data)
                .addOnSuccessListener { Timber.i("Card updated") }
                .addOnFailureListener { e -> Timber.e("Error updating: $e") }
        }
    }

    fun delete(giftCard: GiftCardModel) {
        giftCard.firebaseId?.let { id ->
            collection.document(id).delete()
                .addOnSuccessListener { Timber.i("Card deleted") }
                .addOnFailureListener { e -> Timber.e("Error deleting: $e") }
        }
    }
}