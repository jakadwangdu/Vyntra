package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * FROM meals WHERE date = :date ORDER BY timestamp DESC")
    fun getMealsByDate(date: String): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals ORDER BY timestamp DESC")
    fun getAllMeals(): Flow<List<MealEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity): Long

    @Delete
    suspend fun deleteMeal(meal: MealEntity)

    @Query("DELETE FROM meals WHERE id = :id")
    suspend fun deleteMealById(id: Int)
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileEntity)
}

@Dao
interface WaterLogDao {
    @Query("SELECT * FROM water_logs WHERE date = :date")
    fun getWaterLog(date: String): Flow<WaterLogEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWaterLog(log: WaterLogEntity)
}

@Dao
interface WorkoutLogDao {
    @Query("SELECT * FROM workout_logs WHERE date = :date ORDER BY timestamp DESC")
    fun getWorkoutsByDate(date: String): Flow<List<WorkoutLogEntity>>

    @Query("SELECT * FROM workout_logs ORDER BY timestamp DESC")
    fun getAllWorkouts(): Flow<List<WorkoutLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutLogEntity): Long
}
