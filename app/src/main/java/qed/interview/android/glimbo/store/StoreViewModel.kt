package qed.interview.android.glimbo.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoreViewModel : ViewModel() {

    private val storeEntriesClient = StoreEntriesClient()

    private val storePurchaseClient = StorePurchaseClient()

    private val storeState = MutableStateFlow<StoreState>(StoreState.Loading)
    val state: StateFlow<StoreState>
        get() = storeState

    init {
        viewModelScope.launch {
            val backpack = async {
                storePurchaseClient.getCurrentBackpack()
            }
            val entries = async {
                storeEntriesClient
                    .getStoreEntries()
                    .map {
                        StoreEntry(
                            name = it.name,
                            price = it.price,
                            buyCallback = { onBuyStoreEntry(name = it.name, price = it.price) },
                        )
                    }
            }
            storeState.value = StoreState.Loaded(
                backpack = backpack.await(),
                entries = entries.await(),
            )
        }
    }

    private fun onBuyStoreEntry(name: String, price: Int) = viewModelScope.launch {
        when (val currentState = storeState.value) {
            StoreState.Loading -> {}
            is StoreState.Loaded -> {
                storeState.value = currentState.copy(
                    backpack = storePurchaseClient.attemptPurchase(name, price),
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

sealed interface StoreState {
    data object Loading: StoreState

    data class Loaded(
        val backpack: Backpack,
        val entries: List<StoreEntry>,
    ): StoreState
}
