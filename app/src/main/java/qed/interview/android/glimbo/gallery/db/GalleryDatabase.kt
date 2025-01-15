package qed.interview.android.glimbo.gallery.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.UUID

@Database(
    entities = [
        GalleryEntity::class,
    ],
    version = 1,
)
abstract class GalleryDatabase: RoomDatabase() {
    abstract fun galleryDao(): GalleryDao
}

fun getTempGalleryDatabase(context: Context): GalleryDatabase {
    val db = Room.inMemoryDatabaseBuilder(
        context,
        GalleryDatabase::class.java,
    ).allowMainThreadQueries().build()

    val exampleEntities = listOf(
        GalleryEntity(
            uuid = UUID.randomUUID(),
            name = "glimbo george kowalski",
        ),
        GalleryEntity(
            uuid = UUID.randomUUID(),
            name = "glimbo joe ligma",
        ),
        GalleryEntity(
            uuid = UUID.randomUUID(),
            name = "glimbo % wazowski",
        ),
    )
    exampleEntities.forEach { entity ->
        db.galleryDao().insert(entity)
    }

    return db
}
