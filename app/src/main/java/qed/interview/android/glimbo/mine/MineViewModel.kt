package qed.interview.android.glimbo.mine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

class MineViewModel : ViewModel() {

    private val notificationsQueue = Channel<MineNotification>(capacity = Channel.UNLIMITED)

    private val _notifications = MutableStateFlow<List<MineNotification>>(listOf())

    val notifications: StateFlow<List<MineNotification>>
        get() = _notifications.asStateFlow()

    init {
        viewModelScope.launch {
            mockMineDiamonds().collect { notification ->
                notificationsQueue.send(notification)
            }
        }
    }

    suspend fun consumeNotifications() {
        notificationsQueue.receiveAsFlow().collect { notification ->
            addNotification(notification)
            viewModelScope.launch {
                delay(5.seconds)
                removeNotification(notification)
            }
        }
    }

    private fun addNotification(notification: MineNotification) {
        _notifications.value += notification
    }

    private fun removeNotification(notification: MineNotification) {
        _notifications.value -= notification
    }
}

data class MineNotification(
    val timestamp: Instant,
    val message: String,
)

fun mockMineDiamonds(): Flow<MineNotification> = flow {
    while (true) {
        delay(2.seconds)
        val diamonds = Random.nextInt().mod(3) + 1
        emit(
            MineNotification(
                timestamp = Clock.System.now(),
                message = "mined $diamonds diamond",
            ),
        )
    }
}
