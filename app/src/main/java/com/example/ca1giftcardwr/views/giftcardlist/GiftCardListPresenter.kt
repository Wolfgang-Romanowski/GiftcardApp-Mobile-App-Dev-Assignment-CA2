package com.example.ca1giftcardwr.views.giftcardlist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ca1giftcardwr.main.MainApp
import com.example.ca1giftcardwr.models.GiftCardModel
import com.example.ca1giftcardwr.views.giftcardadd.GiftCardAddView
import com.example.ca1giftcardwr.views.giftcardedit.GiftCardEditView

class GiftCardListPresenter(private val view: GiftCardListView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>

    init {
        registerRefreshCallback()
    }

    fun getGiftCards(): List<GiftCardModel> {
        return app.findAll()
    }

    fun filterGiftCards(searchQuery: String): List<GiftCardModel> {
        val allCards = app.findAll()
        return if (searchQuery.isEmpty()) {
            allCards
        } else {
            allCards.filter { card ->
                card.storeName.contains(searchQuery, ignoreCase = true) ||
                        card.cardNumber.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    fun doAddGiftCard() {
        val intent = Intent(view, GiftCardAddView::class.java)
        view.startActivity(intent)
        view.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    fun doEditGiftCard(giftCard: GiftCardModel) {
        val launcherIntent = Intent(view, GiftCardEditView::class.java).apply {
            putExtra("gift_card", giftCard)
        }
        refreshIntentLauncher.launch(launcherIntent)
        view.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    fun doDeleteGiftCard(giftCard: GiftCardModel) {
        app.delete(giftCard)
    }

    fun doUndoDelete(giftCard: GiftCardModel) {
        app.add(giftCard)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher = view.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                view.onRefresh()
            }
        }
    }
}