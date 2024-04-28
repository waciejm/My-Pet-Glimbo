package qed.interview.android.glimbo.hatch

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import qed.interview.android.glimbo.R
import qed.interview.android.glimbo.ui.theme.MyPetGlimboTheme
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

// Task 1
//
// We've noticed that rotating the phone causes the Glimbo Hatching Timer™ to reset.
// Fix it so that all of our users can experience the miracle of life
// in a timely fashion regardless of their screen auto-rotation circumstances.
//
class HatchActivity : ComponentActivity() {

    private lateinit var hatchingState: MutableState<HatchingState>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val state = HatchingState.Hatching(
            hatchingStart = Instant.now(),
            hatchingDuration = 10.seconds,
        )
        hatchingState = mutableStateOf(state)

        Timer().schedule(state.hatchingDuration.inWholeMilliseconds) {
            Log.i("HatchActivity", "Glimbo hath hatched")
            hatchingState.value = HatchingState.Hatched
        }

        setContent {
            MyPetGlimboTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    HatchingView(state = hatchingState.value)
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

sealed interface HatchingState {
    data object Hatched: HatchingState

    data class Hatching(
        val hatchingStart: Instant,
        val hatchingDuration: Duration,
    ): HatchingState
}

@Composable
private fun HatchingView(state: HatchingState) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
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

                val secondsLeft = remember { mutableIntStateOf(0) }
                LaunchedEffect(state) {
                    val hatchingEnd = state.hatchingStart + state.hatchingDuration.toJavaDuration()
                    secondsLeft.intValue = ChronoUnit
                        .SECONDS
                        .between(Instant.now(), hatchingEnd)
                        .toInt() + 1
                    while (true) {
                        delay(1.seconds)
                        secondsLeft.intValue -= 1
                    }
                }
                Text(text = "Glimbo will hatch in ${secondsLeft.intValue} seconds...")
            }
        }
    }
}
