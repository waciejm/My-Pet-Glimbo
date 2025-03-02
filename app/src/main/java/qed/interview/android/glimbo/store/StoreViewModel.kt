package qed.interview.android.glimbo.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoreViewModel : ViewModel() {

    private val storeEntriesClient = StoreEntriesClient()

    private val storeState = MutableStateFlow<StoreState>(StoreState.Loading)
    val state: StateFlow<StoreState>
        get() = storeState

    init {
        viewModelScope.launch {
            storeState.value = StoreState.Loaded(
                backpack = Backpack(
                    money = 100,
                    productsOwned = mapOf(),
                ),
                entries = storeEntriesClient
                    .getStoreEntries()
                    .map {
                        StoreEntry(
                            name = it.name,
                            price = it.price,
                            buyCallback = { onBuyStoreEntry(name = it.name, price = it.price) },
                        )
                    },
            )
        }
    }

    private fun onBuyStoreEntry(name: String, price: Int) = viewModelScope.launch {
        when (val currentState = storeState.value) {
            StoreState.Loading -> {}
            is StoreState.Loaded -> {
                storeState.value = currentState.copy(
                    backpack = Backpack(
                        money = currentState.backpack.money - price,
                        productsOwned = currentState.backpack.productsOwned.toMutableMap().apply {
                            this[name] = (this[name] ?: 0) + 1
                        }
                    ),
                )
            }
        }
    }
}

data class Backpack(
    val money: Int,
    // Map of store entry name to amount owned.
    val productsOwned: Map<String, Int>
)

data class StoreEntry(
    val name: String,
    val price: Int,
    val buyCallback: () -> Unit,
)
