package qed.interview.android.glimbo.hatch

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import qed.interview.android.glimbo.R
import qed.interview.android.glimbo.ui.theme.MyPetGlimboTheme
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.time.ComparableTimeMark
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

// Glimbo egg hatching activity.
//
// Upon opening the activity a short (5 second) hatching timer starts
// and counts down until the creature hatches from the egg.
//
// Task 1 - timer reset
// We've noticed that rotating the phone causes the Glimbo Hatching Timer™ to reset.
// Pls fix so that the timer does not reset as long as the activity is open.
//
class HatchActivity : ComponentActivity() {

    private val hatchEnd = TimeSource.Monotonic.markNow() + 5.seconds

    private val hatchingState = MutableStateFlow<HatchingState>(
        HatchingState.Hatching(hatchEnd = hatchEnd),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toHatchEnd = hatchEnd - TimeSource.Monotonic.markNow()

        Timer().schedule(toHatchEnd.inWholeMilliseconds) {
            Log.i("HatchActivity", "Glimbo broke free...")
            hatchingState.value = HatchingState.Hatched
        }

        setContent {
            MyPetGlimboTheme {
                Scaffold { paddingValues ->
                    HatchingView(
                        modifier = Modifier
                            .padding(paddingValues)
                            .consumeWindowInsets(paddingValues),
                        state = hatchingState.collectAsState().value,
                    )
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

@Composable
private fun HatchingView(
    modifier: Modifier = Modifier,
    state: HatchingState,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (state) {
            HatchingState.Hatched -> {
                Image(
                    painter = painterResource(id = R.drawable.glimbo_egg_hatched),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp),
                )
                Text(text = "Glimbo has emerged...")
            }

            is HatchingState.Hatching -> {
                Image(
                    painter = painterResource(id = R.drawable.glimbo_egg_hatching),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp),
                )

                val displayedSecondsLeft = remember { mutableLongStateOf(0) }
                LaunchedEffect(state) {
                    val initDurationLeft = state.hatchEnd - TimeSource.Monotonic.markNow()
                    var wholeSecondsLeft = initDurationLeft.inWholeSeconds
                    displayedSecondsLeft.longValue = wholeSecondsLeft + 1

                    while (wholeSecondsLeft > 0) {
                        val durationLeft = state.hatchEnd - TimeSource.Monotonic.markNow()
                        val untilWholeSecondsLeft = durationLeft - wholeSecondsLeft.seconds

                        delay(untilWholeSecondsLeft)
                        displayedSecondsLeft.longValue = wholeSecondsLeft
                        wholeSecondsLeft -= 1
                    }
                }

                Text(text = "Glimbo will hatch in ${displayedSecondsLeft.longValue} seconds...")
            }
        }
    }
}

sealed interface HatchingState {
    data class Hatching(
        val hatchEnd: ComparableTimeMark,
    ): HatchingState

    data object Hatched: HatchingState
}
