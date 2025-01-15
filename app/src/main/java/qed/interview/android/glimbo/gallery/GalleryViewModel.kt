package qed.interview.android.glimbo.gallery

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import qed.interview.android.glimbo.gallery.db.GalleryEntity
import qed.interview.android.glimbo.gallery.db.getTempGalleryDatabase

class GalleryViewModel(context: Context) : ViewModel() {
    companion object {
        fun factory(context: Context) = viewModelFactory {
            initializer { GalleryViewModel(context) }
        }
    }

    private val db = getTempGalleryDatabase(context)
    private val galleryDao = db.galleryDao()

    private val galleryState = MutableStateFlow(
        GalleryState(
            filter = "",
            entries = listOf(),
            onFilterUpdate = ::onFilterUpdate,
        ),
    )
    val state: StateFlow<GalleryState>
        get() = galleryState

    private fun onFilterUpdate(filter: String) {
        // TODO
    }
}

data class GalleryState(
    val filter: String,
    val entries: List<GalleryEntry>,
    val onFilterUpdate: (String) -> Unit,
)

data class GalleryEntry(
    val name: String,
) {
    companion object {
        fun fromEntity(entity: GalleryEntity): GalleryEntry {
            return GalleryEntry(entity.name)
        }
    }
}
