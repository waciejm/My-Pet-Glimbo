package qed.interview.android.glimbo.gallery.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class GalleryEntity(
    @PrimaryKey val uuid: UUID,
    val name: String,
)