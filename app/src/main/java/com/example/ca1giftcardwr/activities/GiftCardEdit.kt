package com.example.ca1giftcardwr.activities

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ca1giftcardwr.databinding.GiftcardEditBinding
import com.example.ca1giftcardwr.main.MainApp
import com.example.ca1giftcardwr.models.GiftCardModel
import com.google.android.material.snackbar.Snackbar

class GiftCardEdit : AppCompatActivity() {

    private lateinit var binding: GiftcardEditBinding
    lateinit var app: MainApp
    lateinit var giftCard: GiftCardModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GiftcardEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        giftCard = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("gift_card", GiftCardModel::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<GiftCardModel>("gift_card")!!
        }

        binding.giftCardStore.setText(giftCard.storeName)
        binding.giftCardBalance.setText(giftCard.balance.toString())
        binding.giftCardNumber.setText(giftCard.cardNumber)
        binding.giftCardExpiry.setText(giftCard.expiryDate)
        binding.giftCardNotes.setText(giftCard.notes)

        setSupportActionBar(binding.toolbarEdit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnUpdate.setOnClickListener {
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
                app.update(giftCard)
                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar.make(it, "Please enter store name and balance", Snackbar.LENGTH_LONG)
                    .show()
            } }
        binding.btnDelete.setOnClickListener {
            app.delete(giftCard)
            setResult(RESULT_OK)
            finish()
        } }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}