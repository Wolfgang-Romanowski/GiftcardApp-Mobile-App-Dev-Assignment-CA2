package com.example.ca1giftcardwr.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ca1giftcardwr.R
import com.example.ca1giftcardwr.databinding.GiftcardListBinding
import com.example.ca1giftcardwr.databinding.CardGiftcardBinding
import com.example.ca1giftcardwr.main.MainApp
import com.example.ca1giftcardwr.models.GiftCardModel

class GiftcardList : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: GiftcardListBinding
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GiftcardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        setSupportActionBar(binding.toolbar)

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        loadGiftCards()
        registerRefreshCallback()
    }

    private fun loadGiftCards() {
        val cards = app.findAll()

        if (cards.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
            binding.recyclerView.adapter = GiftCardAdapter(cards)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, GiftCardAdd::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                loadGiftCards()
            }
    }
}

// Adapter for RecyclerView
class GiftCardAdapter(private var giftCards: List<GiftCardModel>) :
    RecyclerView.Adapter<GiftCardAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGiftcardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding) }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val giftCard = giftCards[holder.bindingAdapterPosition]
        holder.bind(giftCard)

        holder.itemView.alpha = 0f
        holder.itemView.animate()
            .alpha(1f)
            .setDuration(300)
            .start()
    }

    override fun getItemCount(): Int = giftCards.size

    class MainHolder(private val binding: CardGiftcardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(giftCard: GiftCardModel) {
            binding.giftCardStore.text = giftCard.storeName
            binding.giftCardBalance.text = "$${String.format("%.2f", giftCard.balance)}"
            binding.giftCardExpiry.text = if (giftCard.expiryDate.isNotEmpty()) {
                "Expires: ${giftCard.expiryDate}"
            } else {
                "No expiry date"
            }
        }
    }
}