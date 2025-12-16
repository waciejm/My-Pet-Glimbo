package qed.interview.android.glimbo.journal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import qed.interview.android.glimbo.ui.theme.MyPetGlimboTheme
import qed.interview.android.glimbo.ui.theme.setUpEdgeToEdgeOnCreate

// Journal activity.
//
// Task 4 - journal entries UI
//
// Our designers have cooked up a detailed design for displaying journal entries.
// Implement a UI for journal entries according to the sketch available at:
// https://waciejm.github.io/My-Pet-Glimbo/journal-sketch.png
//
// Solution:
// 1. Just implement the view with the correct structure.
// 2. Handle window insets correctly e.g. by using Material3 Scaffold paddingValues.
// 3. Bonus points for attention to details like handling large font sizes and long titles
//    so that the date always displays correctly, etc.
//
class JournalActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpEdgeToEdgeOnCreate()

        val viewModel: JournalViewModel by viewModels()
        val entries: StateFlow<List<JournalEntry>> = viewModel.entries

        setContent {
            MyPetGlimboTheme {
                JournalEntryScreen(entries = entries.value)
            }
        }
    }
}

@Composable
private fun JournalEntryScreen(entries: List<JournalEntry>) {
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .consumeWindowInsets(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(0.dp))
            }
            item {
                Text(
                    text = "Journal",
                    style = MaterialTheme.typography.displaySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            items(items = entries) { entry ->
                JournalEntryCard(entry = entry)
            }
            item {
                Spacer(modifier = Modifier.height(0.dp))
            }
        }
    }
}

@Composable
private fun JournalEntryCard(entry: JournalEntry) {
    Column(
        modifier = Modifier
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = entry.title,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = entry.date,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Text(entry.text)
    }
}
