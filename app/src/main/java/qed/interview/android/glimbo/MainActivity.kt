package qed.interview.android.glimbo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Button(onClick = { launchActivity(HatchActivity::class) }) {
                            Text("Hatch Glimbo")
                        }
                        Button(onClick = { launchActivity(StoreActivity::class) }) {
                            Text("Buy Glimbo food")
                        }
                        Button(onClick = { launchActivity(JournalActivity::class) }) {
                            Text("Remember Glimbo \uD83D\uDD6F\uFE0F")
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
