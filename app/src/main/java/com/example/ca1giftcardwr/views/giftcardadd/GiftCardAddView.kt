package com.example.ca1giftcardwr.views.giftcardadd

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ca1giftcardwr.R
import com.example.ca1giftcardwr.databinding.GiftcardAddBinding
import com.example.ca1giftcardwr.models.Location
import com.example.ca1giftcardwr.views.editlocation.EditLocationView
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class GiftCardAddView : AppCompatActivity() {

    private lateinit var binding: GiftcardAddBinding
    private lateinit var presenter: GiftCardAddPresenter
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private var location = Location()
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GiftcardAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = GiftCardAddPresenter(this)

        setSupportActionBar(binding.toolbarAdd)

        registerMapCallback()
        registerImageCallback()

        binding.giftCardExpiry.setOnClickListener {
            showDatePicker()
        }

        binding.btnChooseImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            imageIntentLauncher.launch(intent)
        }

        binding.btnSetLocation.setOnClickListener {
            val launcherIntent = Intent(this, EditLocationView::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        binding.btnAdd.setOnClickListener {
            val success = presenter.doAddGiftCard(
                storeName = binding.giftCardStore.text.toString(),
                balance = binding.giftCardBalance.text.toString(),
                cardNumber = binding.giftCardNumber.text.toString(),
                expiryDate = binding.giftCardExpiry.text.toString(),
                notes = binding.giftCardNotes.text.toString(),
                lat = location.lat,
                lng = location.lng,
                zoom = location.zoom,
                image = imageUri?.toString() ?: ""
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

    private fun registerMapCallback() {
        mapIntentLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    if (result.data != null) {
                        location = result.data!!.extras?.getParcelable("location")!!
                        binding.btnSetLocation.text = "Location Set ✓"
                    }
                }
            }
        }
    }

    private fun registerImageCallback() {
        imageIntentLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    result.data?.data?.let { uri ->
                        contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                        imageUri = uri
                        binding.giftCardImage.setImageURI(uri)
                        binding.giftCardImage.visibility = View.VISIBLE
                        binding.btnChooseImage.text = "Change Image ✓"
                    }
                }
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
    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}