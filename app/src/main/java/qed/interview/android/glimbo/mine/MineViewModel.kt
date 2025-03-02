package qed.interview.android.glimbo.mine

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class MineViewModel : ViewModel() {
    val state = MineState(notifications = mutableStateListOf())

    init {
        viewModelScope.launch {
            makeFakeMine().mine().collect { notification ->
                state.notifications.add(notification)
                viewModelScope.launch {
                    delay(5.seconds)
                    state.notifications.removeAt(0)
                }
            }
        }
    }
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

interface Mine {
    suspend fun mine(): Flow<String>
}

fun makeFakeMine(): Mine = object : Mine {
    override suspend fun mine() = flow {
        while (true) {
            delay(2.seconds)
            val diamonds = Random.nextInt().mod(3) + 1
            emit("mined $diamonds diamond")
        }
    }
}