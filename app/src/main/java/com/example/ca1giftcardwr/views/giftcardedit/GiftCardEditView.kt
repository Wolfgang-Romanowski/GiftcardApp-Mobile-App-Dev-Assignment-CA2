package com.example.ca1giftcardwr.views.giftcardedit

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ca1giftcardwr.databinding.GiftcardEditBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class GiftCardEditView : AppCompatActivity() {

    private lateinit var binding: GiftcardEditBinding
    private lateinit var presenter: GiftCardEditPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GiftcardEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = GiftCardEditPresenter(this)
        showGiftCard(presenter.giftCard)
        setSupportActionBar(binding.toolbarEdit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.giftCardExpiry.setOnClickListener {
            showDatePicker()
        }

        binding.btnUpdate.setOnClickListener {
            val success = presenter.doUpdateGiftCard(
                storeName = binding.giftCardStore.text.toString(),
                balance = binding.giftCardBalance.text.toString(),
                cardNumber = binding.giftCardNumber.text.toString(),
                expiryDate = binding.giftCardExpiry.text.toString(),
                notes = binding.giftCardNotes.text.toString()
            )

            if (success) {
                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar.make(it, "Please enter store name and balance", Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.btnDelete.setOnClickListener {
            presenter.doDelete()
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun showGiftCard(giftCard: com.example.ca1giftcardwr.models.GiftCardModel) {
        binding.giftCardStore.setText(giftCard.storeName)
        binding.giftCardBalance.setText(giftCard.balance.toString())
        binding.giftCardNumber.setText(giftCard.cardNumber)
        binding.giftCardExpiry.setText(giftCard.expiryDate)
        binding.giftCardNotes.setText(giftCard.notes)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, _ ->
                val formattedDate = String.format("%02d/%04d", selectedMonth + 1, selectedYear)
                binding.giftCardExpiry.setText(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}