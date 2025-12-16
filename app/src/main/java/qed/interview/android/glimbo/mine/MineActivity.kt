package qed.interview.android.glimbo.mine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import qed.interview.android.glimbo.R
import qed.interview.android.glimbo.ui.theme.MyPetGlimboTheme
import qed.interview.android.glimbo.ui.theme.setUpEdgeToEdgeOnCreate

// Mine activity.
//
// Task 3 - missed notifications.
//
// When MineActivity is open the user is mining diamonds. This mining continues when users
// turn off the screen. When a user mines some diamonds we show them a pop-up message.
// The message is only shown for 5 seconds.
//
// However, if the user mines diamonds with the screen turned off, they might never see the message.
//
// Pls fix so that if diamonds are mined while the screen is off,
// the pop-up message is not shown until the screen is turned on again.
//
// We can assume that the screen being on is equivalent to Activity being STARTED.
//
// Solution:
// 1. Put new notifications in some sort of queue instead of consuming immediately.
// 2. Consume notifications from queue only if activity is STARTED state,
//    e.g. by launching coroutine in Activity.lifecycleScope and using repeatOnLifecycle(...)
//
class MineActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpEdgeToEdgeOnCreate()

        val viewModel by viewModels<MineViewModel>()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.consumeNotifications()
            }
        }

        setContent {
            MyPetGlimboTheme {
                Scaffold { paddingValues ->
                    MineView(
                        modifier = Modifier
                            .padding(paddingValues)
                            .consumeWindowInsets(paddingValues),
                        notifications = viewModel.notifications.collectAsStateWithLifecycle().value,
                    )
                }
            }
        }
    }
}

@Composable
private fun MineView(
    modifier: Modifier = Modifier,
    notifications: List<MineNotification>,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.mine_diamonds),
            contentDescription = null,
            modifier = Modifier.size(256.dp).align(Alignment.Center),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Bottom),
            horizontalAlignment = Alignment.End,
        ) {
            for (notification in notifications) {
                key(notification) {
                    val visible = remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) { visible.value = true }
                    AnimatedVisibility(visible = visible.value) {
                        Card {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Text(text = notification.message)
                            }
                        }
                    }
                }
            }
        }
    }
}
