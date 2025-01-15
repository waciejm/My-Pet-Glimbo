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
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import qed.interview.android.glimbo.ui.theme.MyPetGlimboTheme

// Food store activity.
//
// Allows the user to purchase food from a list of available goods.
// The list of food is downloaded from
//
// Task 2 - goto StoreEntriesClient.kt
//
// Task 3 - food time travel
// We've noticed that buying two different food items in succession leads to some funky behavior.
// Steps to reproduce:
// - buy a food item
// - quickly buy another, different food item
// - observe the amount of first food item in backpack increase then decrease
// Pls fix
//
class StoreActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: StoreViewModel by viewModels()

        setContent {
            MyPetGlimboTheme {
                Scaffold { paddingValues ->
                    StoreView(
                        modifier = Modifier
                            .padding(paddingValues)
                            .consumeWindowInsets(paddingValues),
                        storeState = viewModel.state.collectAsState().value,
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
    modifier: Modifier = Modifier,
    storeState: StoreState,
) {
    when (storeState) {
        StoreState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }
        }
        is StoreState.Loaded -> {
            Column(modifier) {
                StoreHeader(money = storeState.backpack.money)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    StoreBackpackView(productsOwned = storeState.backpack.productsOwned)
                    Spacer(modifier = Modifier.height(28.dp))
                    StoreEntriesView(
                        entries = storeState.entries,
                        money = storeState.backpack.money,
                    )
                }
            }
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
            text = "\$$money",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
private fun StoreBackpackView(productsOwned: Map<String, Int>) {
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
            if (productsOwned.isEmpty()) {
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
                    productsOwned.toSortedMap().entries.forEachIndexed { index, (name, count) ->
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
                            verticalAlignment = Alignment.CenterVertically,
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
private fun StoreEntriesView(entries: List<StoreEntry>, money: Int) {
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
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                entries.forEachIndexed { index, storeEntry ->
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
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(storeEntry.name)
                        Button(
                            onClick = storeEntry.buyCallback,
                            enabled = money >= storeEntry.price,
                        ) {
                            Text("Buy for \$${storeEntry.price}")
                        }
                    }
                }
            }
        }
    }
}
