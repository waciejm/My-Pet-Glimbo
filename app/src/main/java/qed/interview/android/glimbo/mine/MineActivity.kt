package qed.interview.android.glimbo.mine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import qed.interview.android.glimbo.R
import qed.interview.android.glimbo.ui.theme.MyPetGlimboTheme

// Mine activity.
//
// Task 3 - missed notifications.
// When MineActivity is open the user is mining diamonds. This mining continues when users
// turn off the screen. When a user mines some diamonds we show them a pop-up message.
// However, if the user mines diamonds with the screen turned off, they might never see the message.
// Pls fix so that if diamonds are mined while the UI is not being displayed,
// the pop-up message is not shown until the UI is visible again.
class MineActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<MineViewModel>()

        setContent {
            MyPetGlimboTheme {
                Scaffold { paddingValues ->
                    MineView(
                        modifier = Modifier
                            .padding(paddingValues)
                            .consumeWindowInsets(paddingValues),
                        state = viewModel.state,
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

data class MineState(
    val notifications: SnapshotStateList<String>,
)

@Composable
private fun MineView(
    modifier: Modifier = Modifier,
    state: MineState,
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
            for (notification in state.notifications) {
                Card {
                    Text(
                        text = notification,
                        modifier = Modifier.padding(10.dp),
                    )
                }
            }
        }
    }
}
