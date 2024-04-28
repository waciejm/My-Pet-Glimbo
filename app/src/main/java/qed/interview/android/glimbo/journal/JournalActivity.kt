package qed.interview.android.glimbo.journal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels

// Task 4
//
// Oh no! We've been asked to justify why we collect so much data about our users.
// Fortunately we've come up with a solution! Use the data to generate journal entries
// so that we can say we have a legitimate reason to store all that data!
// Implement the Journal UI based on this sketch created by our finest UI designer:
// https://waciejm.github.io/My-Pet-Glimbo/journal-sketch.png
//
class JournalActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: JournalViewModel by viewModels()
        val entries = viewModel.entries
    }
}