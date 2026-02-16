package com.example.expensetrackerapp

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.parcelize.Parcelize
import kotlinx.coroutines.flow.Flow

import android.os.Parcelable

@Parcelize

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val category: String,
    val date: Long,
    val paymentMethod: String = "Cash",
    val note: String = ""
):Parcelable

data class CategoryTotal(
    val category: String,
    val total: Double
)

data class DailyTotal(
    val date: Long,
    val total: Double
)
data class WeeklyTotal(
    val week: Int,
    val total: Double
)


@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("""
    SELECT category, SUM(amount) as total
    FROM expenses
    WHERE strftime('%m', date/1000, 'unixepoch') = :month
    AND strftime('%Y', date/1000, 'unixepoch') = :year
    GROUP BY category
""")
    fun getCategoryTotals(month: String, year: String): Flow<List<CategoryTotal>>

    @Query("""
    SELECT date, SUM(amount) as total
    FROM expenses
    WHERE strftime('%m', date/1000, 'unixepoch') = :month
    AND strftime('%Y', date/1000, 'unixepoch') = :year
    GROUP BY date
    ORDER BY date ASC
""")
    fun getDailyTotals(month: String, year: String): Flow<List<DailyTotal>>

    @Query("""
    SELECT * FROM expenses
    WHERE strftime('%m', date/1000, 'unixepoch') = :month
    AND strftime('%Y', date/1000, 'unixepoch') = :year
    ORDER BY date DESC
""")
    fun getExpensesByMonth(month: String, year: String): Flow<List<Expense>>

    @Query("SELECT * FROM expenses ORDER BY amount DESC")
    fun getExpensesSortedByAmount(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getExpensesSortedByDate(): Flow<List<Expense>>


    @Query("""
    SELECT 
        ((CAST(strftime('%d', date/1000, 'unixepoch') AS INTEGER) - 1) / 7) + 1 AS week,
        SUM(amount) AS total
    FROM expenses
    WHERE strftime('%m', date/1000, 'unixepoch') = :month
      AND strftime('%Y', date/1000, 'unixepoch') = :year
    GROUP BY week
    ORDER BY week
""")
    fun getWeeklyTotals(month: String, year: String): Flow<List<WeeklyTotal>>




    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)
}


@Database(entities = [Expense::class], version = 2, exportSchema = false)
abstract class ExpenseDatabase : androidx.room.RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getDatabase(context: android.content.Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}