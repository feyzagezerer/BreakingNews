package com.example.breakingnews.data.local

import androidx.room.TypeConverter
import com.example.breakingnews.data.model.Source

class Converters {
    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }
}