package qed.interview.android.glimbo.store

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import qed.interview.android.glimbo.ui.theme.MyPetGlimboTheme

// Task 3
//
// Disaster has struck! We've noticed that users are getting more food than they are paying for!
// If the user presses the "Buy" button quickly they are getting free food items!
// Fix this top priority issue!
//
class StoreActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: StoreViewModel by viewModels()

        viewModel.loadStoreEntries()

        setContent {
            MyPetGlimboTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    StoreView(
                        money = viewModel.getMoneyState().intValue,
                        backpack = viewModel.getBackpackState().value,
                        entries = viewModel.getEntriesState().value,
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
private fun StoreView(
    money: Int,
    backpack: Map<String, Int>,
    entries: StoreEntriesState,
) {
    Column {
        StoreHeader(money = money)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            StoreBackpackView(backpack = backpack)
            Spacer(modifier = Modifier.height(28.dp))
            StoreEntriesView(entries = entries, money = money)
        }
    }
}

@Composable
private fun StoreHeader(money: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(
            text = "Store",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "$money mooneeys",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
private fun StoreBackpackView(backpack: Map<String, Int>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Backpack",
            style = MaterialTheme.typography.titleLarge,
        )
        Card(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth(),
        ) {
            if (backpack.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Empty...")
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    backpack.toSortedMap().entries.forEachIndexed { index, (name, count) ->
                        if (index != 0) {
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.background,
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(name)
                            Text(count.toString())
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StoreEntriesView(entries: StoreEntriesState, money: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "For sale",
            style = MaterialTheme.typography.titleLarge,
        )
        Card(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth(),
        ) {
            when (entries) {
                StoreEntriesState.Loading -> StoreEntriesLoadingView()
                is StoreEntriesState.Loaded -> StoreEntriesLoadedListView(
                    entries = entries,
                    money = money,
                )
            }
        }
    }
}

@Composable
private fun StoreEntriesLoadingView() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(20.dp))
        CircularProgressIndicator()
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun StoreEntriesLoadedListView(entries: StoreEntriesState.Loaded, money: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        entries.entries.forEachIndexed { index, storeEntry ->
            if (index != 0) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background,
                )
            }
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(storeEntry.name)
                    Text(
                        text = "${storeEntry.price} mooneeys",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
                Button(
                    onClick = storeEntry.buyCallback,
                    enabled = money >= storeEntry.price,
                ) {
                    Text("Buy")
                }
            }
        }
    }
}
