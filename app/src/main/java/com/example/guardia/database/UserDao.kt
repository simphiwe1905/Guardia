package com.example.guardia.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/*
 Data Access Object (DAO) for the 'users' table.
 This interface defines the database operations for User entities.
 Room generates the implementation for these methods at compile time.
 */
@Dao
interface UserDao {

    /*
     Inserts a new user into the database.
     If a user with the same primary key already exists, it will be replaced.
     @return The row ID of the newly inserted user.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long


     //Updates an existing user record in the database.
    @Update
    suspend fun updateUser(user: User)


    //Deletes a user record from the database.

    @Delete
    suspend fun deleteUser(user: User)

    /*
     Verifies user credentials for login.
     @param username The username provided by the user.
     @param password The password provided by the user.
     @return The User object if credentials match, otherwise null.
     */
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun loginUser(username: String, password: String): User?

    /*
     Retrieves a user by their unique username.
     Used for checking username availability during registration.
     */
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    /*
     Retrieves a user by their email address.
     Used for checking email uniqueness during registration.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?


    //Retrieves a specific user's details using their ID.

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): User?

    /*
     Retrieves all users from the database as a Flow.
     Flow allows observing database changes in real-time.
     */
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>
}
