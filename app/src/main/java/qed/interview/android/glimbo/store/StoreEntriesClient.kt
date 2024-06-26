package qed.interview.android.glimbo.store

import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import kotlin.time.Duration.Companion.seconds

// Task 2
//
// Our backend team has finally finished implementing the store entries API.
// Replace these mocked entries with entries downloaded from the following URL:
// https://waciejm.github.io/My-Pet-Glimbo/store/entries.json
//
class StoreEntriesClient {

    suspend fun getStoreEntries(): List<Entry> {
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

    @Serializable
    data class Entry(
        val name: String,
        val price: Int,
    )
}
