package com.example.ca1giftcardwr.views.giftcardadd

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.ca1giftcardwr.R
import com.example.ca1giftcardwr.databinding.GiftcardAddBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class GiftCardAddView : AppCompatActivity() {

    private lateinit var binding: GiftcardAddBinding
    private lateinit var presenter: GiftCardAddPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GiftcardAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = GiftCardAddPresenter(this)

        setSupportActionBar(binding.toolbarAdd)

        binding.giftCardExpiry.setOnClickListener {
            showDatePicker()
        }

        binding.btnAdd.setOnClickListener {
            val success = presenter.doAddGiftCard(
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_giftcard_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> presenter.doCancel()
        }
        return super.onOptionsItemSelected(item)
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
}