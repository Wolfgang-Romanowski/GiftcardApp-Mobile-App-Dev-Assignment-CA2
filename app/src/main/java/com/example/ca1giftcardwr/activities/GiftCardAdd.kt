package com.example.ca1giftcardwr.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ca1giftcardwr.databinding.GiftcardAddBinding
import com.example.ca1giftcardwr.main.MainApp
import com.example.ca1giftcardwr.models.GiftCardModel
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber.Forest.i

class GiftCardAdd : AppCompatActivity() {

    private lateinit var binding: GiftcardAddBinding
    lateinit var app: MainApp
    var giftCard = GiftCardModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GiftcardAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        setSupportActionBar(binding.toolbarAdd)

        binding.btnAdd.setOnClickListener {
            giftCard.storeName = binding.giftCardStore.text.toString()

            val balanceText = binding.giftCardBalance.text.toString()
            giftCard.balance = if (balanceText.isNotEmpty()) {
                try {
                    balanceText.toDouble()
                } catch (e: NumberFormatException) {
                    0.0
                }
            } else {
                0.0
            }

            giftCard.cardNumber = binding.giftCardNumber.text.toString()
            giftCard.expiryDate = binding.giftCardExpiry.text.toString()
            giftCard.notes = binding.giftCardNotes.text.toString()

            if (giftCard.storeName.isNotEmpty() && giftCard.balance > 0) {
                app.add(giftCard.copy())
                i("Gift Card added: $giftCard")
                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar.make(it, "Please enter store name and balance", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}