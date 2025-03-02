package qed.interview.android.glimbo.journal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import kotlinx.coroutines.flow.StateFlow

// Journal activity.
//
// Task 4 - journal entries UI
// Our designers have cooked up a detailed design for displaying journal entries.
// Implement a UI for journal entries according to the sketch available at:
// https://waciejm.github.io/My-Pet-Glimbo/journal-sketch.png
//
class JournalActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: JournalViewModel by viewModels()
        val entries: StateFlow<List<JournalEntry>> = viewModel.entries

        setContent {
            // TODO
        }
    }
}

data class JournalEntry(
    val title: String,
    val date: String,
    val text: String,
)
