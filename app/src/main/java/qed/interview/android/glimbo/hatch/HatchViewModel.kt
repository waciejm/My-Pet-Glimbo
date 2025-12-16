package qed.interview.android.glimbo.hatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

class HatchViewModel : ViewModel() {

    private val hatchState = MutableStateFlow<HatchState>(HatchState.NotHatching)

    val state: StateFlow<HatchState>
        get() = hatchState.asStateFlow()

    fun onStartHatching() {
        val hatchTime = 5.seconds
        val hatchEnd = TimeSource.Monotonic.markNow() + hatchTime

        hatchState.value = HatchState.Hatching(hatchEnd = hatchEnd)

        viewModelScope.launch {
            delay(hatchTime)
            hatchState.value = HatchState.Hatched
        }
    }
}
