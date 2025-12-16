package qed.interview.android.glimbo.hatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.LongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import qed.interview.android.glimbo.R
import qed.interview.android.glimbo.ui.theme.MyPetGlimboTheme
import qed.interview.android.glimbo.ui.theme.setUpEdgeToEdgeOnCreate
import kotlin.time.ComparableTimeMark
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

// Glimbo egg hatching activity.
//
// Task 1 - timer reset
//
// We've noticed that rotating the phone causes egg hatching to reset.
//
// Pls fix so that does not happen as long as the activity is open.
//
class HatchActivity : ComponentActivity() {

    private val hatchState = MutableStateFlow<HatchState>(HatchState.NotHatching)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpEdgeToEdgeOnCreate()

        setContent {
            MyPetGlimboTheme {
                Scaffold { paddingValues ->
                    HatchingView(
                        modifier = Modifier
                            .padding(paddingValues)
                            .consumeWindowInsets(paddingValues),
                        state = hatchState.collectAsStateWithLifecycle().value,
                        onStartHatching = ::onStartHatching,
                    )
                }
            }
        }
    }

    private fun onStartHatching() {
        val hatchTime = 5.seconds
        val hatchEnd = TimeSource.Monotonic.markNow() + hatchTime

        hatchState.value = HatchState.Hatching(hatchEnd = hatchEnd)

        lifecycleScope.launch {
            delay(hatchTime)
            hatchState.value = HatchState.Hatched
        }
    }
}

sealed interface HatchState {
    data object NotHatching: HatchState

    data class Hatching(
        val hatchEnd: ComparableTimeMark,
    ): HatchState

    data object Hatched: HatchState
}

@Composable
private fun HatchingView(
    modifier: Modifier = Modifier,
    state: HatchState,
    onStartHatching: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (state) {
            HatchState.NotHatching -> {
                Button(
                    onClick = onStartHatching,
                ) {
                    Text("Start hatching")
                }
            }

            is HatchState.Hatching -> {
                Image(
                    painter = painterResource(id = R.drawable.glimbo_egg_hatching),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp),
                )
                val secondsLeft = secondsUntilTimeMark(end = state.hatchEnd)
                Text(text = "Glimbo will hatch in ${secondsLeft.longValue} seconds...")
            }

            HatchState.Hatched -> {
                Image(
                    painter = painterResource(id = R.drawable.glimbo_egg_hatched),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp),
                )
                Text(text = "Glimbo has emerged...")
            }
        }
    }
}

@Composable
private fun secondsUntilTimeMark(end: ComparableTimeMark): LongState {
    val displaySecondsLeft = remember { mutableLongStateOf(0) }

    LaunchedEffect(end) {
        val initDurationLeft = end - TimeSource.Monotonic.markNow()
        var wholeSecondsLeft = initDurationLeft.inWholeSeconds
        displaySecondsLeft.longValue = wholeSecondsLeft + 1

        while (wholeSecondsLeft > 0) {
            val durationLeft = end - TimeSource.Monotonic.markNow()
            val untilWholeSecondsLeft = durationLeft - wholeSecondsLeft.seconds

            delay(untilWholeSecondsLeft)
            displaySecondsLeft.longValue = wholeSecondsLeft
            wholeSecondsLeft -= 1
        }
    }

    return displaySecondsLeft
}
