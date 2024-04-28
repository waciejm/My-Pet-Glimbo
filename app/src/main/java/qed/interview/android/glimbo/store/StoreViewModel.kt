package qed.interview.android.glimbo.store

import android.util.Log
import androidx.compose.runtime.IntState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class StoreViewModel : ViewModel() {

    private val storeEntriesClient by lazy { StoreEntriesClient() }

    private val storePurchaseClient by lazy { StorePurchaseClient() }

    private val money = mutableIntStateOf(100)
    fun getMoneyState(): IntState = money

    private val backpack = mutableStateOf(mapOf<String, Int>())
    fun getBackpackState(): State<Map<String, Int>> = backpack

    private val entries = mutableStateOf<StoreEntriesState>(StoreEntriesState.Loading)
    fun getEntriesState(): State<StoreEntriesState> = entries

    fun loadStoreEntries() = viewModelScope.launch(Dispatchers.IO) {
        val loadedEntries = storeEntriesClient
            .getStoreEntries()
            .map {
                StoreEntry(
                    name = it.name,
                    price = it.price,
                    buyCallback = { onBuyStoreEntry(name = it.name, price = it.price) },
                )
            }
        entries.value = StoreEntriesState.Loaded(loadedEntries)
    }

    private fun onBuyStoreEntry(name: String, price: Int) = viewModelScope.launch(Dispatchers.IO) {
        val response = storePurchaseClient.performPurchase(
            moneyBeforePurchase = money.intValue,
            itemName = name,
            itemPrice = price,
        )
        when (response) {
            StorePurchaseClient.PurchaseResponse.Failed -> {}
            is StorePurchaseClient.PurchaseResponse.Purchased -> {
                money.intValue = response.newMoney
                backpack.value = backpack.value.toMutableMap().apply {
                    this[name] = (this[name] ?: 0) + 1
                }
            }
        }
    }
}

data class StoreEntry(
    val name: String,
    val price: Int,
    val buyCallback: () -> Unit,
)

sealed interface StoreEntriesState {
    data object Loading: StoreEntriesState

    data class Loaded(val entries: List<StoreEntry>): StoreEntriesState
}

//
//
//
//
//
//
//
//
//
//

// fake store purchase client implementation
class StorePurchaseClient {
    suspend fun performPurchase(
        moneyBeforePurchase: Int,
        itemName: String,
        itemPrice: Int,
    ): PurchaseResponse {
        delay(1.seconds)
        return if (moneyBeforePurchase >= itemPrice) {
            Log.i("StorePurchaseClient", "bought $itemName for $itemPrice")
            PurchaseResponse.Purchased(newMoney = moneyBeforePurchase - itemPrice)
        } else {
            Log.i("StorePurchaseClient", "failed to buy $itemName - not enough money")
            PurchaseResponse.Failed
        }
    }

    sealed interface PurchaseResponse {
        data object Failed: PurchaseResponse

        data class Purchased(
            val newMoney: Int,
        ): PurchaseResponse
    }
}
