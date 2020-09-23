package nl.soffware.madlevel4task1.dataaccess

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "itemTable")
data class Item(
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "amount")
    var amount: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
){
}