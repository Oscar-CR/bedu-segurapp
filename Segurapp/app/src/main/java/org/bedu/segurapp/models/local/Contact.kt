package org.bedu.segurapp.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity
data class Contact constructor(
    @PrimaryKey(autoGenerate = true)val id:Int = 0,
    @ColumnInfo val name:String?,
    @ColumnInfo val phone: String?
)