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
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ca1giftcardwr.R
import com.example.ca1giftcardwr.databinding.CardGiftcardBinding
import com.example.ca1giftcardwr.databinding.GiftcardListBinding
import com.example.ca1giftcardwr.main.MainApp
import com.example.ca1giftcardwr.models.GiftCardModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

interface GiftCardListener {
    fun onGiftCardClick(giftCard: GiftCardModel)
}

class GiftcardList : AppCompatActivity(), GiftCardListener,
    NavigationView.OnNavigationItemSelectedListener {

    lateinit var app: MainApp
    private lateinit var binding: GiftcardListBinding
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var searchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GiftcardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        setSupportActionBar(binding.toolbar)

        setupNavigationDrawer()

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager


        // Swipe to delete
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val cards = app.findAll() as ArrayList<GiftCardModel>
                val deletedCard = cards[position]

                app.delete(deletedCard)
                loadGiftCards()

                Snackbar.make(binding.root, "Card deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        app.add(deletedCard)
                        loadGiftCards()
                    }
                    .show()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        loadGiftCards()
        registerRefreshCallback()
    }

    private fun setupNavigationDrawer() {
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_cards -> {}
            R.id.nav_add -> {
                val intent = Intent(this, GiftCardAdd::class.java)
                refreshIntentLauncher.launch(intent)
            }
            R.id.nav_about -> {
                Snackbar.make(binding.root, "Gift Card Manager v2.0", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }

    private fun loadGiftCards() {
        filterGiftCards()
    }

    private fun filterGiftCards() {
        val allCards = app.findAll()
        val filteredCards = if (searchQuery.isEmpty()) {
            allCards
        } else {
            allCards.filter { card ->
                card.storeName.contains(searchQuery, ignoreCase = true) ||
                        card.cardNumber.contains(searchQuery, ignoreCase = true)
            }
        }

        if (filteredCards.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
            binding.recyclerView.adapter = GiftCardAdapter(filteredCards, this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.item_search)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery = newText ?: ""
                filterGiftCards()
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
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
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    loadGiftCards()
                }
            }
    }

    override fun onGiftCardClick(giftCard: GiftCardModel) {
        val intent = Intent(this, GiftCardEdit::class.java).apply {
            putExtra("gift_card", giftCard)
        }
        refreshIntentLauncher.launch(intent)
    }
}

class GiftCardAdapter(
    private var giftCards: List<GiftCardModel>,
    private val listener: GiftCardListener
) : RecyclerView.Adapter<GiftCardAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGiftcardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val giftCard = giftCards[holder.bindingAdapterPosition]
        holder.bind(giftCard, listener)

        holder.itemView.alpha = 0f
        holder.itemView.animate()
            .alpha(1f)
            .setDuration(300)
            .start()
    }

    override fun getItemCount(): Int = giftCards.size

    class MainHolder(private val binding: CardGiftcardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(giftCard: GiftCardModel, listener: GiftCardListener) {
            binding.giftCardStore.text = giftCard.storeName
            binding.giftCardBalance.text = "$${String.format("%.2f", giftCard.balance)}"
            binding.giftCardExpiry.text = if (giftCard.expiryDate.isNotEmpty()) {
                "Expires: ${giftCard.expiryDate}"
            } else {
                "No expiry date"
            }

            binding.root.setOnClickListener {
                listener.onGiftCardClick(giftCard)
            }
        }
    }
}