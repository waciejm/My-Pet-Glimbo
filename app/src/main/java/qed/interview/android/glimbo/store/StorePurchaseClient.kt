package qed.interview.android.glimbo.store

import android.util.Log
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

// Fake store purchase client implementation.
//
// The body of this class represents the logic implemented on the backend.
// Unfortunately for whatever reason we are unable to fix the problem there
// and have to work around it...
//
class StorePurchaseClient {

    suspend fun getCurrentBackpack(): Backpack {
        // request delay
        delay(0.1.seconds)

        // db roundtrip delay
        delay(0.1.seconds)
        val currentBackpack = backpackInDB
        delay(0.5.seconds)

        // response delay
        delay(0.1.seconds)
        return currentBackpack
    }

    suspend fun attemptPurchase(
        itemName: String,
        itemPrice: Int,
    ): Backpack {
        // request delay
        delay(0.1.seconds)

        // db roundtrip delay
        delay(0.1.seconds)
        val currentBackpack = backpackInDB
        delay(0.5.seconds)

        return if (currentBackpack.money >= itemPrice) {
            Log.i("StorePurchaseClient", "bought $itemName for $itemPrice")
            val newBackpack = Backpack(
                money = currentBackpack.money - itemPrice,
                productsOwned = currentBackpack.productsOwned.toMutableMap().apply {
                    this[itemName] = (this[itemName] ?: 0) + 1
                },
            )

            // db roundtrip delay
            delay(0.5.seconds)
            backpackInDB = newBackpack
            delay(0.1.seconds)

            newBackpack
        } else {
            Log.i("StorePurchaseClient", "failed to buy $itemName - not enough money")
            currentBackpack
        }
    }

    private var backpackInDB = Backpack(
        money = 100,
        productsOwned = mapOf(),
    )
}