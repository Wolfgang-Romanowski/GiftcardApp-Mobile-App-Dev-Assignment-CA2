package com.example.ca1giftcardwr.views.giftcardedit

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ca1giftcardwr.R
import com.example.ca1giftcardwr.databinding.GiftcardEditBinding
import com.example.ca1giftcardwr.models.Location
import com.example.ca1giftcardwr.views.editlocation.EditLocationView
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class GiftCardEditView : AppCompatActivity() {

    private lateinit var binding: GiftcardEditBinding
    private lateinit var presenter: GiftCardEditPresenter
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private var location = Location()
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GiftcardEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = GiftCardEditPresenter(this)
        showGiftCard(presenter.giftCard)
        setSupportActionBar(binding.toolbarEdit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //load gift card form location
        location.lat = presenter.giftCard.lat
        location.lng = presenter.giftCard.lng
        location.zoom = presenter.giftCard.zoom

        if (presenter.giftCard.zoom != 0f) {
            binding.btnSetLocation.text = "Location Set ✓"
        }

        //load existing image
        if (presenter.giftCard.image.isNotEmpty()) {
            try {
                imageUri = Uri.parse(presenter.giftCard.image)
                binding.giftCardImage.setImageURI(imageUri)
                binding.giftCardImage.visibility = View.VISIBLE
                binding.btnChooseImage.text = "Change Image ✓"
            } catch (e: Exception) {
            }
        }

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

        binding.btnUpdate.setOnClickListener {
            val success = presenter.doUpdateGiftCard(
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

        binding.btnDelete.setOnClickListener {
            presenter.doDelete()
            setResult(RESULT_OK)
            finish()
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

    private fun showGiftCard(giftCard: com.example.ca1giftcardwr.models.GiftCardModel) {
        binding.giftCardStore.setText(giftCard.storeName)
        binding.giftCardBalance.setText(giftCard.balance.toString())
        binding.giftCardNumber.setText(giftCard.cardNumber)
        binding.giftCardExpiry.setText(giftCard.expiryDate)
        binding.giftCardNotes.setText(giftCard.notes)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val months = arrayOf("January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December")
        val years = (currentYear..currentYear + 10).map { it.toString() }.toTypedArray()
        val dialogView = layoutInflater.inflate(R.layout.dialog_month_year_picker, null)
        val monthSpinner = dialogView.findViewById<android.widget.Spinner>(R.id.monthSpinner)
        val yearSpinner = dialogView.findViewById<android.widget.Spinner>(R.id.yearSpinner)
        val spinnerLayout = android.R.layout.simple_spinner_dropdown_item

        monthSpinner.adapter = android.widget.ArrayAdapter(this, spinnerLayout, months)
        yearSpinner.adapter = android.widget.ArrayAdapter(this, spinnerLayout, years)

        monthSpinner.setSelection(currentMonth)
        yearSpinner.setSelection(0)

        val dialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
            .setTitle("Select Expiry Date")
            .setView(dialogView)
            .setPositiveButton("Set") { _, _ ->
                val selectedMonth = monthSpinner.selectedItemPosition + 1
                val selectedYear = years[yearSpinner.selectedItemPosition]
                val formattedDate = String.format("%02d/%s", selectedMonth, selectedYear)
                binding.giftCardExpiry.setText(formattedDate)
            }
            .setNegativeButton("Cancel", null)
            .show()

        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            ?.setTextColor(getColor(R.color.accent_light))
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE)
            ?.setTextColor(getColor(R.color.accent_light))
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}