package qed.interview.android.glimbo.store

import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.seconds

class StoreEntriesClient {

    suspend fun getStoreEntries(): List<Entry> {
        // mock request
        delay(1.seconds)
        return listOf(
            Entry(
                name = "food1",
                price = 1,
            ),
            Entry(
                name = "food2",
                price = 2,
            ),
            Entry(
                name = "food3",
                price = 3,
            ),
        )
    }
}

@Serializable
data class Entry(
    val name: String,
    val price: Int,
)
