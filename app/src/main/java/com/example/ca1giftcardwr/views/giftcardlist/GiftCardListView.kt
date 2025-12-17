package com.example.ca1giftcardwr.views.giftcardlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ca1giftcardwr.R
import com.example.ca1giftcardwr.databinding.CardGiftcardBinding
import com.example.ca1giftcardwr.databinding.GiftcardListBinding
import com.example.ca1giftcardwr.models.GiftCardModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

interface GiftCardListener {
    fun onGiftCardClick(giftCard: GiftCardModel)
}

class GiftCardListView : AppCompatActivity(), GiftCardListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: GiftcardListBinding
    private lateinit var presenter: GiftCardListPresenter
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var searchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val nightMode = prefs.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(nightMode)

        binding = GiftcardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize presenter
        presenter = GiftCardListPresenter(this)

        setSupportActionBar(binding.toolbar)
        setupNavigationDrawer()

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        setupSwipeToDelete()
        loadGiftCards()
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

    private fun setupSwipeToDelete() {
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
                val cards = presenter.getGiftCards() as ArrayList<GiftCardModel>
                val deletedCard = cards[position]

                presenter.doDeleteGiftCard(deletedCard)
                loadGiftCards()

                Snackbar.make(binding.root, "Card deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        presenter.doUndoDelete(deletedCard)
                        loadGiftCards()
                    }
                    .show()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_cards -> {}
            R.id.nav_add -> presenter.doAddGiftCard()
            R.id.nav_night_mode -> {
                val currentMode = AppCompatDelegate.getDefaultNightMode()
                val newMode = if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.MODE_NIGHT_NO
                } else {
                    AppCompatDelegate.MODE_NIGHT_YES
                }
                val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                prefs.edit().putInt("night_mode", newMode).apply()
                AppCompatDelegate.setDefaultNightMode(newMode)
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
        val filteredCards = presenter.filterGiftCards(searchQuery)

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
                loadGiftCards()
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
            R.id.item_add -> presenter.doAddGiftCard()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGiftCardClick(giftCard: GiftCardModel) {
        presenter.doEditGiftCard(giftCard)
    }
    fun onRefresh() {
        loadGiftCards()
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

            // Show image or initial
            if (giftCard.image.isNotEmpty()) {
                try {
                    binding.storeImage.setImageURI(android.net.Uri.parse(giftCard.image))
                    binding.storeImage.visibility = View.VISIBLE
                    binding.storeInitial.visibility = View.GONE
                } catch (e: Exception) {
                    binding.storeImage.visibility = View.GONE
                    binding.storeInitial.visibility = View.VISIBLE
                    binding.storeInitial.text = giftCard.storeName.firstOrNull()?.uppercase() ?: "?"
                }
            } else {
                binding.storeImage.visibility = View.GONE
                binding.storeInitial.visibility = View.VISIBLE
                binding.storeInitial.text = giftCard.storeName.firstOrNull()?.uppercase() ?: "?"
            }
            if (giftCard.zoom != 0f) {
                binding.giftCardLocation.visibility = View.VISIBLE
                try {
                    val geocoder = android.location.Geocoder(binding.root.context, java.util.Locale.getDefault())
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(giftCard.lat, giftCard.lng, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        val locationText = address.locality ?: address.subAdminArea ?: address.adminArea ?: "Location set"
                        binding.giftCardLocation.text = "📍 $locationText"
                    } else {
                        binding.giftCardLocation.text = "📍 Location set"
                    }
                } catch (e: Exception) {
                    binding.giftCardLocation.text = "📍 Location set"
                }
            } else {
                binding.giftCardLocation.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                listener.onGiftCardClick(giftCard)
            }
        }
    }
}