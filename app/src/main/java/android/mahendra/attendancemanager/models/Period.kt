package android.mahendra.attendancemanager.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "period_table")
class Period(@field:ColumnInfo(name = "subject_title") var subjectTitle: String?,
             @field:ColumnInfo(name = "period_number") var periodNumber: Int,
             @field:ColumnInfo(name = "week_day") var weekDay: Int) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var periodId: Long = 0
}