package qed.interview.android.glimbo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import qed.interview.android.glimbo.gallery.GalleryActivity
import qed.interview.android.glimbo.hatch.HatchActivity
import qed.interview.android.glimbo.journal.JournalActivity
import qed.interview.android.glimbo.store.StoreActivity
import qed.interview.android.glimbo.ui.theme.MyPetGlimboTheme
import kotlin.reflect.KClass

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPetGlimboTheme {
                Scaffold { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .consumeWindowInsets(paddingValues)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Button(onClick = { launchActivity(HatchActivity::class) }) {
                            Text("Hatch activity \uD83E\uDD5A")
                        }
                        Button(onClick = { launchActivity(StoreActivity::class) }) {
                            Text("Store activity \uD83D\uDED2")
                        }
                        Button(onClick = { launchActivity(GalleryActivity::class) }) {
                            Text("Gallery activity \uD83D\uDCF7")
                        }
                        Button(onClick = { launchActivity(JournalActivity::class) }) {
                            Text("Journal activity \uD83D\uDD6F\uFE0F")
                        }
                    }
                }
            }
        }
    }

    private fun launchActivity(activityClass: KClass<out Activity>) {
        val intent = Intent(this, activityClass.java)
        startActivity(intent)
    }
}
