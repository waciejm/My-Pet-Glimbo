package qed.interview.android.glimbo.store

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET

class StoreEntriesClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://waciejm.github.io/My-Pet-Glimbo/")
        .addConverterFactory(Json.asConverterFactory("application/json; charset=utf-8".toMediaType()))
        .build()

    private val service = retrofit.create(StoreEntriesService::class.java)

    suspend fun getStoreEntries(): Result<List<Entry>> {
        return try {
            Result.success(service.getStoreEntries())
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            Result.failure(e)
        }
    }
}

interface StoreEntriesService {
    @GET("store/entries.json")
    suspend fun getStoreEntries(): List<Entry>
}

@Serializable
data class Entry(
    val name: String,
    val price: Int,
)
