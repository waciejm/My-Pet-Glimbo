package qed.interview.android.glimbo.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoreViewModel : ViewModel() {

    private val storeEntriesClient = StoreEntriesClient()

    private val storeState = MutableStateFlow<StoreState>(StoreState.Loading)
    val state: StateFlow<StoreState>
        get() = storeState

    init {
        viewModelScope.launch {
            storeState.value = storeEntriesClient.getStoreEntries().fold(
                onSuccess = { entries ->
                    StoreState.Loaded(
                        backpack = Backpack(money = 100, productsOwned = mapOf()),
                        entries = entries.map {
                            StoreEntry(
                                name = it.name,
                                price = it.price,
                            )
                        },
                    )
                },
                onFailure = {
                    StoreState.Error
                }
            )
        }
    }

    fun buy(name: String) {
        storeState.update { currentState ->
            when (currentState) {
                StoreState.Loading -> {
                    currentState
                }
                StoreState.Error -> {
                    currentState
                }
                is StoreState.Loaded -> {
                    val price = currentState.entries
                        .find { it.name == name }
                        ?.price
                        ?: return@update currentState
                    val newBackpack = Backpack(
                        money = currentState.backpack.money - price,
                        productsOwned = currentState.backpack.productsOwned.toMutableMap().apply {
                            this[name] = (this[name] ?: 0) + 1
                        }
                    )
                    currentState.copy(backpack = newBackpack)
                }
            }
        }
    }
}

sealed interface StoreState {
    data object Loading: StoreState

    data object Error: StoreState

    data class Loaded(
        val backpack: Backpack,
        val entries: List<StoreEntry>,
    ): StoreState
}

data class Backpack(
    val money: Int,
    // Map of store entry name to amount owned.
    val productsOwned: Map<String, Int>
)

data class StoreEntry(
    val name: String,
    val price: Int,
)
