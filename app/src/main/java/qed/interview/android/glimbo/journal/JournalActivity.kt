package qed.interview.android.glimbo.journal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

// Journal activity.
//
// Displays a list of journal entries.
//
// Task 5 - journal entries UI
// We want to increase our users attachment to their favorite pet Glimbo.
// Our designers have cooked up a detailed design for displaying journal entries.
// Implement a Compose UI for journal entries according to the sketch available at:
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
    val date: LocalDate,
    val text: String,
)