package qed.interview.android.glimbo.gallery.db

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface GalleryDao {
    @Insert
    fun insert(galleryEntity: GalleryEntity)
}