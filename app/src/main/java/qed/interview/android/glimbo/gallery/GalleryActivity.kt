package qed.interview.android.glimbo.gallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import qed.interview.android.glimbo.ui.theme.MyPetGlimboTheme

// Glimbo gallery activity.
//
// A gallery in which users can admire a list of other users pets.
//
// Task 4 - pet filtering
// Glimbos have become too popular. There are too many Glimbos in the gallery.
// We can't show all of them at once.
// Implement gallery entry filtering logic:
// - do not show any entries if the filter is empty
// - show all entries where the filter is a substring of entry name
//
class GalleryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: GalleryViewModel by viewModels { GalleryViewModel.factory(context = this) }

        setContent {
            MyPetGlimboTheme {
                Scaffold { paddingValues ->
                    GalleryView(
                        modifier = Modifier
                            .padding(paddingValues)
                            .consumeWindowInsets(paddingValues),
                        state = viewModel.state.collectAsState().value,
                    )
                }
            }
        }
    }
}

@Composable
private fun GalleryView(
    modifier: Modifier,
    state: GalleryState,
) {
    Column(modifier) {
        GalleryFilterBar(state.filter, state.onFilterUpdate)
        GalleryEntriesList(state.entries)
    }
}

@Composable
private fun GalleryFilterBar(
    filter: String,
    onFilterUpdate: (String) -> Unit,
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        value = filter,
        onValueChange = onFilterUpdate,
    )
}

@Composable
private fun GalleryEntriesList(entries: List<GalleryEntry>) {
    for (entry in entries) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = entry.name,
            )
        }
    }
}
