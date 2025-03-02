package qed.interview.android.glimbo.journal

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

class JournalViewModel : ViewModel() {
    val entries: StateFlow<List<JournalEntry>> = MutableStateFlow(
        listOf(
            JournalEntry(
                title = "Time to Say Glimbye",
                date = LocalDate.of(2024, 4, 28).toString(),
                text = loremIpsum(),
            ),
            JournalEntry(
                title = "A Very Glimberry Christmas",
                date = LocalDate.of(2023, 12, 24).toString(),
                text = loremIpsum(),
            ),
            JournalEntry(
                title = "It's a Glimberful Life",
                date = LocalDate.of(2023, 7, 11).toString(),
                text = loremIpsum(),
            ),
            JournalEntry(
                title = "Chasing Glimbo",
                date = LocalDate.of(2023, 6, 14).toString(),
                text = loremIpsum(),
            ),
        ),
    )
}

private fun loremIpsum() =
    "Lorem ipsum dolor sit amet, " +
    "consectetur adipiscing elit, " +
    "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
    "Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
    "laboris nisi ut aliquip ex ea commodo consequat. " +
    "Duis aute irure dolor in reprehenderit in voluptate velit " +
    "esse cillum dolore eu fugiat nulla pariatur. " +
    "Excepteur sint occaecat cupidatat non proident, " +
    "sunt in culpa qui officia deserunt mollit anim id est laborum."

